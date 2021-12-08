package com.pedro.rtpstreamer.retrofit;

import com.google.gson.annotations.SerializedName;

public class ResultResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("result")
    private String result;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getResult() {
        return result;
    }
}
