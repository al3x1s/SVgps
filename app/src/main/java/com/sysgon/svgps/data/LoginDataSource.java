package com.sysgon.svgps.data;

import android.util.Log;

import com.sysgon.svgps.data.model.User;

import java.io.IOException;


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    public Result<User> login(User user) {
        try {
            Log.i("TEST", "login LoginDatasource");
            return new Result.Success<>(user);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
