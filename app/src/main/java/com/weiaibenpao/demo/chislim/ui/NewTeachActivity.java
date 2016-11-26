package com.weiaibenpao.demo.chislim.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.weiaibenpao.demo.chislim.Interface.GetInterfaceVideoListener;
import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.adater.TeachListAdapter2;
import com.weiaibenpao.demo.chislim.bean.NewTeachResult;
import com.weiaibenpao.demo.chislim.util.ACache;
import com.weiaibenpao.demo.chislim.util.GetIntentData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewTeachActivity extends Activity {


    @BindView(R.id.teachRecycler)
    RecyclerView teachRecycler;
    @BindView(R.id.sFresh)
    SwipeRefreshLayout sFresh;
    @BindView(R.id.myBar)
    View myBar;
    @BindView(R.id.back)
    ImageView back;
    private ACache mCache;
    ArrayList bunnerList;

    String[] images, titles;
    GetIntentData intentData;
    Context context;

    TeachListAdapter2 mAdapter;
    int lastVisibleItem;
    LinearLayoutManager mLayoutManager;
    int num = 1;
    public boolean flagTop = false;
    public boolean flagLast = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_teach);
        ButterKnife.bind(this);

        context = this;
        mCache = ACache.get(context);
        bunnerList = new ArrayList();
        intentData = new GetIntentData();

        Intent intent = getIntent();
        initView(intent.getIntExtra("position",1));
        initGetData();
        initTeach();


        sFresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (flagTop = false) {
                            num++;
                            intentData.getAllTeach(context, "getAllTeach", num, 10);
                        }
                        flagTop = true;
                        //initGetData(num, 5);
                    }
                }, 2000);
            }
        });
        //rv要使用上啦加载更多。需要在activity中像下面这样做就行了。
        teachRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (flagLast = false) {
                                num++;
                                intentData.getAllTeach(context, "getAllTeach", num, 10);
                            }
                            flagLast = true;
                        }
                    }, 1500);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
    }


    public void initView(final int n) {
        initClassCatch(n);
        intentData.getAllTeachTab2(context, "getAllTeachTab2", n);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        teachRecycler.setLayoutManager(mLayoutManager);
       //teachRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
    }

    //获取不同种类缓存数据
    public void initClassCatch(int position) {
        //获取缓存数据
        NewTeachResult newTeachResult = (NewTeachResult) mCache.getAsObject("newTeachResult" + position);
        if (newTeachResult != null && mAdapter == null) {
            mAdapter = new TeachListAdapter2(context, (ArrayList) newTeachResult.getNewTeachBean());
            teachRecycler.setAdapter(mAdapter);
        } else if (newTeachResult != null && mAdapter != null) {
            mAdapter.changeData((ArrayList) newTeachResult.getNewTeachBean());
            mAdapter.notifyDataSetChanged();
        }
    }

    //请求网络返回数据
    public void initGetData() {
        intentData.setGetIntentDataListener(new GetInterfaceVideoListener() {
            @Override
            public void getDateList(ArrayList dateList) {

                if (num == 1 && dateList.size() > 0) {
                    if (mAdapter == null) {
                        mAdapter = new TeachListAdapter2(context, dateList);
                        teachRecycler.setAdapter(mAdapter);
                    } else {
                        mAdapter.changeData(dateList);
                        mAdapter.notifyDataSetChanged();
                    }
                } else if (num > 1 && dateList.size() > 0) {
                    if (flagTop) {
                        mAdapter.refreshItem(dateList);
                        sFresh.setRefreshing(false);
                        flagTop = false;
                    }
                    if (flagLast) {
                        mAdapter.addMoreData(dateList);
                        flagLast = false;
                    }
                } else if (dateList.size() == 0) {
                    flagTop = false;
                    flagLast = false;
                    num--;
                    sFresh.setRefreshing(false);
                }
            }
        });
    }

    /**
     * 内容布局
     */
    public void initTeach() {
        TeachListAdapter2.setOnItemClickListener(new TeachListAdapter2.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ArrayList list) {
                Intent intent = new Intent(NewTeachActivity.this, NewTeachPlanActivity.class);
                intent.putExtra("picture", ((NewTeachResult.NewTeachBeanBean) list.get(position)).getTeach_image());
                intent.putExtra("teachid", ((NewTeachResult.NewTeachBeanBean) list.get(position)).getTeach_id());
                intent.putExtra("teacName", ((NewTeachResult.NewTeachBeanBean) list.get(position)).getTeachName());
                intent.putExtra("teacUsehad", ((NewTeachResult.NewTeachBeanBean) list.get(position)).getTeach_userhad());
                intent.putExtra("teacText", ((NewTeachResult.NewTeachBeanBean) list.get(position)).getTeach_text());

                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position, ArrayList list) {

            }
        });
    }

    @OnClick({R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
}
