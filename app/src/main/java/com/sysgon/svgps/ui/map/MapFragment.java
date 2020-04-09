package com.sysgon.svgps.ui.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sysgon.svgps.MainApplication;
import com.sysgon.svgps.R;
import com.sysgon.svgps.data.model.Device;
import com.sysgon.svgps.data.model.Event;
import com.sysgon.svgps.data.model.Position;
import com.sysgon.svgps.data.model.User;
import com.sysgon.svgps.webservice.WebService;
import com.sysgon.svgps.webservice.WebServiceCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class MapFragment extends Fragment implements OnMapReadyCallback, AdapterView.OnItemSelectedListener  {
    private static final String TAG = "MapFragment";
    private static final int LOCATION_REQUEST_CODE = 1;
    private MapViewModel homeViewModel;
    private GoogleMap mMap;
    private MapView mapFragment;

    private Spinner mMapTypeSelector;
    private int mMapTypes[] = {
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID
    };

    private Handler handler = new Handler();
    private ObjectMapper objectMapper = new ObjectMapper();
    private Map<Long, Device> devices = new HashMap<>();
    private Map<Long, Position> positions = new HashMap<>();
    private Map<Long, Marker> markers = new HashMap<>();

    private Socket mSocket = null;

    private LinearLayout myLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mMapTypeSelector = root.findViewById(R.id.map_type_selector);
        mMapTypeSelector.setOnItemSelectedListener(this);

        mapFragment = root.findViewById(R.id.map_home);

        mapFragment.onCreate(savedInstanceState);
        mapFragment.onResume(); // needed to get the map to display immediately
        mapFragment.getMapAsync(this);

        myLayout = root.findViewById(R.id.attr_view);

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        closeSocket();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        closeSocket();
    }

    private void closeSocket(){
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket = null;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Mostrar diálogo explicativo
            } else {
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View view = getActivity().getLayoutInflater().inflate(R.layout.view_info_mapmarker, null);
                ((TextView) view.findViewById(R.id.title_marker)).setText(marker.getTitle());
                ((TextView) view.findViewById(R.id.details_marker)).setText(marker.getSnippet());
                return view;
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Position p = (Position) marker.getTag();
                showAttributes(p.getAttributesP());
                myLayout.setVisibility(View.VISIBLE);
                return false;
            }
        });

        mMap.setOnInfoWindowCloseListener(new GoogleMap.OnInfoWindowCloseListener() {
            @Override
            public void onInfoWindowClose(Marker marker) {
                myLayout.setVisibility(View.GONE);
            }
        });

        View locationButton = ((View) mapFragment.findViewById(1).getParent()).findViewById(2);
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rlp.setMargins(0, DpToPx(40), 30, 0);
        createWebSocket();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mMap.setMapType(mMapTypes[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (permissions.length > 0 &&
                    permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            } else {
                Toast.makeText(this.getContext(), R.string.location_permision_error, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showAttributes(Map<String, Object> attr){
        LinearLayout.LayoutParams lpTextView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpTextView.setMargins(DpToPx(5), DpToPx(10), DpToPx(5), DpToPx(10));
        myLayout.removeAllViews();

        Map<String, Object> attrShow = new HashMap<>();
        float battery = 0, power = 0, totalDistance = 0;
        if(attr.containsKey(Event.KEY_BATTERY))  battery = (float)((double) attr.get(Event.KEY_BATTERY));
        if(attr.containsKey(Event.KEY_POWER))    power = (float) ((double) attr.get(Event.KEY_POWER))/100;
        attrShow.put("Bateria GPS", battery + "V");
        attrShow.put("Bateria Vehiculo", power + "V");

        if(attr.containsKey("io1"))  attrShow.put("io1", attr.get("io1"));
        if(attr.containsKey("adc1"))  attrShow.put("adc1", attr.get("adc1"));
        if(attr.containsKey("motion"))  attrShow.put("motion", attr.get("motion"));
        if(attr.containsKey("totalDistance"))  totalDistance = (float) ((double) attr.get("totalDistance"))/1000;
        attrShow.put("Odometro", totalDistance + " Km");

        for (Map.Entry<String, Object> entry : attrShow.entrySet()){
            TextView tv = new TextView(getContext());
            tv.setLayoutParams(lpTextView);
            tv.setTextColor(0XFF2F2F2F);
            tv.setText(entry.getKey() + ": " + entry.getValue());
            myLayout.addView(tv);

            View v = new View(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DpToPx(1), LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins(10, 10, 10, 10);
            v.setLayoutParams(lp);
            v.setBackgroundColor(Color.GRAY);
            myLayout.addView(v);
        }
    }

    private void createWebSocket() {
        final MainApplication application = (MainApplication) getActivity().getApplication();
        application.getServiceAsync(new MainApplication.GetServiceCallback() {
            @Override
            public void onServiceReady(final OkHttpClient client, final Retrofit retrofit, WebService service) {
                User user = application.getUser();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(user.getLatitude(), user.getLongitude()), user.getZoom()));
                service.getDevices().enqueue(new WebServiceCallback<List<Device>>(getContext()) {

                    @Override
                    public void onSuccess(retrofit2.Response<List<Device>> response) {
                        for (Device device : response.body()) {
                            if (device != null) {
                                devices.put(device.getId(), device);
                            }
                        }

                        String ds = MainApplication.DEFAULT_SERVER;
                        HttpUrl url = HttpUrl.parse(ds + MainApplication.DEFAULT_WEP_PORT);

                        final StringBuilder sb = new StringBuilder();
                        for (Cookie c : client.cookieJar().loadForRequest(url)) {
                            sb.append(c.name()).append("=").append(c.value()).append(";");
                        }

                        try {
                            String urlSocket = ds + MainApplication.DEFAULT_SOCKET_PORT;
                            mSocket = IO.socket(urlSocket);
                            mSocket.io().on(Manager.EVENT_TRANSPORT, new Emitter.Listener() {
                                @Override
                                public void call(Object... args) {
                                    Transport transport = (Transport) args[0];
                                    transport.on(Transport.EVENT_REQUEST_HEADERS, new Emitter.Listener() {
                                        @Override
                                        public void call(Object... args) {
                                            @SuppressWarnings("unchecked")
                                            Map<String, List<String>> headers = (Map<String, List<String>>) args[0];
                                            // modify request headers
                                            headers.put("Cookie", Arrays.asList(sb.toString()));
                                        }
                                    });
                                }
                            });

                            mSocket.on(Socket.EVENT_CONNECT, onConnect);
                            mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
                            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
//                            mSocket.on(Socket.EVENT_DISCONNECT, onConnectError);
                            mSocket.on("positions", onNewPositions);
                            mSocket.connect();
                        } catch (URISyntaxException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }

            @Override
            public boolean onFailure() {
                return false;
            }
        });
    }

    private void reconnectWebSocket() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    Log.i(TAG, "trying reconnect socket");
                    if(mSocket == null){
                        createWebSocket();
                    }
                }
            }
        });
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "socket connected");
                    }
                });
            }
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSocket = null;
                        reconnectWebSocket();
                    }
                });
            }
        }
    };

    private Emitter.Listener onNewPositions = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONArray data = (JSONArray) args[0];
                        handlePosition(data);
                    }
                });
            }
        }

        private String formatDetails(Position position) {
            final MainApplication application = (MainApplication) getContext().getApplicationContext();
            final User user = application.getUser();

            SimpleDateFormat dateFormat;
            if(user.getTwelveHourFormat()) {
                dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
            } else {
                dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            }

            String speedUnit = getString(R.string.user_kmh);
            double factor = 1.852;
            if (user.getSpeedUnit() != null) {
                switch (user.getSpeedUnit()) {
                    case "mph":
                        speedUnit = getString(R.string.user_mph);
                        factor = 1.15078;
                        break;
                    case "kn":
                        speedUnit = getString(R.string.user_kn);
                        factor = 1;
                        break;
                    default:
                        speedUnit = getString(R.string.user_kmh);
                        factor = 1.852;
                        break;
                }
            }
            double speed = position.getSpeed() * factor;
            return new StringBuilder()
                    .append(getString(R.string.position_time)).append(": ")
                    .append(dateFormat.format(position.getFixTime())).append('\n')
                    .append(getString(R.string.position_address)).append(": ")
                    .append(position.getAddress().trim()).append('\n')
                    .append(getString(R.string.position_speed)).append(": ")
                    .append(String.format("%.1f", speed)).append(' ')
                    .append(speedUnit).append('\n')
                    .toString();
        }

        private void handlePosition(JSONArray positionsArray) {
            for(int i = 0; i < positionsArray.length(); i++){
                try {
                    JSONObject position = positionsArray.getJSONObject(i);
                    Position p = objectMapper.readValue(position.toString(), Position.class);
                    if (devices.containsKey(p.getDeviceId())) {
                        Device dev = devices.get(p.getDeviceId());
                        LatLng location = new LatLng(p.getLatitude(), p.getLongitude());
                        Marker marker = markers.get(p.getDeviceId());
                        if (marker == null) {
                            marker = mMap.addMarker(new MarkerOptions()
                                    .title("[" + dev.getPlaca() + "] " + dev.getName())
                                    .icon(dev.getImageBitmap())
                                    .position(location));
                            markers.put(p.getDeviceId(), marker);
                        } else {
                            marker.setPosition(location);
                        }
                        marker.setTag(p);
                        marker.setSnippet(formatDetails(p));
                        positions.put(p.getDeviceId(), p);
                    }
                } catch (IOException | JSONException e) {
                    Log.e(TAG, "Exception: " + e.getMessage());
                    continue;
                }
            }
        }
    };

    private int DpToPx(float dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}