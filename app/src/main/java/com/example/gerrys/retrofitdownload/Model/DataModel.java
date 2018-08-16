package com.example.gerrys.retrofitdownload.Model;

import com.google.gson.annotations.SerializedName;

public class DataModel {
    @SerializedName("token")
    private String token;
    @SerializedName("customer")
    private Costumer customer;

    public Costumer getCustomer() {
        return customer;
    }

    public String getToken() {
        return token;
    }

    public void setCustomer(Costumer customer) {
        this.customer = customer;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
