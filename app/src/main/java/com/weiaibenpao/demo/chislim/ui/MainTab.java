package com.weiaibenpao.demo.chislim.ui;

import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.fragment.NewFirstViewPagerFragment;
import com.weiaibenpao.demo.chislim.fragment.SportFragment;
import com.weiaibenpao.demo.chislim.fragment.UserFragment;
import com.weiaibenpao.demo.chislim.viewpagerfragment.ThirdViewPagerFragment;

/**
 * Created by Administrator on 2016/4/22.
 */
public enum MainTab {

    FIRST(0, R.string.first,R.drawable.tab_icon, NewFirstViewPagerFragment.class),
    //SECOND(1,R.string.second, R.drawable.tab_icon, SecondViewPagerFragment.class),    //隐藏了第二个界面
    FIFTH(4,R.string.fifth,R.drawable.tab_icon,SportFragment.class),
    THIRD(2,R.string.third, R.drawable.tab_icon, ThirdViewPagerFragment.class),
    FOURTH(3,R.string.fourth,R.drawable.tab_icon,UserFragment.class);


    private int index,resName,resIcon;
    Class<?> clz;
    private MainTab(int index,int resName,int resIcon,Class<?> clz){
        this.index=index;
        this.resName=resName;
        this.resIcon=resIcon;
        this.clz=clz;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getResName() {
        return resName;
    }

    public void setResName(int resName) {
        this.resName = resName;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }
}
