package com.sysgon.svgps.webservice;

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


import com.sysgon.svgps.data.model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface WebService {
    @FormUrlEncoded
    @POST("/api/session/android")
    Call<User> login(@Field("email") String email, @Field("p") String password);

    @GET("/api/logout")
    Call<Void> logout();


//    @GET("/api/android/devices")
//    Call<List<Device>> getDevices();

//    @GET("/api/commandtypes")
//    Call<List<CommandType>> getCommandTypes(@Query("deviceId") long deviceId);
//
//    @POST("/api/commands")
//    Call<Command> sendCommand(@Body Command command);

}