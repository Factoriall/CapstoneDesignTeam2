package com.example.logintest;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("userId")
    private int userId;

    public String getMessage() {
        return message;
    }

    public int getUserId() {
        return userId;
    }
}
