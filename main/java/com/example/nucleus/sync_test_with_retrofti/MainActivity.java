package com.example.nucleus.sync_test_with_retrofti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import modelo.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import singleton.Singleton;

public class MainActivity extends AppCompatActivity {


    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);

        registerForContextMenu(listView);


    }


    @Override
    protected void onResume() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(NucleusService.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        NucleusService nucleusService = retrofit.create(NucleusService.class);
        Call<List<User>> requestListUser = nucleusService.getListaUser();

        requestListUser.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Erro de acesso ao servidor", Toast.LENGTH_SHORT).show();
                } else {
                    List<User> users = response.body();
                    listView.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, users));
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erro de conexão com a internet", Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Singleton.getInstance().currentUser = (User) parent.getItemAtPosition(position);
                startActivity(new Intent(MainActivity.this, FormActivity.class));
            }
        });
        super.onResume();
    }
}
