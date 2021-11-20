package com.pedro.rtpstreamer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.pedro.rtpstreamer.retrofit.LoginData;
import com.pedro.rtpstreamer.retrofit.LoginResponse;
import com.pedro.rtpstreamer.retrofit.RetrofitClient;
import com.pedro.rtpstreamer.retrofit.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etEmail;
    private EditText etPassword;
    private Button btLogin;
    private ServiceApi service;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPassword);
        btLogin = findViewById(R.id.buttonLogin);

        btLogin.setOnClickListener(this);

        service = RetrofitClient.getClient().create(ServiceApi.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonLogin:
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                if (email.equals("") || email == null) {
                    Toast.makeText(LoginActivity.this, "You did not enter an email.", Toast.LENGTH_SHORT).show();
                } else if (password.equals("") || password == null) {
                    Toast.makeText(LoginActivity.this, "You did not enter a password.", Toast.LENGTH_SHORT).show();
                } else {
                    attemptLogin(email, password);
                }
                break;
            default:
                break;
        }
    }

    private void attemptLogin(String email, String password) {
        LoginData data = new LoginData(email, password);

        service.userLogin(data).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse result = response.body();
                Toast.makeText(LoginActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();

                if (result.getCode() == 200) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("userName", result.getUserName());
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Login Error", Toast.LENGTH_SHORT).show();
                Log.e("로그인 에러 발생", t.getMessage());
                t.printStackTrace();
            }
        });
    }
}