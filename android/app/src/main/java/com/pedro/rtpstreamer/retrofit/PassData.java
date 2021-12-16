package com.pedro.rtpstreamer.retrofit;

import com.google.gson.annotations.SerializedName;

public class PassData {
    @SerializedName("userName")
    String userName;

    public PassData(String userName) {
        this.userName = userName;
    }
}
