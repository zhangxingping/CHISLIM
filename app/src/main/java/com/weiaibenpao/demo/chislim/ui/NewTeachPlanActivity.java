package com.weiaibenpao.demo.chislim.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextPaint;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.weiaibenpao.demo.chislim.Interface.GetInterfaceVideoListener;
import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.adater.TeachPlanGifAdapter;
import com.weiaibenpao.demo.chislim.bean.NewTeachGifImageResult;
import com.weiaibenpao.demo.chislim.util.ACache;
import com.weiaibenpao.demo.chislim.util.Default;
import com.weiaibenpao.demo.chislim.util.GetIntentData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewTeachPlanActivity extends Activity {


    GetIntentData intentData;
    Context context;
    ArrayList arrayList;
    Intent intent;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.bgImage)
    ImageView bgImage;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.userHad)
    TextView userHad;
    @BindView(R.id.myLinLayout)
    LinearLayout myLinLayout;
    @BindView(R.id.teachList)
    RecyclerView teachList;
    @BindView(R.id.introTitle)
    TextView introTitle;
    @BindView(R.id.intro)
    TextView intro;
    @BindView(R.id.startTeach)
    RelativeLayout startTeach;
    private ACache mCache;

    TeachPlanGifAdapter mAdapter1;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newteach_plan);
        ButterKnife.bind(this);

        context = this.getApplicationContext();
        mCache = ACache.get(context);

        intentData = new GetIntentData();
        intent = getIntent();

        Picasso.with(context)
                .load(Default.urlPic + intent.getStringExtra("picture"))
                .placeholder(R.mipmap.zhanwei)
                .error(R.mipmap.zhanwei)
                .config(Bitmap.Config.RGB_565)
                .into(bgImage);


        initView();
        initData(intent.getIntExtra("teachid", 1));
    }

    public void initData(int teachid) {
        teachList.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));

        //获取缓存数据
        NewTeachGifImageResult newTeachGif = (NewTeachGifImageResult) mCache.getAsObject("newTeachGifImageResult" + teachid);
        if (newTeachGif != null) {
            arrayList = (ArrayList) newTeachGif.getNewTeachGifImageBean();
            mAdapter1 = new TeachPlanGifAdapter(context, arrayList, 6);
            teachList.setAdapter(mAdapter1);
        }
        intentData.getAllTeachGifIGifImage(context, "getAllTeachGif", teachid);
        intentData.setGetIntentDataListener(new GetInterfaceVideoListener() {
            @Override
            public void getDateList(ArrayList dateList) {
                arrayList = dateList;
                if (dateList == null || dateList.size() == 0) {

                } else {
                    if (mAdapter1 == null) {
                        mAdapter1 = new TeachPlanGifAdapter(context, dateList, 6);
                        teachList.setAdapter(mAdapter1);

                    } else {
                        mAdapter1 = new TeachPlanGifAdapter(context, dateList, 6);
                        mAdapter1.notifyDataSetChanged();
                    }
                }

            }
        });

        TeachPlanGifAdapter.setOnItemClickListener(new TeachPlanGifAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ArrayList list) {

                Intent intent = new Intent(NewTeachPlanActivity.this, NewTeachStartActivity.class);
                //intent.putExtra("dataObject",((NewTeachGifImageResult.NewTeachGifImageBeanBean)list.get(position)));
                intent.putExtra("datalist", list);
                intent.putExtra("position", position);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position, ArrayList list) {

            }
        });
    }

    public void initView() {
        //字体加粗
        title.setText(intent.getStringExtra("teacName"));
        TextPaint tp = title.getPaint();
        tp.setFakeBoldText(true);
        intro.setText(intent.getStringExtra("teacText"));
        userHad.setText(intent.getIntExtra("teacUsehad", 1) + "");

        arrayList = new ArrayList();
    }

    @OnClick({R.id.back,R.id.startTeach})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.startTeach:
                Intent intent = new Intent(NewTeachPlanActivity.this, NewTeachStartActivity.class);
                //intent.putExtra("dataObject",((NewTeachGifImageResult.NewTeachGifImageBeanBean)list.get(position)));
                intent.putExtra("datalist", arrayList);
                intent.putExtra("position", 0);
                startActivity(intent);
                break;
        }
    }
}
