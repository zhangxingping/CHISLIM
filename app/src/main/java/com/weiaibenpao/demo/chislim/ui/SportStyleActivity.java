package com.weiaibenpao.demo.chislim.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.map.activity.DrawTraceActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SportStyleActivity extends Activity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.commonSport)
    TextView commonSport;
    @BindView(R.id.disSport)
    TextView disSport;
    @BindView(R.id.timeSport)
    TextView timeSport;
    @BindView(R.id.picSport)
    TextView picSport;
    @BindView(R.id.stepSport)
    TextView stepSport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_style);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.back, R.id.commonSport, R.id.disSport, R.id.timeSport, R.id.picSport,R.id.stepSport})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.commonSport:
                Intent intent3 = new Intent(SportStyleActivity.this, DrawTraceActivity.class);
                intent3.putExtra("disNum", 0);
                startActivity(intent3);
                finish();
                break;
            case R.id.disSport:
                Intent intent1 = new Intent(SportStyleActivity.this, DisSportStyleActivity.class);
                startActivity(intent1);
                break;
            case R.id.timeSport:
                Intent intent2 = new Intent(SportStyleActivity.this, TimeSportStyleActivity.class);
                startActivity(intent2);
                break;
            case R.id.stepSport:
                Intent intent4 = new Intent(SportStyleActivity.this, StepSportActivity.class);
                startActivity(intent4);
                break;
            case R.id.picSport:
                break;
        }
    }
}
