package com.example.gerrys.retrofitdownload;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gerrys.retrofitdownload.Model.ResponseModel;
import com.example.gerrys.retrofitdownload.Model.User;
import com.example.gerrys.retrofitdownload.Util.Preference;
import com.example.gerrys.retrofitdownload.service.Client;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText password = (EditText) findViewById(R.id.input_phone);
        final EditText phone = (EditText) findViewById(R.id.input_password);
        final TextView text =  (TextView) findViewById(R.id.texta) ;
        Button signInButton = (Button) findViewById(R.id.btn_signup);
        Preference.setContext(this);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User(
                        phone.getText().toString(),
                        password.getText().toString());
                okhttp3.logging.HttpLoggingInterceptor interceptor = new okhttp3.logging.HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder().addInterceptor(interceptor);
                okhttpBuilder.addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder newRequest = request.newBuilder()
                                .header("apiKey","xFebVVh3H9ypv9GAy7zg")
                                .header("Content-Type", "application/json");

                        return chain.proceed(newRequest.build()) ;
                    }
                });

                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl("http://mpm.stagingdevbox.com/")
                        .client(okhttpBuilder.build())
                        .addConverterFactory(GsonConverterFactory.create(gson));
                Retrofit retrofit = builder.build();
                Client client =  retrofit.create(Client.class);

                Call<ResponseModel> call = client.createAccount(user);

                call.enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        Toast.makeText(MainActivity.this,"yeah"+response.body().toString(),Toast.LENGTH_LONG).show();
                        text.setText(response.toString());
                        Preference.setToken(response.body().getData().getToken());
                        Preference.setAccount(response.body().getData().getCustomer());
                        Intent intent = new Intent(MainActivity.this,DownloadUpload.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Toast.makeText(MainActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                    }
                });


            }

        });

    }

}
