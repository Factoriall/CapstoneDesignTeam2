/*
 * Copyright (C) 2021 pedroSG94.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pedro.rtpstreamer.defaultexample;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.pedro.encoder.input.video.CameraOpenException;
import com.pedro.rtmp.utils.ConnectCheckerRtmp;
import com.pedro.rtplibrary.rtmp.RtmpCamera1;
import com.pedro.rtpstreamer.LoginActivity;
import com.pedro.rtpstreamer.MainActivity;
import com.pedro.rtpstreamer.R;
import com.pedro.rtpstreamer.ResultActivity;
import com.pedro.rtpstreamer.utils.PathUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * More documentation see:
 * {@link com.pedro.rtplibrary.base.Camera1Base}
 * {@link com.pedro.rtplibrary.rtmp.RtmpCamera1}
 */
public class ExampleRtmpActivity extends AppCompatActivity
    implements ConnectCheckerRtmp, View.OnClickListener, SurfaceHolder.Callback {

  private RtmpCamera1 rtmpCamera1;
  private Button button;

  private String currentDateAndTime = "";
  private File folder;

  private TextView tvCount;
  private CountDownTimer timer;

  private String userName;
  private TextView tvUserName;
  private String selectedPoomsae;
  private TextView tvPoomsae;
  private String url;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    setContentView(R.layout.activity_example);
    folder = PathUtils.getRecordPath(this);
    SurfaceView surfaceView = findViewById(R.id.surfaceView);
    button = findViewById(R.id.b_start_stop);
    button.setOnClickListener(this);
    Button switchCamera = findViewById(R.id.switch_camera);
    switchCamera.setOnClickListener(this);
    rtmpCamera1 = new RtmpCamera1(surfaceView, this);
    rtmpCamera1.setReTries(10);
    surfaceView.getHolder().addCallback(this);

    userName = getIntent().getStringExtra("userName");
    tvUserName = findViewById(R.id.tv_user_name);
    tvUserName.setText(userName);

    selectedPoomsae = getIntent().getStringExtra("selectedPoomsae");
    tvPoomsae = findViewById(R.id.tv_poomsae);
    tvPoomsae.setText(selectedPoomsae);

    url = "rtmp://3.144.208.159:1935/live/" + userName + "+" + selectedPoomsae;

    tvCount = findViewById(R.id.tv_countdown);

    timer = new CountDownTimer(5000, 1000) {
      @Override
      public void onTick(long l) {
        tvCount.setText(String.valueOf(l/1000+1));
      }

      @Override
      public void onFinish() {
        tvCount.setText("");
        Log.i("Streaming Start", String.valueOf(url));
        rtmpCamera1.startStream(url);
      }
    };
  }

  @Override
  public void onConnectionStartedRtmp(String rtmpUrl) {
  }

  @Override
  public void onConnectionSuccessRtmp() {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Toast.makeText(ExampleRtmpActivity.this, "Connection success", Toast.LENGTH_SHORT).show();
      }
    });
  }

  @Override
  public void onConnectionFailedRtmp(final String reason) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        if (rtmpCamera1.reTry(5000, reason)) {
          Toast.makeText(ExampleRtmpActivity.this, "Retry", Toast.LENGTH_SHORT)
              .show();
          Log.e("Streaming Error", reason);
          Log.e("Streaming Error Url", url);
        } else {
          Toast.makeText(ExampleRtmpActivity.this, "Connection failed. " + reason, Toast.LENGTH_SHORT)
              .show();
          rtmpCamera1.stopStream();
          button.setText(R.string.start_button);
        }
      }
    });
  }

  @Override
  public void onNewBitrateRtmp(long bitrate) {

  }

  @Override
  public void onDisconnectRtmp() {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Toast.makeText(ExampleRtmpActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
      }
    });
  }

  @Override
  public void onAuthErrorRtmp() {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Toast.makeText(ExampleRtmpActivity.this, "Auth error", Toast.LENGTH_SHORT).show();
      }
    });
  }

  @Override
  public void onAuthSuccessRtmp() {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Toast.makeText(ExampleRtmpActivity.this, "Auth success", Toast.LENGTH_SHORT).show();
      }
    });
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.b_start_stop:
        if (!rtmpCamera1.isStreaming()) {
          if (rtmpCamera1.isRecording()
              || rtmpCamera1.prepareAudio() && rtmpCamera1.prepareVideo()) {
            timer.start();
            button.setText(R.string.stop_button);
          } else {
            Toast.makeText(this, "Error preparing stream, This device cant do it",
                Toast.LENGTH_SHORT).show();
          }
        } else {
          button.setText(R.string.start_button);
          rtmpCamera1.stopStream();
          Intent intent = new Intent(ExampleRtmpActivity.this, ResultActivity.class);
          intent.putExtra("userName", userName);
          startActivity(intent);
        }
        break;
      case R.id.switch_camera:
        try {
          rtmpCamera1.switchCamera();
        } catch (CameraOpenException e) {
          Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        break;

      default:
        break;
    }
  }

  @Override
  public void surfaceCreated(SurfaceHolder surfaceHolder) {

  }

  @Override
  public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    rtmpCamera1.startPreview();
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    if (rtmpCamera1.isStreaming()) {
      rtmpCamera1.stopStream();
      button.setText(getResources().getString(R.string.start_button));
    }
    rtmpCamera1.stopPreview();
  }
}
