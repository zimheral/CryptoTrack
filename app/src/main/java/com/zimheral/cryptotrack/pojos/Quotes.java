package com.zimheral.cryptotrack.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Quotes implements Serializable {
    @SerializedName("USD")
    @Expose
    USD uSD;

    public Quotes() {
    }

    public USD getUSD() {
        return uSD;
    }

    public void setUSD(USD uSD) {
        this.uSD = uSD;
    }
}