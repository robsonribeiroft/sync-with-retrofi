package com.example.nucleus.sync_test_with_retrofti;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import modelo.User;
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

        final TextView textView = (TextView) findViewById(R.id.textView);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(NucleusService.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        NucleusService nucleusService = retrofit.create(NucleusService.class);

        final Call<List<User>> requestListUser = nucleusService.getListaUser();


        requestListUser.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()){
                    Log.i("Error", "Erro conexão banco");
                } else {
                    List<User> lista = response.body();
                    for (User user : lista){
                        Log.i("Sucess", String.format("%s: %s, %s %s\n\n\n", user.name, user.email, user.cpf, user._id));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.i("Error", "Erro de conexão de internet\n" + t.getMessage());
            }
        });


        Call<User> requestSingleUser = nucleusService.getUser("58b04712d66cb900048562ef");

        requestSingleUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()){
                    Log.i("Error", "Erro conexão banco");
                } else {
                   textView.setText("Olá " + response.body().name);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("Error", "Erro de conexão de internet\n" + t.getMessage());
            }
        });


    }
}
