package com.weiaibenpao.demo.chislim.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.adater.RecyclerHisAdapter;

public class MyHistoryActivity extends Activity {

    RecyclerView hisRecycler;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_history);

        hisRecycler = (RecyclerView) findViewById(R.id.hisRecycler);
        hisRecycler.setLayoutManager(new LinearLayoutManager(this));
        RecyclerHisAdapter mAdapter1 = new RecyclerHisAdapter(this,5);
        hisRecycler.setAdapter(mAdapter1);

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
