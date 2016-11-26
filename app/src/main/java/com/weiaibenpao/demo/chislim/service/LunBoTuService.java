package com.weiaibenpao.demo.chislim.service;


import com.weiaibenpao.demo.chislim.bean.LunBoTuBeanResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by lenovo on 2016/8/15.
 */

public interface LunBoTuService {
    @GET("/Chislim/LunBotuServlet")
    Call<LunBoTuBeanResult> getResult(
            @Query("dowhat") String dowhat,
            @Query("tab") int tab,
            @Query("i") int i,
            @Query("n") int n);
}
