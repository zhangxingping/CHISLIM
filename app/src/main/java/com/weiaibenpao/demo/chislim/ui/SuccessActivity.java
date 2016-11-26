package com.weiaibenpao.demo.chislim.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.bean.RegistResult;
import com.weiaibenpao.demo.chislim.bean.UserBean;
import com.weiaibenpao.demo.chislim.model.GetDisModel;
import com.weiaibenpao.demo.chislim.util.Default;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuccessActivity extends AppCompatActivity {

    /**
     * 个人成就界面
     * @param savedInstanceState
     */
    ImageView back;   //返回

    ImageView nowSuccess;
    ImageView dis1;
    ImageView dis2;
    ImageView dis3;
    ImageView dis4;
    ImageView dis5;
    ImageView dis6;

    ArrayList<ImageView> imageList;


    private int dis;   //用户运动总的距离

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        initView();
    }

    public void initView(){

        UserBean user = UserBean.getUserBean();

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        nowSuccess = (ImageView) findViewById(R.id.nowSuccess);
        Picasso.with(SuccessActivity.this)
                .load(R.drawable.dis0)
                .into(nowSuccess);

        dis1 = (ImageView) findViewById(R.id.dis1);
        Picasso.with(SuccessActivity.this)
                .load(R.drawable.dis1)
                .into(dis1);

        dis2 = (ImageView) findViewById(R.id.dis2);
        Picasso.with(SuccessActivity.this)
                .load(R.drawable.dis2)
                .into(dis2);

        dis3 = (ImageView) findViewById(R.id.dis3);
        Picasso.with(SuccessActivity.this)
                .load(R.drawable.dis3)
                .into(dis3);

        dis4 = (ImageView) findViewById(R.id.dis4);
        Picasso.with(SuccessActivity.this)
                .load(R.drawable.dis4)
                .into(dis4);

        dis5 = (ImageView) findViewById(R.id.dis5);
        Picasso.with(SuccessActivity.this)
                .load(R.drawable.dis5)
                .into(dis5);

        dis6 = (ImageView) findViewById(R.id.dis6);
        Picasso.with(SuccessActivity.this)
                .load(R.drawable.dis6)
                .into(dis6);

        getDis(user.userId);
    }

    /**
     * 根据跑步距离跟换图片
     * @param sport  跑步距离
     */
    public void changeImg(int sport){
        imageList = new ArrayList<ImageView>();
        imageList.add(dis1);
        imageList.add(dis2);
        imageList.add(dis3);
        imageList.add(dis4);
        imageList.add(dis5);
        imageList.add(dis6);
        int arg[] = { 3,5,10,21,42,50,100 };
        for (int i = 0; i < arg.length; i++) {

            if(arg[0] > sport){
                Picasso.with(SuccessActivity.this)
                        .load(R.drawable.dis0 + i)
                        .into(nowSuccess);
                break;
            }


            if(arg[i] >= sport){

                Picasso.with(SuccessActivity.this)
                        .load(R.drawable.now0 + i-1)
                        .into(nowSuccess);

                Picasso.with(SuccessActivity.this)
                        .load(R.drawable.dis0 + i)
                        .into((ImageView)imageList.get(i-1));

                break;
            }else if(i == 6 && arg[i] < sport){
                Picasso.with(SuccessActivity.this)
                        .load(R.drawable.now5)
                        .into((ImageView)imageList.get(i-1));

                Picasso.with(SuccessActivity.this)
                        .load(R.drawable.now6)
                        .into(nowSuccess);
            }else{
                Picasso.with(SuccessActivity.this)
                        .load(R.drawable.now0 + i)
                        .into((ImageView)imageList.get(i));
            }

        }
    }

    /**
     * 从服务器获取距离
     */
    public void getDis(int userId){
        Call<RegistResult> call = GetDisModel.getModel().getService().getResult(Default.countDis,userId);

        call.enqueue(new Callback<RegistResult>() {
            @Override
            public void onResponse(Call<RegistResult> call, Response<RegistResult> response) {
                if (response.isSuccessful()) {
                    RegistResult result = response.body();
                    if (result.getNum() != -1) {

                        dis = result.getNum();

                        changeImg(dis);

                    }else {

                    }
                }
            }

            @Override
            public void onFailure(Call<RegistResult> call, Throwable t) {

            }
        });
    }
}
