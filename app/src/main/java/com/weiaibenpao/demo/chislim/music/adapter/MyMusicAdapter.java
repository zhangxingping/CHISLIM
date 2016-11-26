package com.weiaibenpao.demo.chislim.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.music.bean.Bean_music;
import com.weiaibenpao.demo.chislim.music.holder.Helder;

import java.util.ArrayList;

/**
 * Created by 建宝 on 2016/4/2.
 * 给音乐播放列表的适配器
 */
public class MyMusicAdapter extends BaseAdapter {

    private ArrayList mBA_ArrayList;
    private Context mContext;

    public MyMusicAdapter(Context c, ArrayList a) {
        mContext = c;
        mBA_ArrayList = a;
    }

    @Override
    public int getCount() {
        return mBA_ArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Helder mhander;
        if (convertView == null) {

            mhander = new Helder();

            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, null);

           // mhander.setMy_imageView((ImageView) convertView.findViewById(R.id.mImage));
            mhander.setMy_songname((TextView) convertView.findViewById(R.id.songName));
            mhander.setMy_musicname((TextView) convertView.findViewById(R.id.music_name));


            convertView.setTag(mhander);

        } else {
            mhander = (Helder) convertView.getTag();
        }
/*        Picasso.with(mContext)
                .load(R.mipmap.pic5)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(mhander.getMy_imageView());*/
        /*mhander.getMy_imageView().setImageResource(((Bean_music) (mBA_ArrayList.get(position))).getSong_image());*/
        mhander.getMy_songname().setText(((Bean_music) (mBA_ArrayList.get(position))).getMusic_title());
        mhander.getMy_musicname().setText(((Bean_music) mBA_ArrayList.get(position)).getMusic_singer());

        return convertView;
    }
}
