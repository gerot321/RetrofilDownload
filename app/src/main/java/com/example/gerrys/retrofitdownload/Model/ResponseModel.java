package com.example.gerrys.retrofitdownload.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseModel {

        @SerializedName("Status")
        @Expose
        private String Status;
        @SerializedName("data")
        @Expose

        private Data data;

        public Data getData() {
            return data;
        }

        public static class Data{
            @SerializedName("token")
            @Expose
            private String token;
            @SerializedName("customer")
            @Expose
            private Costumer customer;

            public String getToken() {
                return token;
            }

            public Costumer getCustomer() {
                return customer;
            }
        }
}


