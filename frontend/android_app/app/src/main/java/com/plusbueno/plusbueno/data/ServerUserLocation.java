package com.plusbueno.plusbueno.data;

/**
 * Created by LZMA on 2018/12/2.
 */
public class ServerUserLocation {
    private String userCoord;

    public ServerUserLocation(String userCoord) {
        this.userCoord = userCoord;
    }

    public ServerUserLocation() {
    }

    public String getUserCoord() {
        return userCoord;
    }

    public void setUserCoord(String userCoord) {
        this.userCoord = userCoord;
    }
}
