package com.pedro.rtpstreamer.retrofit;

import com.google.gson.annotations.SerializedName;

public class ResultData {
    @SerializedName("userName")
    String userName;

    public ResultData(String userName) {
        this.userName = userName;
    }
}
