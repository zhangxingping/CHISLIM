package com.weiaibenpao.demo.chislim.bean;

/**
 * Created by lenovo on 2016/8/15.
 */

public class UserBean {

    public int userId;
    public String userName;
    public String userPhoone;
    public String userBirth;
    public String userImage;
    public String userTntru;

    public String userSex;
    public int userProject;
    public String userEmail;

    public Float userWeight;
    public int userHeigh;
    public int userBmi;

    public int userMark;
    public String userHobby;

    public String userOpenid;

    private static UserBean userBean;
    private UserBean() {
    }

    public static UserBean getUserBean() {
        if (null == userBean) {
            userBean = new UserBean();
        }
        return userBean;
    }

}
