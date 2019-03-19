package com.plusbueno.plusbueno.parser;

import com.plusbueno.plusbueno.data.UserType;

/**
 * Created by LZMA on 2018/10/31.
 */
public class LoginManager {
    private static String sessonKey;
    private static String username;
    private static UserType userType;

    public synchronized static void setSessonKey(String key) {
        sessonKey = key;
    }

    public static String getSessonKey() {
        return sessonKey;
    }

    public static String getUsername() {
        return username;
    }

    public synchronized static void setUsername(String username) {
        LoginManager.username = username;
    }

    public static UserType getUserType() {
        return userType;
    }

    public synchronized static void setUserType(UserType userType) {
        LoginManager.userType = userType;
    }
}
