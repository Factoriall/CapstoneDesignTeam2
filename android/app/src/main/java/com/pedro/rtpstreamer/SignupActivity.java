package com.pedro.rtpstreamer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pedro.rtpstreamer.retrofit.SignupData;
import com.pedro.rtpstreamer.retrofit.SignupResponse;
import com.pedro.rtpstreamer.retrofit.RetrofitClient;
import com.pedro.rtpstreamer.retrofit.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etEmail;
    private EditText etName;
    private EditText etPassword;
    private Button btRegister;
    private ServiceApi service;
    private String email;
    private String name;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etEmail = findViewById(R.id.etSignupEmail);
        etName = findViewById(R.id.etSignupName);
        etPassword = findViewById(R.id.etSignupPassword);
        btRegister = findViewById(R.id.buttonRegister);

        btRegister.setOnClickListener(this);

        service = RetrofitClient.getClient().create(ServiceApi.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonRegister:
                email = etEmail.getText().toString();
                name = etName.getText().toString();
                password = etPassword.getText().toString();
                if (email.equals("") || email == null) {
                    Toast.makeText(SignupActivity.this, "You did not enter an email.", Toast.LENGTH_SHORT).show();
                } else if (name.equals("") || name == null) {
                    Toast.makeText(SignupActivity.this, "You did not enter a name.", Toast.LENGTH_SHORT).show();
                } else if (password.equals("") || password == null) {
                    Toast.makeText(SignupActivity.this, "You did not enter a password.", Toast.LENGTH_SHORT).show();
                } else {
                    attemptSignup(email, name, password);
                }
                break;
            default:
                break;
        }
    }

    private void attemptSignup(String email, String name, String password) {
        SignupData data = new SignupData(name, email, password);

        service.userSignup(data).enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                SignupResponse result = response.body();
                Toast.makeText(SignupActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();

                if (result.getCode() == 200) {
                    Intent intent = new Intent(SignupActivity.this, StartActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Toast.makeText(SignupActivity.this, "Sign Up Error", Toast.LENGTH_SHORT).show();
                Log.e("회원가입 에러 발생", t.getMessage());
            }
        });
    }
}