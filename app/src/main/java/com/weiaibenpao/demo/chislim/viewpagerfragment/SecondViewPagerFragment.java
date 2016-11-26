package com.weiaibenpao.demo.chislim.viewpagerfragment;

import android.support.design.widget.TabLayout;

import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.adater.ViewPagerFragmentAdapter;
import com.weiaibenpao.demo.chislim.base.BaseViewPageFragment;
import com.weiaibenpao.demo.chislim.fragment.B_FirstFragment;
import com.weiaibenpao.demo.chislim.fragment.B_SecondFragment;
import com.weiaibenpao.demo.chislim.fragment.B_ThirdFragment;


/**
 * Created by Administrator on 2016/4/26.
 */
public class SecondViewPagerFragment extends BaseViewPageFragment {

    @Override
    public void onSetupTabAdater(ViewPagerFragmentAdapter adapter) {
        String[] titles = getResources().getStringArray(R.array.second_viewpage_arrays);
        adapter.addTab(titles[0],titles[0],B_FirstFragment.class,null);
        adapter.addTab(titles[1],titles[1],B_SecondFragment.class,null);
        adapter.addTab(titles[2],titles[2],B_ThirdFragment.class,null);
        /*adapter.addTab(titles[3],titles[3],B_FourthFragment.class,null);*/
    }

    @Override
    public void setScreenPageLimit() {
        mViewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void setTabLayout(TabLayout tabLayout) {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }
}
