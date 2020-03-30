package com.sysgon.svgps.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;

import com.sysgon.svgps.MainApplication;
import com.sysgon.svgps.data.LoginRepository;
import com.sysgon.svgps.data.Result;
import com.sysgon.svgps.R;
import com.sysgon.svgps.data.model.User;
import com.sysgon.svgps.webservice.WebService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(Context context, final String username, final String password) {
        // can be launched in a separate asynchronous job
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences
                .edit()
                .putString(MainApplication.PREFERENCE_EMAIL, username)
                .putString(MainApplication.PREFERENCE_PASSWORD, password)
                .apply();

        final MainApplication application = (MainApplication) context.getApplicationContext();
        application.removeService();

        application.getServiceAsync(new MainApplication.GetServiceCallback() {
            @Override
            public void onServiceReady(OkHttpClient client, Retrofit retrofit, WebService service) {
                Log.i("TEST", "LoginViewModer onServiceReady");
                Result<User> result = loginRepository.login(application.getUser());
                if (result instanceof Result.Success) {
                    User data = ((Result.Success<User>) result).getData();
                    loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
                    preferences.edit().putBoolean(MainApplication.PREFERENCE_AUTHENTICATED, true).apply();
                } else {
                    loginResult.setValue(new LoginResult(R.string.login_failed));
                    preferences.edit().putBoolean(MainApplication.PREFERENCE_AUTHENTICATED, false).apply();
                }
            }

            @Override
            public boolean onFailure() {
                Log.i("TEST", "onFailure");
                Result<User> result = loginRepository.login(null);
                loginResult.setValue(new LoginResult(R.string.login_failed));
                preferences.edit().putBoolean(MainApplication.PREFERENCE_AUTHENTICATED, false).apply();
                return false;
            }
        });

    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
