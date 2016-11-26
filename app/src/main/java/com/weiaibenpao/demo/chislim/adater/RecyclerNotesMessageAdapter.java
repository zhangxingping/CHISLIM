package com.weiaibenpao.demo.chislim.adater;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.bean.NotesMessageResult;

import java.util.ArrayList;

/**
 * 留言适配
 * Created by lenovo on 2016/10/11.
 */

public class RecyclerNotesMessageAdapter extends RecyclerView.Adapter<RecyclerNotesMessageAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private ArrayList messageList;
    private ArrayList reMessage;
    Context context;

    /**
     * 构造器
     * @param context
     */
    public RecyclerNotesMessageAdapter(Context context, ArrayList messageList) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);

        this.messageList = messageList;
    }

    /**
     * item显示类型
     * 引入布局
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_travelmessage, parent, false);
        //view.setBackgroundColor(Color.RED);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * 数据的绑定显示
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        int re_tnid = ((NotesMessageResult.TravelMessageBean)messageList.get(position)).getTra_tm_id();
        if(re_tnid==0){

        }else{
            reMessage.add(re_tnid);
            holder.recyclerReMessage.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
            RecyclerNotesMessageAdapter mAdapter = new RecyclerNotesMessageAdapter(context,reMessage);
            holder.recyclerReMessage.setAdapter(mAdapter);
        }
        holder.userWord.setText(((NotesMessageResult.TravelMessageBean)messageList.get(position)).getUserName());
        holder.wordText.setText(((NotesMessageResult.TravelMessageBean)messageList.get(position)).getTm_text());
        holder.wordTime.setText(((NotesMessageResult.TravelMessageBean)messageList.get(position)).getTm_time());
    }

    /**
     * 要显示的item数目
     * @return
     */
    @Override
    public int getItemCount() {
        return messageList.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userWord;
        public TextView wordText;
        public TextView wordTime;
        public RecyclerView recyclerReMessage;

        public ViewHolder(View view) {
            super(view);
            userWord = (TextView) view.findViewById(R.id.userName);
            wordText = (TextView) view.findViewById(R.id.wordText);
            wordTime = (TextView) view.findViewById(R.id.wordTime);
            recyclerReMessage = (RecyclerView) view.findViewById(R.id.reMessage);
        }
    }
}