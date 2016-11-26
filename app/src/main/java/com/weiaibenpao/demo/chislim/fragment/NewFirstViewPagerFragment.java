package com.weiaibenpao.demo.chislim.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.weiaibenpao.demo.chislim.Interface.GetInterfaceVideoListener;
import com.weiaibenpao.demo.chislim.Interface.GetObjectListener;
import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.adater.ActivityHealthAdapter;
import com.weiaibenpao.demo.chislim.adater.ActivityMainAdapter;
import com.weiaibenpao.demo.chislim.adater.MainTalkAdapter;
import com.weiaibenpao.demo.chislim.adater.RecyclerTeachAdapter;
import com.weiaibenpao.demo.chislim.base.BaseFragment;
import com.weiaibenpao.demo.chislim.bean.LunBoTuBeanResult;
import com.weiaibenpao.demo.chislim.bean.MainBeanResult;
import com.weiaibenpao.demo.chislim.ui.MovieActivity;
import com.weiaibenpao.demo.chislim.ui.MusicListActivity;
import com.weiaibenpao.demo.chislim.ui.NewTeachActivity;
import com.weiaibenpao.demo.chislim.ui.TalkActivity;
import com.weiaibenpao.demo.chislim.util.ACache;
import com.weiaibenpao.demo.chislim.util.Default;
import com.weiaibenpao.demo.chislim.util.GetIntentData;
import com.weiaibenpao.demo.chislim.util.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Administrator on 2016/4/25.
 */
public class NewFirstViewPagerFragment extends BaseFragment {


    View view;

    GetIntentData getIntentData;
    Context context;
    String[] images, titles;
    Banner banner;

    RecyclerView recyclerView;

    ArrayList bunnerList;
    GetIntentData intentData;

    RecyclerView talkView;
    RecyclerView activityView;

    private ACache mCache;

    MainTalkAdapter mAdapter;
    ActivityMainAdapter activityMainAdapter;
    ActivityHealthAdapter activityHealthAdapter;
    MainBeanResult mainResult;

    LinearLayout talkLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.main_layout, container, false);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        context = getActivity().getApplicationContext();
        mCache = ACache.get(context);

        images = getResources().getStringArray(R.array.url);
        titles = getResources().getStringArray(R.array.title);

        intentData = new GetIntentData();
        bunnerList = new ArrayList();

        getIntentData = new GetIntentData();

        initView(view);
        initData();
        initLunBoData(1, 1, 5);
        initTalkActivityData();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initView(view);
    }

    //活动和话题
    public void initTalkActivityData() {
        talkView = (RecyclerView) view.findViewById(R.id.talkView);                     //话题
        activityView = (RecyclerView) view.findViewById(R.id.activityView);            //活动

        talkView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        activityView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        //获取缓存数据
        mainResult = (MainBeanResult) mCache.getAsObject("mainData");

        if (mainResult != null) {
            mAdapter = new MainTalkAdapter(context, (ArrayList) mainResult.getTalk());                    //话题适配
            activityMainAdapter = new ActivityMainAdapter(context, (ArrayList) mainResult.getActivity());          //活动适配

            talkView.setAdapter(mAdapter);
            activityView.setAdapter(activityMainAdapter);
        }
        MainTalkAdapter.setOnItemClickListener(new MainTalkAdapter.OnItemClickListener() {         //话题图片点击触发监听
            @Override
            public void onItemClick(View view, int position, ArrayList talkList) {
                Toast.makeText(context,"11111111111",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position, ArrayList talkList) {

            }
        });
        //点击进入话题列表
        talkLayout = (LinearLayout) view.findViewById(R.id.talkLayout);
        talkLayout.setOnClickListener(new View.OnClickListener() {                      //话题上边的空白区域点击触发
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TalkActivity.class);
                intent.putParcelableArrayListExtra("talkResult",(ArrayList) mainResult.getTalk());
                startActivity(intent);
            }
        });

        //获取首页数据
        getIntentData.GetMainData(context, "getActivity", 1, 5, 1, 5);
        getIntentData.setGetObjectListener(new GetObjectListener() {
            @Override
            public void getObject(Object object) {
                mainResult = (MainBeanResult) object;
                if (mAdapter != null) {
                    mAdapter.changeData((ArrayList) mainResult.getTalk());
                    activityMainAdapter.changeData((ArrayList) mainResult.getActivity());
                } else {
                    mAdapter = new MainTalkAdapter(context, (ArrayList) mainResult.getTalk());
                    activityMainAdapter = new ActivityMainAdapter(context, (ArrayList) mainResult.getActivity());
                    talkView.setAdapter(mAdapter);
                    activityView.setAdapter(activityMainAdapter);
                }
            }
        });
    }

    @Override
    public void initView(View view) {
        //音乐和电影
        recyclerView = (RecyclerView) view.findViewById(R.id.difClass);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 2));
        RecyclerTeachAdapter mAdapter1 = new RecyclerTeachAdapter(getActivity(), Default.MenuList(), 6);
        recyclerView.setAdapter(mAdapter1);

        //点击事件
        RecyclerTeachAdapter.setOnItemClickListener(new RecyclerTeachAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        Intent intent0 = new Intent(context, MusicListActivity.class);
                        startActivity(intent0);
                        break;
                    case 1:
                        Intent intent1 = new Intent(context, MovieActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(context, NewTeachActivity.class);
                        startActivity(intent2);
                        break;
                }
                /*Intent intent = new Intent(context,ShowVideoActivity.class);
                intent.putExtra("url","http://www.56.com/u83/v_ODg4MDg4ODA.html#fromoutpvid=ODg4MDg4ODA");
                startActivity(intent);*/
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        //健身模块布局
        RecyclerView healthTeach = (RecyclerView) view.findViewById(R.id.healthTeach);
        healthTeach.setLayoutManager(new GridLayoutManager(context, 2));               //健身布局
        activityHealthAdapter = new ActivityHealthAdapter(context, Default.getHealthList());
        healthTeach.setAdapter(activityHealthAdapter);
        activityHealthAdapter.setOnItemClickListener(new ActivityHealthAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ArrayList list) {
                Intent intent = new Intent(context, NewTeachActivity.class);
                intent.putExtra("position",position+1);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position, ArrayList list) {

            }
        });

        ImageView myImage = (ImageView) view.findViewById(R.id.myImage);            //首页中间大图
        Picasso.with(context)
                .load("http://ofplk6att.bkt.clouddn.com/home_import_class.png")
                .placeholder(R.mipmap.noshow)
                .error(R.mipmap.zhanwei)
                .config(Bitmap.Config.RGB_565)
                .into(myImage);
    }

    /**
     * 轮播图
     * @param tab
     * @param i
     * @param n
     */
    public void initLunBoData(int tab, int i, int n) {

        //缓存
        LunBoTuBeanResult lunBoTuBeanResult = (LunBoTuBeanResult) mCache.getAsObject("lunBoTuBeanResultHappy");
        if (lunBoTuBeanResult != null) {
            int num = lunBoTuBeanResult.getLunboTuBean().size();
            for (int j = 0; j < num; j++) {
                bunnerList.add((lunBoTuBeanResult.getLunboTuBean().get(j)).getPic_url());
            }
            initBanner(view, bunnerList);
        }

        intentData.getLunBoTu(context, "getAllLunBo", tab, i, n);
        intentData.setGetIntentDataListener(new GetInterfaceVideoListener() {
            @Override
            public void getDateList(ArrayList dateList) {
                if (dateList.size() > 0) {
                    int num = dateList.size();
                    for (int i = 0; i < num; i++) {
                        bunnerList.add(((LunBoTuBeanResult.LunboTuBeanBean) dateList.get(i)).getPic_url());
                    }
                    initBanner(view, bunnerList);
                }
            }
        });
    }

    public void initBanner(View view, ArrayList image) {
        banner = (Banner) view.findViewById(R.id.banner);
        //简单使用
        //设置banner样式
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(image);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(Arrays.asList(titles));
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(6000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        banner.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {
                //Toast.makeText(getActivity().getApplicationContext(), "点击：" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
