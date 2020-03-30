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

public class User {
    private long id;

    public long getId() {
        return id;
    }

    public long getUserId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private long companyid;

    public long getCompanyid() {
        return companyid;
    }

    public void setCompanyid(long companyid) {
        this.companyid = companyid;
    }

    private String name;

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String speedunit;

    public String getSpeedunit() { return speedunit; }

    public void setSpeedunit(String speedUnit) { this.speedunit = speedUnit; }

    private double latitude;

    public double getLatitude() { return latitude; }

    public void setLatitude(double latitude) { this.latitude = latitude; }

    private double longitude;

    public double getLongitude() { return longitude; }

    public void setLongitude(double longitude) { this.longitude = longitude; }

    private int zoom;

    public int getZoom() { return zoom; }

    public void setZoom(int zoom) { this.zoom = zoom; }

    private boolean twelveHourFormat;

    public boolean getTwelveHourFormat() { return twelveHourFormat; }

    public void setTwelvehourformat(boolean twelveHourFormat) { this.twelveHourFormat = twelveHourFormat; }
}