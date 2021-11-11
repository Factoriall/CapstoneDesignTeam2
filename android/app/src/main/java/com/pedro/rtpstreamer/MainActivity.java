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

package com.pedro.rtpstreamer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.jaredrummler.android.widget.AnimatedSvgView;
import com.pedro.rtpstreamer.defaultexample.ExampleRtmpActivity;
import com.pedro.rtpstreamer.utils.ActivityLink;

import static android.os.Build.VERSION_CODES.JELLY_BEAN;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;
import static android.os.Build.VERSION_CODES.LOLLIPOP;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private Button btEvaluation;
  private ActivityLink link;
  private AnimatedSvgView svgView;
  private Spinner spinner;
  private ArrayAdapter<String> adapter;
  private String[] items;
  private String selectedPoomsae;

  private final String[] PERMISSIONS = {
      Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA,
      Manifest.permission.WRITE_EXTERNAL_STORAGE
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    overridePendingTransition(R.transition.slide_in, R.transition.slide_out);

    btEvaluation = findViewById(R.id.buttonEvaluation);
    btEvaluation.setOnClickListener(this);

    svgView = (AnimatedSvgView) findViewById(R.id.animated_svg_view2);
    svgView.start();

    items = getResources().getStringArray(R.array.poomsae_array);
    spinner = findViewById(R.id.spinner);
    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);

    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedPoomsae = items[i];
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {

      }
    });

    if (!hasPermissions(this, PERMISSIONS)) {
      ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
    }
  }

  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.buttonEvaluation:
        link = new ActivityLink(new Intent(this, ExampleRtmpActivity.class),
                getString(R.string.default_rtmp), JELLY_BEAN);
        if (hasPermissions(this, PERMISSIONS)) {
          int minSdk = link.getMinSdk();
          if (Build.VERSION.SDK_INT >= minSdk) {
            link.getIntent().putExtra("selectedPoomsae", selectedPoomsae);
            startActivity(link.getIntent());
            overridePendingTransition(R.transition.slide_in, R.transition.slide_out);
          } else {
            showMinSdkError(minSdk);
          }
        } else {
          showPermissionsErrorAndRequest();
        }
        break;
      default:
        break;
    }
  }

  private void showMinSdkError(int minSdk) {
    String named;
    switch (minSdk) {
      case JELLY_BEAN_MR2:
        named = "JELLY_BEAN_MR2";
        break;
      case LOLLIPOP:
        named = "LOLLIPOP";
        break;
      default:
        named = "JELLY_BEAN";
        break;
    }
    Toast.makeText(this, "You need min Android " + named + " (API " + minSdk + " )",
        Toast.LENGTH_SHORT).show();
  }

  private void showPermissionsErrorAndRequest() {
    Toast.makeText(this, "You need permissions before", Toast.LENGTH_SHORT).show();
    ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
  }

  private boolean hasPermissions(Context context, String... permissions) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
      for (String permission : permissions) {
        if (ActivityCompat.checkSelfPermission(context, permission)
            != PackageManager.PERMISSION_GRANTED) {
          return false;
        }
      }
    }
    return true;
  }
}