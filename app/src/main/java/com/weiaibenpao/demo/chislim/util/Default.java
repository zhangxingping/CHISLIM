package com.weiaibenpao.demo.chislim.util;

import com.weiaibenpao.demo.chislim.bean.MenuBean;
import com.weiaibenpao.demo.chislim.bean.SportTypeBean;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/8/13.
 */

public class Default {
    public static final String url = "http://112.74.28.179:8080";
    public static final String baiduKey = "467fa36385f73a29f31a6be49a221857";
    public static final String getOneByPhone = "getOneByPhone";
    public static final String getAllTeach = "getAll";
    public static final String getOne = "getOne";
    public static final String updateUser = "updateUser";
    public static final String getOneSport = "getOne";
    public static final String updateProject = "updateProject";
    public static final String updateChannelId = "updateChannelId";
    public static final String countDis = "countDis";
    public static final String getOneByQQ = "getOneByQQ";
    public static final String getOneByWxin = "getOneByWxin";
    public static final String updateMark = "updateMark";
    public static final String getOneByWbo = "getOneByWbo";
    public static final String appKey = "e42cce3acb4d25422ca7451cdb6dc5d2";    //天气预报，聚合的数据

    public static final String urlPic = "http://ofplk6att.bkt.clouddn.com/";


    public static final int FIND = 0;
    public static final int LINK = 1;
    public static final int START = 2;
    public static final int STOP = 3;
    public static final long TIME_OUT = 20000;                                   // 扫描超时时间

    public static ArrayList initList(){
        ArrayList disList = new ArrayList();
        disList.add(3);
        disList.add(5);
        disList.add(7);
        disList.add(9);
        disList.add(10);
        disList.add(15);
        disList.add(20);
        disList.add(25);
        disList.add(30);
        disList.add(35);
        disList.add(40);
        disList.add(50);
        disList.add(70);
        disList.add(100);
        return disList;
    }


    public static final int WRITE_EXTERNAL_STORAGE_CODE = 6;
    public static final int READ_EXTERNAL_STORAGE_CODE = 7;


    public static ArrayList MenuList(){
        ArrayList menuList = new ArrayList();

        MenuBean menuBean1 = new MenuBean("http://ofplk6att.bkt.clouddn.com/home_music.png","音乐");
        MenuBean menuBean2 = new MenuBean("http://ofplk6att.bkt.clouddn.com/home_movie.png","电影");
        menuList.add(menuBean1);
        menuList.add(menuBean2);
        return menuList;
    }

    public static ArrayList getTeachMenuList(){
        ArrayList<String> teachMenuList = new ArrayList<String>();
        teachMenuList.add("全部教程");
        teachMenuList.add("减脂");
        teachMenuList.add("塑型");
        teachMenuList.add("增肌");
        teachMenuList.add("放松");
        teachMenuList.add("瑜伽");
        teachMenuList.add("跑步");

        return  teachMenuList;
    }


    public static ArrayList getSportTypeHome(){
        ArrayList<SportTypeBean> sportType = new ArrayList<SportTypeBean>();
        SportTypeBean sportTypeBean1 = new SportTypeBean("111111111111111111","沙地模式","25:00");
        SportTypeBean sportTypeBean2 = new SportTypeBean("111111111111111111","高原模式","25:00");
        SportTypeBean sportTypeBean3 = new SportTypeBean("111111111111111111","登山模式","25:00");
        SportTypeBean sportTypeBean4 = new SportTypeBean("111111111111111111","滨海小镇","25:00");
        SportTypeBean sportTypeBean5 = new SportTypeBean("111111111111111111","暴走模式","25:00");
        SportTypeBean sportTypeBean6 = new SportTypeBean("111111111111111111","音乐模式","25:00");
        SportTypeBean sportTypeBean7 = new SportTypeBean("111111111111111111","沙地模式","25:00");
        SportTypeBean sportTypeBean8 = new SportTypeBean("111111111111111111","高原模式","25:00");
        SportTypeBean sportTypeBean9 = new SportTypeBean("111111111111111111","登山模式","25:00");
        SportTypeBean sportTypeBean10 = new SportTypeBean("111111111111111111","滨海小镇","25:00");
        SportTypeBean sportTypeBean11 = new SportTypeBean("111111111111111111","暴走模式","25:00");
        SportTypeBean sportTypeBean12 = new SportTypeBean("111111111111111111","音乐模式","25:00");


        sportType.add(sportTypeBean1);
        sportType.add(sportTypeBean2);
        sportType.add(sportTypeBean3);
        sportType.add(sportTypeBean4);
        sportType.add(sportTypeBean5);
        sportType.add(sportTypeBean6);
        sportType.add(sportTypeBean7);
        sportType.add(sportTypeBean8);
        sportType.add(sportTypeBean9);
        sportType.add(sportTypeBean10);
        sportType.add(sportTypeBean11);
        sportType.add(sportTypeBean12);
        sportType.add(sportTypeBean1);
        sportType.add(sportTypeBean2);
        sportType.add(sportTypeBean3);
        sportType.add(sportTypeBean4);
        sportType.add(sportTypeBean5);
        sportType.add(sportTypeBean6);
        sportType.add(sportTypeBean7);
        sportType.add(sportTypeBean8);
        sportType.add(sportTypeBean9);
        sportType.add(sportTypeBean10);
        sportType.add(sportTypeBean11);
        sportType.add(sportTypeBean12);

        return  sportType;
    }


    public static ArrayList getHealthList(){
        ArrayList<String> healthList = new ArrayList<String>();
        healthList.add("http://ofplk6att.bkt.clouddn.com/home_boji.png");
        healthList.add("http://ofplk6att.bkt.clouddn.com/home_zengji.png");
        healthList.add("http://ofplk6att.bkt.clouddn.com/home_suxing.png");
        healthList.add("http://ofplk6att.bkt.clouddn.com/home_yujia.png");
        healthList.add("http://ofplk6att.bkt.clouddn.com/home_qixie.png");
        healthList.add("http://ofplk6att.bkt.clouddn.com/home_jianzhi.png");
        return  healthList;
    }
    //healthList.add("http://ofplk6att.bkt.clouddn.com/home_movie.png");
    //healthList.add("http://ofplk6att.bkt.clouddn.com/home_music.png");
    //healthList.add("http://ofplk6att.bkt.clouddn.com/home_import_class.png");

}
