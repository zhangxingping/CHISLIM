package com.weiaibenpao.demo.chislim.service;


import com.weiaibenpao.demo.chislim.bean.BooleanResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by lenovo on 2016/8/15.
 */

public interface AddSportService {

    @GET("/Chislim/EveryDaySportServlet")
    Call<BooleanResult> getResult(
            @Query("dowhat") String dowhat,
            @Query("userID") int tn_id,
            @Query("dayTime") String tn_item_name,
            @Query("distance") int tn_item_image,
            @Query("calories") int tn_item_text,
            @Query("sportTime") int sportTime,
            @Query("successes") String successes,
            @Query("project") int project);
}
