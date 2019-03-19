package com.plusbueno.plusbueno.parser;


import android.support.annotation.Nullable;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.plusbueno.plusbueno.data.UserType;
import com.plusbueno.plusbueno.parser.exception.BadRequestException;
import com.plusbueno.plusbueno.parser.exception.LoginFailedException;
import com.plusbueno.plusbueno.parser.exception.NetworkErrorException;
import com.plusbueno.plusbueno.parser.util.DoubleSHA1PasswordHasher;
import com.plusbueno.plusbueno.parser.util.PasswordHasher;
import com.plusbueno.plusbueno.parser.util.Poster;
import okhttp3.Response;
import okhttp3.ResponseBody;


import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LoginParser {
    private static String loginUrl = UniversalParser.BASE_URL_RESTFUL + "/rest-auth/login/";
    private static final String TOKEN_HEADER_PREFIX = "Token ";
    private static final String RESPONSE_KEY_TOKEN = "token";
    public static final String RESPONSE_KEY_USERTYPE = "userType";

    private static Gson gson = new Gson();
    private static Type type = new TypeToken<Map<String, String>>(){}.getType();

    /**
     * Post a login request and return a session key (if successful).
     * @param username you know what it mean
     * @param password you know what it mean
     * @return key if everything goes well, null at internal errors (e.g. wrong response parameter name)
     * @throws LoginFailedException on login failed
     * @throws NetworkErrorException on network error, i.e. status code != 200
     */
    @Nullable
    public static String doLogin(String username, String password) throws LoginFailedException, NetworkErrorException, BadRequestException {
        if(username == null || password == null) {
            Log.e("LoginParser", "Identity or password is null");
            return null;
        }

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("username", username);
        PasswordHasher hasher = new DoubleSHA1PasswordHasher();
        userInfo.put("password", hasher.hash(password));
        Response loginResult = new Poster<Map<String, String>>().post(loginUrl, userInfo);
//        Response loginResult = null;

        if (loginResult == null) {
            throw new NetworkErrorException();
        }
        if (loginResult.code() == 401) {
            throw new LoginFailedException();
        }
        if (loginResult.code() == 400) {
            String message = "Bad request.";
            ResponseBody body = loginResult.body();
            if (body != null) {
                try {
                    message = body.string();
                } catch (IOException e) {
                    throw new NetworkErrorException();
                }
                Map<String, List<String>> badRequestMap = gson.fromJson(message, new TypeToken<Map<String, List<String>> >(){}.getType());
                for(String k : badRequestMap.keySet()) {
                    message = badRequestMap.get(k).get(0);
                }

            }

            throw new BadRequestException(message);

        }



        String resultString;
        try {
            if (loginResult.body() != null) {
                resultString = loginResult.body().string();
            } else {
                return null;
            }
            // System.out.println(resultString);
        } catch (IOException e) {
            Log.e("LoginParser", "IOException " + e.getMessage());
            return null;
        }
        try {
            Log.v("LoginParser", resultString);
            Map<String, String> resultMap = gson.fromJson(resultString, type);
            if (resultMap == null) throw new NetworkErrorException();
            if (resultMap.get(RESPONSE_KEY_TOKEN) == null) {
                throw new LoginFailedException();
            } else {
                LoginManager.setSessonKey(TOKEN_HEADER_PREFIX + resultMap.get(RESPONSE_KEY_TOKEN));
                LoginManager.setUsername(username);
                if (resultMap.get(RESPONSE_KEY_USERTYPE) != null)
                    switch (resultMap.get(RESPONSE_KEY_USERTYPE)) {
                        case "1":
                            LoginManager.setUserType(UserType.CUSTOMER);
                            break;
                        case "2":
                            LoginManager.setUserType(UserType.BUSINESS);
                            break;
                        default:
                            LoginManager.setUserType(UserType.CUSTOMER);
                    }
            }
            return resultMap.get(RESPONSE_KEY_TOKEN);
        } catch (JsonSyntaxException e) {
            throw new NetworkErrorException();
        }
    }

    public static void main(String[] args) {
        try{
            System.out.println(LoginParser.doLogin("s@a.com", "123"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
