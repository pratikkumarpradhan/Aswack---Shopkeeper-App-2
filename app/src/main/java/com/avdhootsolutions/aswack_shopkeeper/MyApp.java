package com.avdhootsolutions.aswack_shopkeeper;


import com.avdhootsolutions.aswack_shopkeeper.interfaces.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MyApp {//extends MultiDexApplication {
    private static MyApp instance = null;
    private final API myApi;
    public static String base_URL = "https://admin.aswack.com/api/";

    private MyApp() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://admin.aswack.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(API.class);
    }

    public static synchronized MyApp getInstance() {
        if (instance == null) {
            instance = new MyApp();
        }
        return instance;
    }

    public API getMyApi() {
        return myApi;
    }

}
