package com.pedro.rtpstreamer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etEmail;
    private EditText etName;
    private EditText etPassword;
    private Button btRegister;
    //private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etEmail = findViewById(R.id.etSignupEmail);
        etName = findViewById(R.id.etSignupName);
        etPassword = findViewById(R.id.etSignupPassword);
        btRegister = findViewById(R.id.buttonRegister);

        btRegister.setOnClickListener(this);

        //service = RetrofitClient.getClient().create(ServiceApi.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonRegister:
                //attemptSignup();
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
/*
    private void attemptSignup() {
        String email = etEmail.getText().toString();
        String name = etName.getText().toString();
        String password = etPassword.getText().toString();

        SignupData data = new SignupData(name, email, password);

        service.userSignup(data).enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                SignupResponse result = response.body();
                Toast.makeText(SignupActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Toast.makeText(SignupActivity.this, "Sign Up Error", Toast.LENGTH_SHORT).show();
                Log.e("회원가입 에러 발생", t.getMessage());
            }
        });
    }
    @Override
 */
}