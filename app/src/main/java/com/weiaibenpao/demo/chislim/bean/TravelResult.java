package com.weiaibenpao.demo.chislim.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2016/10/26.
 */

public class TravelResult implements Serializable{


    /**
     * error : 0
     * travel : [{"t_id":26,"t_name":"仙女湖","t_image":"tra26","t_title":"一弯秋月倒影水中","t_text":"原来，说过和你一起去外面走走,你也说那几天正准备出去散个心\u2026\u2026结果，出发前几天，你一直说算了吧,哪怕是我退一步说，那晚上出来吃个饭吧，结果还是NO，一度想取消自己的出行计划，因为完全没了心情，\r\n后来，想想，我自己还会去吧，自己在心里，帮你找了个理由，也许你在忙着写\u2026\u2026","t_address":"location","t_time":"2016.10.24.16.20.15","t_del":0},{"t_id":23,"t_name":"敦煌","t_image":"tra23","t_title":"一弯秋月倒影水中","t_text":"原来，说过和你一起去外面走走,你也说那几天正准备出去散个心\u2026\u2026结果，出发前几天，你一直说算了吧,哪怕是我退一步说，那晚上出来吃个饭吧，结果还是NO，一度想取消自己的出行计划，因为完全没了心情，\r\n后来，想想，我自己还会去吧，自己在心里，帮你找了个理由，也许你在忙着写\u2026\u2026","t_address":"location","t_time":"2016.10.20.16.05.53","t_del":0},{"t_id":18,"t_name":"小瑞士","t_image":"tra18","t_title":"仙女湖","t_text":"原来，说过和你一起去外面走走,你也说那几天正准备出去散个心\u2026\u2026结果，出发前几天，你一直说算了吧,哪怕是我退一步说，那晚上出来吃个饭吧，结果还是NO，一度想取消自己的出行计划，因为完全没了心情，\r\n后来，想想，我自己还会去吧，自己在心里，帮你找了个理由，也许你在忙着写\u2026\u2026","t_address":"location","t_time":"2016.10.20.16.05.53","t_del":0}]
     */

    private int error;
    /**
     * t_id : 26
     * t_name : 仙女湖
     * t_image : tra26
     * t_title : 一弯秋月倒影水中
     * t_text : 原来，说过和你一起去外面走走,你也说那几天正准备出去散个心……结果，出发前几天，你一直说算了吧,哪怕是我退一步说，那晚上出来吃个饭吧，结果还是NO，一度想取消自己的出行计划，因为完全没了心情，
     后来，想想，我自己还会去吧，自己在心里，帮你找了个理由，也许你在忙着写……
     * t_address : location
     * t_time : 2016.10.24.16.20.15
     * t_del : 0
     */

    private List<TravelBean> travel;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public List<TravelBean> getTravel() {
        return travel;
    }

    public void setTravel(List<TravelBean> travel) {
        this.travel = travel;
    }

    public static class TravelBean implements Parcelable,Serializable{
        private int t_id;
        private String t_name;
        private String t_image;
        private String t_title;
        private String t_text;
        private String t_address;
        private String t_time;
        private int t_del;

        protected TravelBean(Parcel in) {
            t_id = in.readInt();
            t_name = in.readString();
            t_image = in.readString();
            t_title = in.readString();
            t_text = in.readString();
            t_address = in.readString();
            t_time = in.readString();
            t_del = in.readInt();
        }

        public static final Creator<TravelBean> CREATOR = new Creator<TravelBean>() {
            @Override
            public TravelBean createFromParcel(Parcel in) {
                return new TravelBean(in);
            }

            @Override
            public TravelBean[] newArray(int size) {
                return new TravelBean[size];
            }
        };

        public int getT_id() {
            return t_id;
        }

        public void setT_id(int t_id) {
            this.t_id = t_id;
        }

        public String getT_name() {
            return t_name;
        }

        public void setT_name(String t_name) {
            this.t_name = t_name;
        }

        public String getT_image() {
            return t_image;
        }

        public void setT_image(String t_image) {
            this.t_image = t_image;
        }

        public String getT_title() {
            return t_title;
        }

        public void setT_title(String t_title) {
            this.t_title = t_title;
        }

        public String getT_text() {
            return t_text;
        }

        public void setT_text(String t_text) {
            this.t_text = t_text;
        }

        public String getT_address() {
            return t_address;
        }

        public void setT_address(String t_address) {
            this.t_address = t_address;
        }

        public String getT_time() {
            return t_time;
        }

        public void setT_time(String t_time) {
            this.t_time = t_time;
        }

        public int getT_del() {
            return t_del;
        }

        public void setT_del(int t_del) {
            this.t_del = t_del;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(t_id);
            parcel.writeString(t_name);
            parcel.writeString(t_image);
            parcel.writeString(t_title);
            parcel.writeString(t_text);
            parcel.writeString(t_address);
            parcel.writeString(t_time);
            parcel.writeInt(t_del);
        }
    }
}
