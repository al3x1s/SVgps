package com.sysgon.svgps.data.model;

/*
 * Copyright 2016 Irving Gonzalez (ialexis93@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.sysgon.svgps.R;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Device {

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    private String placa;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    private String company;

    private String uniqueId;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String lastupdate;

    public String getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(String lastUpdate) {
        this.lastupdate = lastUpdate;
    }

    @Override
    public String toString() {
        return name;
    }

    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public BitmapDescriptor getImageBitmap() {
        switch (image) {
            case "001.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v001);
            case "002.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v002);
            case "003.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v003);
            case "004.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v004);
            case "005.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v005);
            case "006.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v006);
            case "007.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v007);
            case "008.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v008);
            case "009.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v009);
            case "010.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v010);
            case "011.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v011);
            case "012.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v012);
            case "013.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v013);
            case "016.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v016);
            case "017.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v017);
            case "021.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v021);
            case "023.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v023);
            case "024.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v024);
            case "025.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v025);
            case "026.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v026);
            case "027.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v027);
            case "041.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v041);
            case "042.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v042);
            case "043.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v043);
            case "044.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v044);
            case "045.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v045);
            case "046.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v046);
            case "047.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v047);
            case "048.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v048);
            case "049.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v049);
            case "050.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v050);
            case "053.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v053);
            case "054.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v054);
            case "055.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v055);
            case "058.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v058);
            case "061.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v061);
            case "062.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v062);
            case "070.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.v070);
            case "Circulo.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.vcirculo);
            case "dump_truck.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.vdump_truck);
            case "tanker.png":
                return BitmapDescriptorFactory.fromResource(R.drawable.vtanker);
            default:
                return BitmapDescriptorFactory.fromResource(R.drawable.vcirculo);
        }
    }


}