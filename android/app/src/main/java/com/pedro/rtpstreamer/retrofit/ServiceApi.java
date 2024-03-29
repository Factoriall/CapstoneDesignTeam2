package com.pedro.rtpstreamer.retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceApi {
    @POST("/user/login")
    Call<LoginResponse> userLogin(@Body LoginData data);

    @POST("/user/join")
    Call<SignupResponse> userSignup(@Body SignupData data);

    @POST("/user/result")
    Call<ResultResponse> userResult(@Body ResultData data);

    @POST("/user/pass")
    Call<PassResponse> userPass(@Body PassData data);
}
