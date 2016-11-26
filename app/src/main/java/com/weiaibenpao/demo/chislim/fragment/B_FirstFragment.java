package com.weiaibenpao.demo.chislim.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.adater.RecyclerFriendAdapter;
import com.weiaibenpao.demo.chislim.base.BaseFragment;
import com.weiaibenpao.demo.chislim.ui.TravelInfoActivity;
import com.weiaibenpao.demo.chislim.util.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.Arrays;


/**
 * Created by Administrator on 2016/4/22.
 */
public class B_FirstFragment extends BaseFragment {
    View view;
    Banner banner;
    String[] images, titles;
    RecyclerView recyclerView;
    private static final String TAG = "RecyclerView";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_find, container, false);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if(parent != null){
            parent.removeView(view);
        }

        images = getResources().getStringArray(R.array.url);
        titles = getResources().getStringArray(R.array.title);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initView(view);
        initBanner(view);
    }

    @Override
    public void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.listFind);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerFriendAdapter mAdapter1 = new RecyclerFriendAdapter(getActivity(),6);
        recyclerView.setAdapter(mAdapter1);

        //点击事件
        RecyclerFriendAdapter.setOnItemClickListener(new RecyclerFriendAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i(TAG,"点击了第" + position + "图片");
                Intent intent = new Intent(getActivity(), TravelInfoActivity.class);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Log.i(TAG,"长按了第" + position + "图片");
            }
        });

    }

    public void initBanner(View view){
        banner = (Banner) view.findViewById(R.id.banner);
        //简单使用
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(Arrays.asList(images));
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(Arrays.asList(titles));
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        banner.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {
                Toast.makeText(getActivity().getApplicationContext(), "点击：" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void initData() {

    }
}
