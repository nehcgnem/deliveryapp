package com.plusbueno.plusbueno.parser;

import com.google.gson.Gson;
import com.plusbueno.plusbueno.data.Order;
import com.plusbueno.plusbueno.data.Store;
import com.plusbueno.plusbueno.data.User;
import com.plusbueno.plusbueno.parser.exception.AuthorizationException;
import com.plusbueno.plusbueno.parser.exception.NetworkErrorException;
import com.plusbueno.plusbueno.parser.util.HttpUtil;
import com.plusbueno.plusbueno.parser.util.Poster;

import java.util.Date;

/**
 * Created by LZMA on 2018/10/29.
 * Find an example at the main method at the bottom
 */
public class UniversalParser {
    private static Gson gson = new Gson();
    // private static HttpUtil httpUtil = new HttpUtil();

    public static String BASE_URL_RESTFUL;
    public static String BASE_URL_STATIC;

    static {
        BASE_URL_RESTFUL = "https://plusbueno.serveo.net";  // do NOT add slash at the end!
        BASE_URL_STATIC = "http://10.20.28.2:8000";  // do NOT add slash at the end!

    }


    public static <T> T get(String url, Class<T> type) throws NetworkErrorException, AuthorizationException {
        String responseStr = HttpUtil.get(url);
        return gson.fromJson(responseStr, type);
    }

    public static <T> void post(String url, T data) throws NetworkErrorException, AuthorizationException {
        Poster<T> poster = new Poster<>();
        poster.postString(url, data);
    }
    public static <T, U> U post(String url, T body, Class<U> returnType) throws NetworkErrorException, AuthorizationException {
        Poster<T> poster = new Poster<>();
        String response = poster.postString(url, body);
        if (returnType!= null) {
            return gson.fromJson(response, returnType);
        }
        return null;
    }

    public static void delete(String url) throws NetworkErrorException, AuthorizationException {
        String responseStr = HttpUtil.delete(url);
    }

    public static void main(String[] args) {
        try {
//            User[] user = UniversalParser.get(UniversalParser.BASE_URL_RESTFUL+"/logins", User[].class);
//            for (User u : user) {
//                System.out.println(u.getUsername()+u.getEmail());
//            }

            Store store = new Store();
            store.setName("FXXX");

            System.out.println(gson.toJson(store, Store.class));

//            String result = HttpUtil.get(UniversalParser.BASE_URL_RESTFUL + "/orders");
//            Order test[] = gson.fromJson(result, Order[].class);

            //ClassLoader.getSystemClassLoader().loadClass("com.plusbueno.plusbueno.data.Product");
//            Store store[] = UniversalParser.get("http://localhost:3000/store", Store[].class);
//            User newu = new User();
//            newu.setUsername("aaaaaa");
//            UniversalParser.post(UniversalParser.BASE_URL_RESTFUL+"/logins", user);





        } catch (Exception e) {
            e.printStackTrace();
        }

    }




}


