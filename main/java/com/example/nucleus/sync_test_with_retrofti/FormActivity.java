package com.example.nucleus.sync_test_with_retrofti;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import modelo.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import singleton.Singleton;



public class FormActivity extends AppCompatActivity{

    private EditText editName;
    private EditText editphone;
    private EditText editEmail;
    private EditText editCpf;
    private static final int ERROR_CPF_CADASTRADO = 2300001;
    private static final int ERROR_CPF_INVALIDO = 2300002;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(NucleusService.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        final NucleusService nucleusService = retrofit.create(NucleusService.class);

        editName = (EditText) findViewById(R.id.name);
        editCpf = (EditText) findViewById(R.id.cpf);
        editEmail = (EditText) findViewById(R.id.email);
        editphone = (EditText) findViewById(R.id.phone);
        Button btnInserir = (Button) findViewById(R.id.button_inserir);
        Button btnDeletar = (Button) findViewById(R.id.button_deletar);

        editCpf.setText(Singleton.getInstance().currentUser.getCpf());
        editName.setText(Singleton.getInstance().currentUser.getName());
        editEmail.setText(Singleton.getInstance().currentUser.getEmail());
        editphone.setText(Singleton.getInstance().currentUser.getPhone());

        btnInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User tmpUser = new User();
                tmpUser.setName(editName.getText().toString());
                tmpUser.setCpf(editCpf.getText().toString());
                tmpUser.setEmail(editEmail.getText().toString());
                tmpUser.setPhone(editphone.getText().toString());

                Call<User> postUser = nucleusService.postSingleUser(tmpUser);

                postUser.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (!response.isSuccessful()){
                            if (response.code() == ERROR_CPF_INVALIDO){
                                Toast.makeText(FormActivity.this, "CPF inválido", Toast.LENGTH_SHORT).show();
                            }
                            else if(response.code() == ERROR_CPF_CADASTRADO){
                                Toast.makeText(FormActivity.this, "CPF já cadastrado", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(FormActivity.this, "Erro de conxão com o banco", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(FormActivity.this, "Usuário cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(FormActivity.this, "Erro de conexão à internet", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<User> deleteUser = nucleusService.deleteSingleUser(Singleton.getInstance().currentUser.get_id());
                deleteUser.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Toast.makeText(FormActivity.this, "Usuário removido com sucesso", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(FormActivity.this, "Erro conexão com internet", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
