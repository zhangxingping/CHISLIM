package com.weiaibenpao.demo.chislim.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.weiaibenpao.demo.chislim.Interface.GetInterfaceVideoListener;
import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.adater.RecyclerTravelNotesAdapter2;
import com.weiaibenpao.demo.chislim.base.BaseFragment;
import com.weiaibenpao.demo.chislim.bean.TravelNotesResult;
import com.weiaibenpao.demo.chislim.ui.MessageActivity;
import com.weiaibenpao.demo.chislim.ui.NotesUserItemActivity;
import com.weiaibenpao.demo.chislim.util.ACache;
import com.weiaibenpao.demo.chislim.util.GetIntentData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/22.
 */
public class C_SecondFragment extends BaseFragment {


    @BindView(R.id.travelNotes)
    RecyclerView travelNotes;

    Context context;

    GetIntentData getIntentDataNotes;
    ArrayList travelNotesList;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    LinearLayoutManager mLayoutManager;
    int lastVisibleItem;
    RecyclerTravelNotesAdapter2 mAdapter1;

    int p = 1;
    boolean flagTop = false;
    boolean flagLast = false;

    private ACache mCache;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_travel2, null);
        ButterKnife.bind(this, view);
        context = getActivity().getApplicationContext();

        mCache = ACache.get(context);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initCache();
        initView(view);
        initData(p, 5);

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(flagTop == false){
                            p++;
                        }
                        flagTop=true;
                        initData(p, 5);

                    }
                }, 2000);
            }
        });
        //rv要使用上啦加载更多。需要在activity中像下面这样做就行了。
        travelNotes.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter1.getItemCount()){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(flagLast == false){
                                p++;
                            }
                            flagLast=true;
                            initData(p, 5);
                        }
                    } , 1500);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    @Override
    public void initView(View view) {
        mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        travelNotes.setLayoutManager(mLayoutManager);
        //travelNotes.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        RecyclerTravelNotesAdapter2.setOnItemClickListener(new RecyclerTravelNotesAdapter2.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ArrayList list) {
                Intent intent = new Intent(context, NotesUserItemActivity.class);
                intent.putExtra("position",((TravelNotesResult.TravelNotesBean)list.get(position)).getTn_id());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position, ArrayList list) {

            }

            @Override
            public void onMessageClick(View view, int tn_id) {
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                intent.putExtra("tn_id",tn_id);
                startActivity(intent);
            }
        });
    }

    public void initCache(){
        //获取缓存数据
        TravelNotesResult notesResult = (TravelNotesResult) mCache.getAsObject("travelNotesResult");
        if(notesResult != null){
            if(mAdapter1 == null){
                mAdapter1 = new RecyclerTravelNotesAdapter2(context, (ArrayList) notesResult.getTravel_notes());
                travelNotes.setAdapter(mAdapter1);
            }else if(mAdapter1 != null){
                mAdapter1 = new RecyclerTravelNotesAdapter2(context, (ArrayList) notesResult.getTravel_notes());
                travelNotes.setAdapter(mAdapter1);
            }
        }
    }

    public void initData(int i, int num) {
        getIntentDataNotes = new GetIntentData();
        travelNotesList = new ArrayList();

        getIntentDataNotes.getTravelNotes(context, "getAllNotes", i, num);
        getIntentDataNotes.setGetIntentDataListener(new GetInterfaceVideoListener() {
            @Override
            public void getDateList(ArrayList dateList) {
                if(p == 1 && dateList.size() > 0){

                    if(mAdapter1 == null){
                        mAdapter1 = new RecyclerTravelNotesAdapter2(context, dateList);
                        travelNotes.setAdapter(mAdapter1);
                    }else{
                        mAdapter1.changeData(dateList);
                        mAdapter1.notifyDataSetChanged();
                    }

                }else if( p > 1 && dateList.size() > 0) {
                    if(flagTop){
                        //这里主要就是获取刷新的数据，传给adapter
                        mAdapter1.refreshItem(dateList);
                        srl.setRefreshing(false);
                        flagTop = false;
                    }
                    if(flagLast){
                        mAdapter1.addMoreData(dateList);
                        mAdapter1.notifyDataSetChanged();
                        flagLast = false;
                    }
                }else if(dateList.size() == 0){
                    Toast.makeText(context,"检查网络连接...",Toast.LENGTH_SHORT).show();
                    flagTop = false;
                    flagLast = false;
                    p--;
                    srl.setRefreshing(false);
                }
            }
        });
    }
}