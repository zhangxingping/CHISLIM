package com.weiaibenpao.demo.chislim.adater;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.weiaibenpao.demo.chislim.Interface.GetInterfaceBooleanListener;
import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.bean.TravelNotesResult;
import com.weiaibenpao.demo.chislim.bean.UserBean;
import com.weiaibenpao.demo.chislim.ui.TravelPictureActivity;
import com.weiaibenpao.demo.chislim.util.Default;
import com.weiaibenpao.demo.chislim.util.GetIntentData;

import java.util.ArrayList;

/**
 * 游记界面适配器
 * Created by lenovo on 2016/10/11.
 */

public class RecyclerTravelNotesAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView

    //上拉加载更多状态-默认为0
    private int load_more_status = 0;

    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE = 1;


    private LayoutInflater mInflater;
    private ArrayList travelList;
    Context context;
    GetIntentData getIntent = new GetIntentData();

    private static OnItemClickListener mOnItemClickListener;
    //设置点击和长按事件

    public interface OnItemClickListener {
        void onItemClick(View view, int position, ArrayList list); //设置点击事件
        void onItemLongClick(View view, int position, ArrayList list); //设置长按事件
        void onMessageClick(View view, int tn_id);
    }
    public static void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    /**
     * 构造器
     * @param context
     */
    public RecyclerTravelNotesAdapter2(Context context, ArrayList travelNotesList) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);

        travelList = travelNotesList;
    }

    /**
     * item显示类型
     * 引入布局
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_ITEM) {
            View view = mInflater.inflate(R.layout.item_travelnote, parent, false);
            //view.setBackgroundColor(Color.RED);
            ItemViewHolder viewHolder = new ItemViewHolder(view);
            return viewHolder;

        } else if (viewType == TYPE_FOOTER) {
            View foot_view = LayoutInflater.from(context).inflate(R.layout.item_foot_view, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            //view.setBackgroundColor(Color.RED);
            FootViewHolder footViewHolder = new FootViewHolder(foot_view);
            return footViewHolder;
        }
        return null;
    }

    /**
     * 数据的绑定显示
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        //根据holder的不同设置不同的数据
        if (holder instanceof ItemViewHolder) {
            //游记的发表者
            int userID = ((TravelNotesResult.TravelNotesBean)travelList.get(position)).getUserID();
            //游记的iD
            final int tn_id = ((TravelNotesResult.TravelNotesBean)travelList.get(position)).getTn_id();
            //当前登录用户的id
            UserBean user = UserBean.getUserBean();
            final int userNowID = user.userId;

            //绑定数据
            //用户头像
            Picasso.with(context)
                    .load(((TravelNotesResult.TravelNotesBean)travelList.get(position)).getUserImage())
                    .placeholder(R.mipmap.noshow)
                    .error(R.mipmap.zhanwei)
                    .into(((ItemViewHolder) holder).userImage);

            //用户名称
            ((ItemViewHolder) holder).userName.setText(((TravelNotesResult.TravelNotesBean)travelList.get(position)).getUserName());
            //游记标题
            ((ItemViewHolder) holder).travelTitle.setText(((TravelNotesResult.TravelNotesBean)travelList.get(position)).getTn_title());
            //收藏该游记的人及时间
            String start = ((TravelNotesResult.TravelNotesBean)travelList.get(position)).getStartUserTime();
            ((ItemViewHolder) holder).startTravel.setText("收藏");
            if(start != null && start != " "){
                //按“,”截取字符串
                String[] startSupport = start.split(",");
                int userStartLenth = startSupport.length;   //收藏人数
                for(int i=0;i<userStartLenth;i++){
                    if(userNowID == Integer.parseInt(startSupport[i].split("/")[0])){
                        ((ItemViewHolder) holder).startTravel.setTextColor(context.getResources().getColor(R.color.backThem));
                    }
                }
            }else{
                ((ItemViewHolder) holder).support.setText("赞");
            }
            ((ItemViewHolder) holder).startTravel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getIntent.getNotesStart(context,"addOneStart",userNowID,tn_id,1);
                    getIntent.setGetInterfaceBooleanListener(new GetInterfaceBooleanListener() {
                        @Override
                        public void getBoolean(boolean flag) {
                            if(flag){
                                Toast.makeText(context,"收藏成功",Toast.LENGTH_SHORT).show();
                                ((ItemViewHolder) holder).startTravel.setTextColor(context.getResources().getColor(R.color.backThem));
                            }else{
                                Toast.makeText(context,"已收藏",Toast.LENGTH_SHORT).show();
                                ((ItemViewHolder) holder).startTravel.setTextColor(context.getResources().getColor(R.color.backThem));
                            }
                        }
                    });
                }
            });
            //图片
            String image = ((TravelNotesResult.TravelNotesBean) travelList.get(position)).getTn_item_image();
            final String[] temp = image.split(",");
            //大图
            Picasso.with(context)
                    .load(Default.urlPic + temp[0])
                    .error(R.mipmap.zhanwei)
                    .config(Bitmap.Config.RGB_565)
                    .into(((ItemViewHolder) holder).bigPic);
            ((ItemViewHolder) holder).bigPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String[] temp1 = new String[1];
                    temp1[0] = temp[0];

                    Intent intent = new Intent(context, TravelPictureActivity.class);
                    intent.putExtra("picture", temp1);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            //小图横向列表
            ((ItemViewHolder) holder).recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
            RecyclerTravelNotesPicAdapter mAdapter = new RecyclerTravelNotesPicAdapter(context,temp);
            ((ItemViewHolder) holder).recyclerView.setAdapter(mAdapter);
            RecyclerTravelNotesPicAdapter.setOnItemClickListener(new RecyclerTravelNotesPicAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position, String[] travelList) {
                    String[] temp1 = new String[1];
                    temp1[0] = travelList[position+1];

                    Intent intent = new Intent(context, TravelPictureActivity.class);
                    intent.putExtra("picture", temp1);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

                @Override
                public void onItemLongClick(View view, int position, String[] travelList) {

                }
            });

            ((ItemViewHolder) holder).textTitle.setText(((TravelNotesResult.TravelNotesBean)travelList.get(position)).getTn_wordName());
            ((ItemViewHolder) holder).textText.setText(((TravelNotesResult.TravelNotesBean)travelList.get(position)).getTn_text());

            //赞
            String str = ((TravelNotesResult.TravelNotesBean)travelList.get(position)).getSupportUserTime();
            ((ItemViewHolder) holder).support.setTag(false);
            if(str != null && str != " "){
                //按“,”截取字符串
                String[] userSupport = str.split(",");
                //((ItemViewHolder) holder).support.setText("赞 " + userSupport.length);
                int userSupportLenth = userSupport.length;   //点赞人数

                for(int i=0;i<userSupportLenth;i++){
                    if(userNowID == Integer.parseInt(userSupport[i].split("/")[0])){
                        ((ItemViewHolder) holder).support.setTextColor(context.getResources().getColor(R.color.backThem));
                        ((ItemViewHolder) holder).support.setTag(true);
                    }
                }
            }else{
                ((ItemViewHolder) holder).support.setText("赞");
            }

            ((ItemViewHolder) holder).support.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if((boolean) view.getTag()){
                        ((ItemViewHolder) holder).support.setTextColor(context.getResources().getColor(R.color.backThem));
                        getIntent.Support(context,"addOneSupport",userNowID,tn_id,1);
                        view.setTag(false);
                        Log.i("赞","点赞");

                    }else{
                        getIntent.Support(context,"delOneSupportNotes",userNowID,tn_id,0);
                        Log.i("赞","取消点赞");
                        view.setTag(true);
                        ((ItemViewHolder) holder).support.setTextColor(context.getResources().getColor(R.color.backWord));
                    }
                }
            });

            ((ItemViewHolder) holder).getAll.setText("查看全部");
            ((ItemViewHolder) holder).message.setText("留言");

            if (mOnItemClickListener != null) {
                ((ItemViewHolder) holder).getAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickListener.onItemClick(((ItemViewHolder) holder).getAll, position,travelList);
                    }
                });
                ((ItemViewHolder) holder).getAll.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        mOnItemClickListener.onItemLongClick(holder.itemView, position,travelList);
                        return false;
                    }
                });
                ((ItemViewHolder) holder).message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickListener.onMessageClick(((ItemViewHolder) holder).message,((TravelNotesResult.TravelNotesBean)travelList.get(position)).getTn_id());
                    }
                });
            }
            ((ItemViewHolder) holder).share.setText("分享");

        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (load_more_status) {
                //这个可以使用2个也可使用1个
                case PULLUP_LOAD_MORE:
                    footViewHolder.foot_view_item_tv.setText("上拉加载更多...");
                    break;
                case LOADING_MORE:
                    footViewHolder.foot_view_item_tv.setText("正在加载更多数据...");
                    load_more_status = PULLUP_LOAD_MORE;
                    break;
            }
        }
    }

    /**
     * 要显示的item数目
     * @return
     */
    @Override
    public int getItemCount() {
        //由于添加了footer所以返回的值都要加1个
        if (travelList == null || travelList.size() < 1) {
            return 1;
        }
        return travelList.size() + 1;

    }

    public void refreshItem(ArrayList<String> newDatas) {
        //这里下拉刷新的数据是加到头部的
        ArrayList<String> a1 = new ArrayList<>();
        a1.addAll(newDatas);
        a1.addAll(travelList);
        travelList = a1;
        notifyDataSetChanged();
    }

    public void addMoreData(ArrayList<String> moreList) {
        //上啦加载更多的数据是加载到末尾的
        travelList.addAll(moreList);
        notifyDataSetChanged();
    }

    public void changeData(ArrayList<String> moreList) {
        //上啦加载更多的数据是加载到末尾的
        travelList = moreList;
        notifyDataSetChanged();
    }

    /**
     * 底部FootView布局
     */
    public class FootViewHolder extends RecyclerView.ViewHolder {
        private TextView foot_view_item_tv;

        public FootViewHolder(View view) {
            super(view);
            foot_view_item_tv = (TextView) view.findViewById(R.id.foot_view_item_tv);
        }
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView userImage;
        public TextView userName;
        public TextView travelTitle;
        public TextView startTravel;
        public ImageView bigPic;
        public RecyclerView recyclerView;
        public TextView textTitle;
        public TextView textText;
        public TextView getAll;
        public TextView support;
        public TextView message;
        public TextView share;

        public RecyclerView recyclerMessage;

        public ItemViewHolder(View view) {
            super(view);
            userImage = (ImageView) view.findViewById(R.id.userImage);
            userName = (TextView) view.findViewById(R.id.userName);
            travelTitle = (TextView) view.findViewById(R.id.travelTitle);
            startTravel = (TextView) view.findViewById(R.id.startTravel);
            bigPic = (ImageView) view.findViewById(R.id.bigPic);
            recyclerView = (RecyclerView) view.findViewById(R.id.travelItem);
            textTitle = (TextView) view.findViewById(R.id.textTitle);
            textText = (TextView) view.findViewById(R.id.textText);
            getAll = (TextView) view.findViewById(R.id.getAll);
            support = (TextView) view.findViewById(R.id.support);
            message = (TextView) view.findViewById(R.id.message);
            share = (TextView) view.findViewById(R.id.share);

            recyclerMessage = (RecyclerView) view.findViewById(R.id.travelNotesMessage);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            //如果当前位置再加1为总长度。就返回footer
            //假如list.size == 6 。则最大position为5  由于都加了1个 最大position为6 ， getItemCount()为7-----当6+1==7时，就是最末尾了
            return TYPE_FOOTER;
        } else {
            //否则返回正常的item
            return TYPE_ITEM;
        }
    }
}