package com.weiaibenpao.demo.chislim.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.weiaibenpao.demo.chislim.Interface.GetInterfaceVideoListener;
import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.adater.RecyclerVideoAdapter;
import com.weiaibenpao.demo.chislim.bean.TeachResult;
import com.weiaibenpao.demo.chislim.util.ACache;
import com.weiaibenpao.demo.chislim.util.GetIntentData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieActivity extends Activity {
    ListView myVideoList;
    GetIntentData getIntentData;
    Context context;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.myVideo)
    RecyclerView myVideo;

    RecyclerVideoAdapter adapter;
    @BindView(R.id.myAllVideo)
    RecyclerView myAllVideo;

    private ACache mCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);
        context = getApplicationContext();
        mCache = ACache.get(context);
        myVideoList = (ListView) findViewById(R.id.myVideoList);

        getIntentData = new GetIntentData();

        //获取缓存数据
        TeachResult teachResult = (TeachResult) mCache.getAsObject("teachResult");
        if(teachResult != null){
            myVideo.setLayoutManager(new GridLayoutManager(context, 3));
            adapter = new RecyclerVideoAdapter(context, (ArrayList) teachResult.getTeach(), 5);
            myVideo.setAdapter(adapter);

            myAllVideo.setLayoutManager(new GridLayoutManager(context, 3));
            adapter = new RecyclerVideoAdapter(context, (ArrayList) teachResult.getTeach(), 5);
            myAllVideo.setAdapter(adapter);
        }

        //获取教程
        getIntentData.getTeach(context);
        getIntentData.setGetIntentDataListener(new GetInterfaceVideoListener() {
            @Override
            public void getDateList(final ArrayList dateList) {
                if(dateList.size() > 0){
                    myVideo.setLayoutManager(new GridLayoutManager(context, 3));
                    adapter = new RecyclerVideoAdapter(context, dateList, 5);
                    myVideo.setAdapter(adapter);

                    myAllVideo.setLayoutManager(new GridLayoutManager(context, 3));
                    adapter = new RecyclerVideoAdapter(context, dateList, 5);
                    myAllVideo.setAdapter(adapter);
                }else{

                }

            }
        });

        RecyclerVideoAdapter.setOnItemClickListener(new RecyclerVideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ArrayList list) {
                Intent intent = new Intent(context, ShowVideoActivity.class);
                intent.putExtra("url", ((TeachResult.TeachBean) (list.get(position))).getTeachAddress());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position, ArrayList list) {

            }
        });
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
