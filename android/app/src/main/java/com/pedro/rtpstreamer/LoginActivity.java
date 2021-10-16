package com.pedro.rtpstreamer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etEmail;
    private EditText etPassword;
    private Button btLogin;
    private Button btSignup;
    //private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPassword);
        btLogin = findViewById(R.id.buttonLogin);

        btLogin.setOnClickListener(this);

        //service = RetrofitClient.getClient().create(ServiceApi.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonLogin:
                //attemptLogin();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    /*
    private void attemptLogin() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        LoginData data = new LoginData(email, password);

        service.userLogin(data).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse result = response.body();
                Toast.makeText(MainActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Login Error", Toast.LENGTH_SHORT).show();
                Log.e("로그인 에러 발생", t.getMessage());
            }
        });
    }
     */
}