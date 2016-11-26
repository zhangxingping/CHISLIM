package com.weiaibenpao.demo.chislim.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.weiaibenpao.demo.chislim.Interface.GetInterfaceVideoListener;
import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.adater.RecyclerTravelMoreAdapter2;
import com.weiaibenpao.demo.chislim.bean.TravelResult;
import com.weiaibenpao.demo.chislim.util.ACache;
import com.weiaibenpao.demo.chislim.util.GetIntentData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 这个是国内更多，国外更多，港澳台查看更多界面
 */

public class TravelMoreActivity extends Activity {

    @BindView(R.id.traMore)
    RecyclerView traMore;

    GetIntentData getIntent;

    String address;
    SwipeRefreshLayout srl;

    //声明一个LinearLayoutManager
    LinearLayoutManager layoutManager;
    RecyclerTravelMoreAdapter2 mAdapter1;
    ArrayList myArrayList;
    Context context;

    int lastVisibleItem;

    int num = 1;

    public boolean flagTop = false;
    public boolean flagLast = false;
    @BindView(R.id.back)
    ImageView back;

    private ACache mCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_more);
        ButterKnife.bind(this);

        context = this;
        mCache = ACache.get(context);
        myArrayList = new ArrayList();
        Intent intent = getIntent();
        address = intent.getStringExtra("address");

        srl = (SwipeRefreshLayout) findViewById(R.id.srl);

        initView();
        initCache();
        initData(num);

        //srl下拉刷新只需要设置setOnRefreshListener就行了
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        num++;
                        flagTop = true;
                        initData(num);
                    }
                }, 2000);
            }
        });

        //rv要使用上啦加载更多。需要在activity中像下面这样做就行了。
        traMore.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter1.getItemCount()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (flagLast = false) {
                                num++;
                            }
                            flagLast = true;
                            initData(num);
                        }
                    }, 1500);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    public void initCache() {
        //获取缓存数据
        TravelResult travelResult = (TravelResult) mCache.getAsObject("travelNotesResult" + address);
        if (travelResult != null) {
            if (mAdapter1 == null) {
                mAdapter1 = new RecyclerTravelMoreAdapter2(TravelMoreActivity.this, (ArrayList) travelResult.getTravel(), 6);
                traMore.setAdapter(mAdapter1);
            }
        }
    }

    public void initData(int i) {
        getIntent = new GetIntentData();
        getIntent.getTravel(context, "getAllTravel", address, i, 6);
        getIntent.setGetIntentDataListener(new GetInterfaceVideoListener() {
            @Override
            public void getDateList(ArrayList dateList) {
                if (num == 1 && dateList.size() > 0) {

                    if (mAdapter1 == null) {
                        mAdapter1 = new RecyclerTravelMoreAdapter2(TravelMoreActivity.this, dateList, 6);
                        traMore.setAdapter(mAdapter1);
                    } else {
                        mAdapter1.changeData(dateList);
                        //mAdapter1.notifyDataSetChanged();
                    }

                } else if (num > 1 && dateList.size() > 0) {
                    if (flagTop) {
                        mAdapter1.refreshItem(dateList);
                        srl.setRefreshing(false);
                        flagTop = false;
                    }

                    if (flagLast) {
                        mAdapter1.addMoreData(dateList);
                        mAdapter1.notifyDataSetChanged();
                        flagLast = false;
                    }
                } else if (dateList.size() == 0) {
                    flagTop = false;
                    flagLast = false;
                    num--;
                    srl.setRefreshing(false);
                }
            }
        });
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void initView() {

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        traMore.setLayoutManager(layoutManager);
        //traMore.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        //点击事件
        RecyclerTravelMoreAdapter2.setOnItemClickListener(new RecyclerTravelMoreAdapter2.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ArrayList list) {
                Intent intent = new Intent(TravelMoreActivity.this, TravelInfoActivity.class);
                intent.putExtra("travel", (Parcelable) list.get(position));
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


/*    public void initSwipeRefresh() {
        //设置下拉刷新旋转进度的背景颜色
        srl.setProgressBackgroundColorSchemeResource(android.R.color.white);
        //旋转圈圈的颜色，渐变色
        srl.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.darker_gray,
                android.R.color.holo_green_light);

        srl.setProgressViewOffset(false, 8, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
    }*/
}
