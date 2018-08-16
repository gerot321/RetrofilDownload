package com.example.gerrys.retrofitdownload.Model;

import com.google.gson.annotations.SerializedName;

public class Costumer {
    @SerializedName("coreKey")

    private String agentId;
    @SerializedName("name")

    private String name;
    @SerializedName("email")

    private String email;
    @SerializedName("phone")

    private String phone;
    @SerializedName("address")

    private String address;
    @SerializedName("isMarketing")

    private int isMarketing;

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPhone() {
        return phone;
    }

    public int getIsMarketing() {
        return isMarketing;
    }


    public String getAddress() {
        return address;
    }


    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }



    public void setAddress(String address) {
        this.address = address;
    }


    public void setEmail(String email) {
        this.email = email;
    }



    public void setIsMarketing(int isMarketing) {
        this.isMarketing = isMarketing;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }
}