package com.weiaibenpao.demo.chislim.util;


import com.weiaibenpao.demo.chislim.bean.BooleanResult;
import com.weiaibenpao.demo.chislim.model.UpdateMarkModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lenovo on 2016/9/8.
 */

public class IntentSendData {


    /**
     * 上传用户的积分
     * @param userId     用户ID
     * @param userMark   本次获得积分
     */
    public void updateMark(int userId,int userMark){
        Call<BooleanResult> call = UpdateMarkModel.getModel().getService().getResult(Default.updateMark,userId,userMark);

        call.enqueue(new Callback<BooleanResult>() {
            @Override
            public void onResponse(Call<BooleanResult> call, Response<BooleanResult> response) {
                if (response.isSuccessful()) {
                    BooleanResult result = response.body();
                    if (result.isFlag()) {


                    }else{

                    }
                }
            }

            @Override
            public void onFailure(Call<BooleanResult> call, Throwable t) {

            }
        });
    }
}
