package com.plusbueno.plusbueno.data;

/**
 * Created by LZMA on 2018/11/27.
 */
public class LoginInfo {
    private String token;
    private int userType = 1;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
