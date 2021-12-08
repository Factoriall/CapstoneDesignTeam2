package com.pedro.rtpstreamer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pedro.rtpstreamer.retrofit.ResultData;
import com.pedro.rtpstreamer.retrofit.ResultResponse;
import com.pedro.rtpstreamer.retrofit.RetrofitClient;
import com.pedro.rtpstreamer.retrofit.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvScore;
    private Button btMain;
    private ServiceApi service;
    private String userName;
    private int retryCount;
    private final int retryLimitCount = 10;
    private Callback<ResultResponse> callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvScore = findViewById(R.id.tv_score);
        btMain = findViewById(R.id.button_result);
        btMain.setOnClickListener(this);

        service = RetrofitClient.getClient().create(ServiceApi.class);
        userName = getIntent().getStringExtra("userName");

        getResult();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_result:
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("userName", userName);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }

    private void getResult() {
        ProgressDialog progressDialog = new ProgressDialog(ResultActivity.this);
        progressDialog.setMessage("Waiting for the result");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
        retryCount = 0;
        callback = new Callback<ResultResponse>() {
            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                ResultResponse result = response.body();

                if (result.getCode() == 200) {
                    Log.i("Streaming Result", result.getResult());
                    Toast.makeText(ResultActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    tvScore.setText("Score : " + result.getResult());
                    progressDialog.dismiss();
                } else {
                    if (retryCount < retryLimitCount){
                        retryCount++;
                        Log.i("Streaming Retry", Integer.toString(retryCount));
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        retry(call, callback);
                    } else{
                        progressDialog.dismiss();
                        Toast.makeText(ResultActivity.this, "Fail to get result", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultResponse> call, Throwable t) {
                Toast.makeText(ResultActivity.this, "Getting Result Error", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        };
        ResultData data = new ResultData(userName);
        service.userResult(data).enqueue(callback);
    }

    private void retry(Call<ResultResponse> call, Callback<ResultResponse> callback){
        call.clone().enqueue(callback);
    }
}