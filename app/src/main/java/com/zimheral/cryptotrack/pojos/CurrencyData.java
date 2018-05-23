package com.zimheral.cryptotrack.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CurrencyData {

    @SerializedName("data")
    @Expose
    private Currency data = new Currency();

    public Currency getData() {
        return data;
    }

    public void setData(Currency data) {
        this.data = data;
    }

}

