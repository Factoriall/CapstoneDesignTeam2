package com.pedro.rtpstreamer.retrofit;

import com.google.gson.annotations.SerializedName;

public class PassResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("pass")
    private String pass;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getPass() {
        return pass;
    }
}
