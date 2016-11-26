package com.weiaibenpao.demo.chislim.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.weiaibenpao.demo.chislim.Interface.OnGetPro;
import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.update.UpdateManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,OnGetPro {
    //推送会用到
    public static boolean isForeground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        new UpdateManager(this).checkUpdate( );     //监测更新

        //判断网络是否连接
        ConnectivityManager con=(ConnectivityManager)getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        if(wifi|internet){
            //执行相关操作
            Log.i("网络","网络已连接");
        }else{
            Toast.makeText(getApplicationContext(),
                    "亲，网络连了么？", Toast.LENGTH_LONG)
                    .show();
        }
    }

    FragmentTabHost tabHost;
    private void initView(){
        tabHost= (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this,getSupportFragmentManager(),R.id.content);
        initTabs();
    }

    private void initTabs(){
        MainTab[] tabs = MainTab.values();
        final int size = tabs.length;

        for (int i=0;i<size ;i++){
            MainTab mainTab=tabs[i];
            TabHost.TabSpec tab=tabHost.newTabSpec(getString(mainTab.getResName()));
            View indication= LayoutInflater.from(this).inflate(R.layout.tab_indication,null);
            TextView textView= (TextView) indication.findViewById(R.id.tab_title);
            textView.setText(mainTab.getResName());
            Drawable drawable= ContextCompat.getDrawable(this,mainTab.getResIcon());
            textView.setCompoundDrawablesWithIntrinsicBounds(null, drawable,null,null);
            tab.setIndicator(indication);
            tabHost.addTab(tab,mainTab.getClz(),null);
            tabHost.getTabWidget().setDividerDrawable(null);//去掉竖直分割线
        }
    }

    @Override
    public void onClick(View view) {

    }


    @Override
    public void getPro(int pro) {

    }


    /**
     * 查询运动称号树,点击运动称号一整行触发
     * @param v
     */
    public void getSuccess(View v){
        Intent intent = new Intent(MainActivity.this,SuccessActivity.class);
        startActivity(intent);
    }

    /**
     * 查看运动记录,点击个人记录一整行触发
     */
    public void getHistory(View v){
        Intent intent = new Intent(MainActivity.this,MyHistoryActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到积分规则界面
     */
    public void goMark(View v){
        Intent intent = new Intent(this,MarkInfoActivity.class);
        startActivity(intent);
    }


    /**
     * 分享回调
     */
    @Override
    public void shareSport(String text,String title,String url,String media) {
        /*目前支持文本、图片（本地及URL）、音频及视频URL的分享,要分享图片的URL*/
        UMImage image = new UMImage(MainActivity.this, media);
        final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
                {
                        SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.SINA,
                        SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
                };
        new ShareAction(this).setDisplayList( displaylist )
                .withText( text )
                .withTitle(title)
                .withTargetUrl(url)
                .withMedia(image)
                .setListenerList(umShareListener)
                .open();
    }

    /**
     *  分享回调
     */
    private UMShareListener umShareListener = new UMShareListener(){
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.i("分享","-------------成功-----------");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(MainActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Log.i("分享","-------------取消-----------");
        }
    };

    /**
     * 分享面板添加按钮的回调
     */
    private ShareBoardlistener shareBoardlistener = new  ShareBoardlistener() {

        @Override
        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
            if (share_media==null){
                if (snsPlatform.mKeyword.equals("11")){
                    Log.i("分享面板添加按钮的回调","-------------add button success-----------");
                }
            }
            else {
                new ShareAction(MainActivity.this).setPlatform(share_media).setCallback(umShareListener)
                        .withText("多平台分享")
                        .share();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get( this ).onActivityResult( requestCode, resultCode, data);
    }




    /**
     * 推送相关
     */
    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    /*private void init(){
        JPushInterface.init(getApplicationContext());
        String rid = JPushInterface.getRegistrationID(getApplicationContext());
        if (!rid.isEmpty()) {
            Log.i("RegistrationID","-----------成功---------------" + rid);
            changeChannelId(rid);
        } else {
            Log.i("RegistrationID","-----------失败---------------");
        }
    }
    // 停止 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void stop(){
        JPushInterface.stopPush(getApplicationContext());
    }
    // 恢复 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void resume(){
        JPushInterface.resumePush(getApplicationContext());
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    *//**
     * 推送广播
     *//*
    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    JSONObject extrasJson = null;
                    try {
                        extrasJson = new JSONObject(extras);
                        String myValue = extrasJson.optString("url");
                        Log.i("url在MainActivity","---------------------" + myValue + "--------------------");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                setCostomMsg(showMsg.toString());
            }
        }
    }

    private void setCostomMsg(String msg){
       *//* if (null != msgText) {
            msgText.setText(msg);
            msgText.setVisibility(android.view.View.VISIBLE);
        }*//*
        Log.i("版本",android.view.View.VISIBLE+"");
    }


    public void initJpush(){
        String udid =  ExampleUtil.getImei(getApplicationContext(), "");
        String appKey = ExampleUtil.getAppKey(getApplicationContext());
        String deviceId = ExampleUtil.getDeviceId(getApplicationContext());
        String versionName =  ExampleUtil.GetVersion(getApplicationContext());

        String rid = JPushInterface.getRegistrationID(getApplicationContext());
        if (!rid.isEmpty()) {

        } else {
            Log.i("推送初始化失败","---------------------" + "Get registration fail, JPush init failed!" + "--------------------");
        }

        Log.i("推送初始化成功",udid + "---" + appKey + "---" + deviceId + "---" + versionName + "-------" + rid);
        init();
    }


    *//**
     * 推送设置别名
     *//*
    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    private void setAlias() {
        String alias ="jianbaopp";
        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i("推送tag设置", logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i("推送tag设置", logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e("推送tag设置", logs);
            }
            //  ExampleUtil.showToast(logs, getApplicationContext());
        }
    };
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d("推送tag设置", "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    Log.i("推送tag设置", "Unhandled msg - " + msg.what);
            }
        }
    };

    *//**
     * 修改推送标示
     * @param rid
     *//*
    public void changeChannelId(String rid){
        Call<BooleanResult> call = UpdateChannelIdModel.getModel().getService().getResult(Default.updateChannelId,user.userId,rid);

        call.enqueue(new Callback<BooleanResult>() {
            @Override
            public void onResponse(Call<BooleanResult> call, Response<BooleanResult> response) {
                if (response.isSuccessful()) {
                    BooleanResult result = response.body();
                    if (result.isFlag()) {
                        Log.i("ChannelId","推送标示上传成功");
                    }else{
                        Log.i("ChannelId","推送标示上传失败");
                    }
                }
            }

            @Override
            public void onFailure(Call<BooleanResult> call, Throwable t) {

            }
        });
    }*/

}
