package com.weiaibenpao.demo.chislim.music.bean;

/**
 * Created by lenovo on 2016/11/9.
 */

public class MusicSinerResult {

    /**
     * code : 0
     * status : success
     * msg : 数据请求成功
     * data : {"singername":"陈奕迅","image":"http://img1.music.response.itmf.cn/uploadpic/pass/softhead/400/20151103/20151103182436333557.jpg"}
     */

    private int code;
    private String status;
    private String msg;
    /**
     * singername : 陈奕迅
     * image : http://img1.music.response.itmf.cn/uploadpic/pass/softhead/400/20151103/20151103182436333557.jpg
     */

    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String singername;
        private String image;

        public String getSingername() {
            return singername;
        }

        public void setSingername(String singername) {
            this.singername = singername;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
