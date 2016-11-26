package com.weiaibenpao.demo.chislim.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.bean.NewTeachGifImageResult;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class NewTeachStartActivity extends AppCompatActivity {

    @BindView(R.id.myGif)
    SimpleDraweeView myGif;
    @BindView(R.id.fidback)
    ImageView fidback;
    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.myNext)
    TextView myNext;

    ArrayList arrayList;
    int position;
    NewTeachGifImageResult.NewTeachGifImageBeanBean data;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_teach_start);
        ButterKnife.bind(this);
        context = getApplicationContext();
        Intent intent = getIntent();

        arrayList = intent.getParcelableArrayListExtra("datalist");
        position = intent.getIntExtra("position", 0);
        data = intent.getParcelableExtra("dataObject");

        initView(position);
    }

    @OnClick({R.id.fidback, R.id.myNext})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fidback:
                finish();
                break;
            case R.id.myNext:
                position++;
                if(position >= arrayList.size()){

                }else{
                    initView(position);
                }
                break;
        }
    }

    public void initView(int p){

        Glide.with(context)
                .load(((NewTeachGifImageResult.NewTeachGifImageBeanBean) arrayList.get(p)).getGif_Image())
                .asGif() // 判断加载的url资源是否为gif格式的资源
                .skipMemoryCache( true ) //跳过内存缓存
                /**
                 * //  NONE:跳过硬盘缓存 SOURCE：仅仅只缓存原来的全分辨率的图像  RESULT:仅仅缓存最终的图像，即降低分辨率后的（或者是转换后的）
                 * //ALL 缓存所有版本的图像（默认行为）
                 */
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)     //缓存设置
                .priority(Priority.NORMAL)    //优先级  LOW     NORMAL      HIGH      IMMEDIATE
                .placeholder(R.mipmap.zhanwei)    //占位图
                .error(R.mipmap.zhanwei)
                .into(myGif);


        text.setText("           " + ((NewTeachGifImageResult.NewTeachGifImageBeanBean) arrayList.get(p)).getGif_text());
        titleTv.setText(((NewTeachGifImageResult.NewTeachGifImageBeanBean) arrayList.get(p)).getGif_name());
    }
}
