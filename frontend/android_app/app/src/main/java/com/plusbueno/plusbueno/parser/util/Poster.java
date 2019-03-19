package com.plusbueno.plusbueno.parser.util;

import android.support.annotation.Nullable;
import android.util.Log;
import com.google.gson.Gson;
import com.plusbueno.plusbueno.parser.exception.AuthorizationException;
import com.plusbueno.plusbueno.parser.exception.NetworkErrorException;
import okhttp3.*;


import java.io.IOException;

/**
 * You can POST anything as a json body to a given url with this ugly class.
 */
public class Poster<T> {
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @Nullable
    public Response post(String url, T data) throws NetworkErrorException {
        String json = new Gson().toJson(data);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).build();
        try{
            return client.newCall(request).execute();
        } catch (IOException e) {
            Log.e("Poster", e.getMessage());
            throw new NetworkErrorException();
        }

    }

    public String postString(String url, T data) throws NetworkErrorException, AuthorizationException {
        Response response = this.post(url, data);
        if (response == null) {
            return null;
        }
        if (response.code() == 401) {
            throw new AuthorizationException();
        }
        if (response.code() / 100 <4) {
            String result = null;
            try {
                if (response.body() != null) {
                    result = response.body().string();
                }
            } catch (IOException e) {
                Log.e("Poster","IOException on parsing response.");
            } catch (NullPointerException e) {
                Log.e("Poster", "Response body is null.");
            }
            return result;

        } else{
            throw new NetworkErrorException();
        }

    }
}
