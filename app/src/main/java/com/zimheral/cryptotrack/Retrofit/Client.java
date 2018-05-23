package com.zimheral.cryptotrack.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

    private static String url = "https://api.coinmarketcap.com/v2/";

    private static Retrofit getInstance () {

        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static CryptoService getService(){
        return getInstance().create(CryptoService.class);

    }
}
