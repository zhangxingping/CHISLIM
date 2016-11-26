package com.weiaibenpao.demo.chislim.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.adater.RecyclerMarkAdapter;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;

public class MarkInfoActivity extends Activity {


    RecyclerView markRecycler;
    ImageView back;
    TextView guize;
    enum Type {
        AlphaIn {
            @Override
            public AnimationAdapter get(Context context) {
                RecyclerMarkAdapter adapter = new RecyclerMarkAdapter(context,15);
                return new AlphaInAnimationAdapter(adapter);
            }
        },
        ScaleIn {
            @Override
            public AnimationAdapter get(Context context) {
                RecyclerMarkAdapter adapter = new RecyclerMarkAdapter(context,15);
                return new ScaleInAnimationAdapter(adapter);
            }
        },
        SlideInBottom {
            @Override
            public AnimationAdapter get(Context context) {
                RecyclerMarkAdapter adapter = new RecyclerMarkAdapter(context,15);
                return new SlideInBottomAnimationAdapter(adapter);
            }
        },
        SlideInLeft {
            @Override
            public AnimationAdapter get(Context context) {
                RecyclerMarkAdapter adapter = new RecyclerMarkAdapter(context,15);
                return new SlideInLeftAnimationAdapter(adapter);
            }
        },
        SlideInRight {
            @Override
            public AnimationAdapter get(Context context) {
                RecyclerMarkAdapter adapter = new RecyclerMarkAdapter(context,15);
                return new SlideInRightAnimationAdapter(adapter);
            }
        };

        public abstract AnimationAdapter get(Context context);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_info);

        markRecycler = (RecyclerView) findViewById(R.id.markRecycler);

        markRecycler.setLayoutManager(new LinearLayoutManager(this));
        AnimationAdapter adapter = Type.values()[2].get(MarkInfoActivity.this);
        adapter.setFirstOnly(false);
        adapter.setDuration(500);
        adapter.setInterpolator(new OvershootInterpolator(.5f));
        markRecycler.setAdapter(adapter);


        /*markRecycler.setLayoutManager(new LinearLayoutManager(this));
        RecyclerFriendAdapter mAdapter1 = new RecyclerFriendAdapter(this,15);
        markRecycler.setAdapter(mAdapter1);*/

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        guize = (TextView) findViewById(R.id.guize);
        guize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MarkInfoActivity.this,MarkActivity.class);
                startActivity(intent);
            }
        });
    }
}
