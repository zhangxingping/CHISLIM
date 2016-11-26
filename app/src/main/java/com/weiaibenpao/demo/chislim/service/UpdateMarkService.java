package com.weiaibenpao.demo.chislim.service;


import com.weiaibenpao.demo.chislim.bean.BooleanResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by lenovo on 2016/8/15.
 */

public interface UpdateMarkService {

    @GET("/Chislim/UserServlet")
    Call<BooleanResult> getResult(
            @Query("dowhat") String dowhat,
            @Query("userID") int userID,
            @Query("userMark") int userMark);
}
