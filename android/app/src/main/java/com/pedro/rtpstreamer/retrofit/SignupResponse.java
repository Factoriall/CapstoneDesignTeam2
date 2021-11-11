package com.pedro.rtpstreamer.retrofit;

import com.google.gson.annotations.SerializedName;

public class SignupResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
