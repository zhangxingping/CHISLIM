package com.weiaibenpao.demo.chislim.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.base.BaseFragment;
import com.weiaibenpao.demo.chislim.bean.SportTypeBean;
import com.weiaibenpao.demo.chislim.map.activity.DrawTraceActivity;
import com.weiaibenpao.demo.chislim.ui.SportHomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2016/4/22.
 */
public class SportFragment extends BaseFragment {


    @BindView(R.id.homeSport)
    ImageView homeSport;
    @BindView(R.id.outdoorSport)
    ImageView outdoorSport;
    @BindView(R.id.sportHome)
    RelativeLayout sportHome;
    @BindView(R.id.sportOut)
    RelativeLayout sportOut;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sport, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initView(view);
    }

    @Override
    public void initView(View view) {
        // Picasso.with(getActivity()).load("/storage/emulated/0/Demo/ScreenImages/Screen_1.png").into(myLog);
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.homeSport, R.id.outdoorSport,R.id.sportHome, R.id.sportOut})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.homeSport:                                                   //跳转到室内运动
                intent = new Intent(getActivity(), SportHomeActivity.class);
                intent.putExtra("sportTypeBean", new SportTypeBean("", "普通模式", ""));
                startActivity(intent);
                break;
            case R.id.outdoorSport:                                                //跳转到室外运动
                intent = new Intent(getActivity(), DrawTraceActivity.class);
                intent.putExtra("disNum", 0);
                startActivity(intent);
                break;
            case R.id.sportHome:
                intent = new Intent(getActivity(), SportHomeActivity.class);
                intent.putExtra("sportTypeBean", new SportTypeBean("", "普通模式", ""));
                startActivity(intent);
                break;
            case R.id.sportOut:
                intent = new Intent(getActivity(), DrawTraceActivity.class);
                intent.putExtra("disNum", 0);
                startActivity(intent);
                break;
        }
    }
}
