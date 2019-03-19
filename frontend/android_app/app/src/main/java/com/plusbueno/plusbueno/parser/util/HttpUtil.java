package com.plusbueno.plusbueno.parser.util;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.plusbueno.plusbueno.parser.LoginManager;
import com.plusbueno.plusbueno.parser.exception.AuthorizationException;
import com.plusbueno.plusbueno.parser.exception.BadRequestException;
import com.plusbueno.plusbueno.parser.exception.NetworkErrorException;
import okhttp3.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


/**
 * Created by LZMA on 2018/10/31.
 * Lower level networking.
 *
 * List of Methods:
 * get(url) will return String
 * getStream(url) will return InputStream
 * post(url, body, bodyType) will return String, body would in json format
 * delete(url) will return String
 *
 * Notice: methods will automatically include token header
 */
public class HttpUtil {
    private static String SESSIONKEY_HEADER_NAME = "authorization";
    private static String HTTP400_MESSAGE_FIELD_NAME = "error";

    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Gson gson = new Gson();
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    //private T object;

//    public String get(String url, Object data, Type type) throws NetworkErrorException {
//        RequestBody body = null;
//
//
//        if (data!= null && type != null){
//            String json = gson.toJson(data, type);
//            body = RequestBody.create(JSON, json);
//        }
//
//        Request request = new Request.Builder().method("GET", body).url(url).build();
//        try {
//            Response response = okHttpClient.newCall(request).execute();
//            if (response.body() != null) {
//                return response.body().string();
//            } else {
//                throw new NetworkErrorException();
//            }
//
//        } catch (IOException e) {
//            throw new NetworkErrorException();
//        }
//
//    }

    public static String get(String url) throws NetworkErrorException, AuthorizationException {
        Request.Builder builder = new Request.Builder().get().url(url);
        String sessionKey = LoginManager.getSessonKey();
        if (sessionKey != null) {
            builder.header(SESSIONKEY_HEADER_NAME, sessionKey);
        }
        Request request = builder.build();

        return execute(request);
    }

    public static InputStream getStream(String url) throws NetworkErrorException {
        Request request = new Request.Builder().get().url(url).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.body() != null) {
                return response.body().byteStream();
            } else {
                throw new NetworkErrorException();
            }

        } catch (IOException e) {
            throw new NetworkErrorException();
        }
    }

    public static  <T> String post(String url, T body, Class<T> bodyType) throws NetworkErrorException, AuthorizationException {
        String json = gson.toJson(body, bodyType);
        RequestBody postBody = RequestBody.create(JSON, json);
        String sessionKey = LoginManager.getSessonKey();
        Request request;
        if (sessionKey == null) {
            request = new Request.Builder().post(postBody).url(url).build();
        } else {
            request = new Request.Builder().post(postBody).url(url).header(SESSIONKEY_HEADER_NAME, sessionKey).build();
        }

        return execute(request);
    }

    public static String delete(String url) throws NetworkErrorException, AuthorizationException {
        Request.Builder builder = new Request.Builder().delete().url(url);
        String sessionKey = LoginManager.getSessonKey();
        if (sessionKey != null) {
            builder.header(SESSIONKEY_HEADER_NAME, sessionKey);
        }
        Request request = builder.build();

        return execute(request);
    }

    public static <T> String put(String url, T body, Class<T> bodyType) throws  NetworkErrorException, AuthorizationException {
        String json = gson.toJson(body, bodyType);
        RequestBody postBody = RequestBody.create(JSON, json);
        String sessionKey = LoginManager.getSessonKey();
        Request request;
        if (sessionKey == null) {
            request = new Request.Builder().post(postBody).url(url).build();
        } else {
            request = new Request.Builder().put(postBody).url(url).header(SESSIONKEY_HEADER_NAME, sessionKey).build();
        }
        return execute(request);
    }

    private static String execute(Request request) throws NetworkErrorException, AuthorizationException {
        try {

            Response response = okHttpClient.newCall(request).execute();


            if (response.code() == 400) {
                String message = "";
                if (response.body() != null) {
                    String value = response.body().string();
                    Log.v("NET_RESPONSE", value);
                    Map<String, String> messageMap = gson.fromJson(value,
                            new TypeToken<Map<String, String>>(){}.getType());
                    message = messageMap.get(HTTP400_MESSAGE_FIELD_NAME) == null ? "" :
                            messageMap.get(HTTP400_MESSAGE_FIELD_NAME);
                }
                throw new BadRequestException(message);
            }
            if (response.code() == 401) {
                throw new AuthorizationException();
            }
            if (response!= null) {
                String r = response.body().string();
                Log.v("HTTP_RESPONSE", r);
                return r;
            } else {
                throw new NetworkErrorException();
            }

        } catch (Exception e) {
            throw new NetworkErrorException();
        }
    }
}