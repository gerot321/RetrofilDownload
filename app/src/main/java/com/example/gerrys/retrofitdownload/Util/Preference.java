package com.example.gerrys.retrofitdownload.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.gerrys.retrofitdownload.Model.Costumer;
import com.google.gson.Gson;

public class Preference {
    private static SharedPreferences sharedPreferencesToken;
    private static String PREF_TOKEN_KEY = "token";
    private static String PREF_ACCOUNT_KEY = "account";


    public static void setContext(Context context) {
        sharedPreferencesToken = context.getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
    }
    public static void setToken(String token) {
        SharedPreferences.Editor editor = sharedPreferencesToken.edit();
        editor.putString(PREF_TOKEN_KEY, token);
        editor.commit();
    }
    public static String getToken() {
        return sharedPreferencesToken.getString(PREF_TOKEN_KEY, "");
    }

    public static void setAccount(Costumer customer) {
        SharedPreferences.Editor editor = sharedPreferencesToken.edit();
        Gson gson = new Gson();
        String json = gson.toJson(customer);
        editor.putString(PREF_ACCOUNT_KEY, json);
        editor.commit();
    }

    public static Costumer getAccount() {
        Gson gson = new Gson();
        String json = sharedPreferencesToken.getString(PREF_ACCOUNT_KEY, "");
        return gson.fromJson(json, Costumer.class);
    }


}
