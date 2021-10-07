package com.example.logintest;

import com.google.gson.annotations.SerializedName;

public class SignupResponse {
    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }
}
