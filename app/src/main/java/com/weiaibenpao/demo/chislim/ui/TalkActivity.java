package com.weiaibenpao.demo.chislim.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.adater.MainTalkAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TalkActivity extends Activity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.myImage)
    ImageView myImage;
    @BindView(R.id.myTitle)
    TextView myTitle;
    @BindView(R.id.mtText)
    TextView mtText;
    @BindView(R.id.talkMessage)
    RecyclerView talkMessage;
    @BindView(R.id.talkMessageMore)
    RecyclerView talkMessageMore;

    Context context;
    ArrayList arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);
        ButterKnife.bind(this);

        context = getApplicationContext();

        Intent intent = getIntent();
        arrayList = intent.getParcelableArrayListExtra("talkResult");

        talkMessage.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        MainTalkAdapter mAdapter = new MainTalkAdapter(context, arrayList);                    //话题适配
        talkMessage.setAdapter(mAdapter);



        talkMessageMore.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        MainTalkAdapter mAdapter2 = new MainTalkAdapter(context, arrayList);                    //话题适配
        talkMessageMore.setAdapter(mAdapter);
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
