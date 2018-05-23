package com.zimheral.cryptotrack.pojos;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrencyListData {

    @SerializedName("data")
    @Expose
    private List<CurrencyList> data = null;

    public List<CurrencyList> getData() {
        return data;
    }
    public void setData(List<CurrencyList> data) {
        this.data = data;
    }
}

