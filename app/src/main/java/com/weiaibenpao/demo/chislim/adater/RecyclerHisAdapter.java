package com.weiaibenpao.demo.chislim.adater;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.util.MyDecoration;


/**
 * Created by lenovo on 2016/10/11.
 */

public class RecyclerHisAdapter extends RecyclerView.Adapter<RecyclerHisAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private int num;
    Context context;

    private static OnItemClickListener mOnItemClickListener;
    //设置点击和长按事件

    public interface OnItemClickListener {
        void onItemClick(View view, int position); //设置点击事件
        void onItemLongClick(View view, int position); //设置长按事件
    }
    public static void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    /**
     * 构造器
     * @param context
     * @param num
     */
    public RecyclerHisAdapter(Context context, int num) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.num = num;
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
        View view = mInflater.inflate(R.layout.item_history, parent, false);

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
        //绑定事件
        if (mOnItemClickListener != null) {
            holder.item_myMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(holder.item_myMonth, position);
                }
            });
            holder.item_myMonth.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemClickListener.onItemLongClick(holder.itemView, position);
                    return false;
                }
            });
        }
        holder.item_myMonth.setText("10月跑步记录");
        holder.hisIntoRecycle.setLayoutManager(new LinearLayoutManager(context));
        RecyclerHisIntoAdapter mAdapter1 = new RecyclerHisIntoAdapter(context,6);

        holder.hisIntoRecycle.setAdapter(mAdapter1);
        //这句就是添加我们自定义的分隔线
        holder.hisIntoRecycle.addItemDecoration(new MyDecoration(context, MyDecoration.VERTICAL_LIST));
    }

    /**
     * 要显示的item数目
     * @return
     */
    @Override
    public int getItemCount() {
        return num;
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView item_myMonth;
        public RecyclerView hisIntoRecycle;

        public ViewHolder(View view) {
            super(view);
            item_myMonth = (TextView) view.findViewById(R.id.myMonth);
            hisIntoRecycle = (RecyclerView) view.findViewById(R.id.hisIntoRecycler);
        }
    }
}