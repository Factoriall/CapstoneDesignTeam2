package com.pedro.rtpstreamer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jaredrummler.android.widget.AnimatedSvgView;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btLogin;
    private Button btSignup;
    private AnimatedSvgView svgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btLogin = findViewById(R.id.buttonStartLogin);
        btSignup = findViewById(R.id.buttonStartSignup);

        btLogin.setOnClickListener(this);
        btSignup.setOnClickListener(this);

        svgView = (AnimatedSvgView) findViewById(R.id.animated_svg_view);
        svgView.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonStartLogin:
                //attemptLogin();
                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonStartSignup:
                Intent intent2 = new Intent(StartActivity.this, SignupActivity.class);
                startActivity(intent2);
            default:
                break;
        }
    }
}