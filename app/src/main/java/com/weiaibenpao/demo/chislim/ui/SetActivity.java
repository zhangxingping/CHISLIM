package com.weiaibenpao.demo.chislim.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.util.ACache;
import com.weiaibenpao.demo.chislim.util.StackManager;


public class SetActivity extends Activity {

    public static final String PREFS_NAME = "UserInfo";
    private SharedPreferences settings;

    private ImageView back;
    StackManager manager = new StackManager();

    private ACache mCache;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_set);
        context = getApplicationContext();
        mCache = ACache.get(context);
        settings = getSharedPreferences(PREFS_NAME, 0);

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 修改用户信息
     * @param v
     */
    public void updateUserInfo(View v){
        Intent intent = new Intent(SetActivity.this,UpdateUserActivity.class);
        startActivity(intent);
    }

    /**
     * 修改密码
     * @param v
     */
    public void updateUserPass(View v){
        Intent intent = new Intent(SetActivity.this,UpdatePassActivity.class);
        startActivity(intent);
    }

    /**
     * 关于
     * @param v
     */
    public void about(View v){
        Intent intent = new Intent(SetActivity.this,AboutActivity.class);
        startActivity(intent);
    }

    /**
     * 切换账号
     */
    public void changeUser(View v){

        //获得SharedPreferences.Editor对象，使SharedPreferences对象变为可编辑状态（生成编辑器）
        SharedPreferences.Editor edit = settings.edit();
        //清除文件内容
        /*edit.clear();*/
        edit.putBoolean("content",false);
        //提交
        edit.commit();
        manager.popAllActivitys();
        Intent intent = new Intent(SetActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void clearCatch(View  v){
        mCache.clear();
    }
}
