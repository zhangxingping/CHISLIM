package com.weiaibenpao.demo.chislim.music.dao;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.weiaibenpao.demo.chislim.music.bean.Bean_music;

import java.util.ArrayList;

/**
 * Created by 建宝 on 2016/4/11.
 */
public class Dao_Get_music extends ContentResolver {

    private ArrayList music_Array = new ArrayList();

    public ArrayList getMusic_Array() {
        return music_Array;
    }

    public Dao_Get_music(Context context) {
        super(context);

        ContentResolver cr = context.getContentResolver();
        if (cr == null) {
            return;
        }
        // 获取所有歌曲
        Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        if (null == cursor) {
            return;
        }


        int i = 0;
        if (cursor.moveToFirst()) {
            do {
                Bean_music music = new Bean_music();
                //获取标题
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                music.setMusic_title(title);

                //获取歌手名
                String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                music.setMusic_singer(singer);

                //获取歌手专辑名
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                music.setMusic_album(album);

                //获取歌曲文件的大小
                long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                music.setMusic_size(size);

                //歌曲的播放时长
                int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                music.setMusic_duration(duration);

                //获取歌曲的文件路径
                String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                music.setMusic_url(url);

               // music.setSong_image(R.mipmap.bz10);

                music_Array.add(i,music);
                i++;

            } while (cursor.moveToNext());
        }
    }
}
