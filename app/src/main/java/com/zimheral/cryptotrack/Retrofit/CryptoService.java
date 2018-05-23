package com.zimheral.cryptotrack.Retrofit;

import com.zimheral.cryptotrack.pojos.CurrencyData;
import com.zimheral.cryptotrack.pojos.CurrencyListData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CryptoService {
        @GET("ticker/{id}")
        Call<CurrencyData> getCurrencyData(@Path("id") String id);

        @GET("listings")
        Call<CurrencyListData> getCurrencyList();
    }

