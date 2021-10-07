package com.example.logintest;

import com.google.gson.annotations.SerializedName;

public class SignupData {
    @SerializedName("userName")
    private String userName;

    @SerializedName("userEmail")
    private String userEmail;

    @SerializedName("userPwd")
    private String userPwd;

    public SignupData(String userName, String userEmail, String userPwd) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPwd = userPwd;
    }
}
