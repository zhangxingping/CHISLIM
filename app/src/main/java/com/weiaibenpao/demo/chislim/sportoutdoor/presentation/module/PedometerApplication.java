package com.weiaibenpao.demo.chislim.sportoutdoor.presentation.module;

import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.umeng.socialize.PlatformConfig;
import com.weiaibenpao.demo.chislim.sportoutdoor.presentation.common.BaseApplication;

import cn.jpush.android.api.JPushInterface;

public class PedometerApplication extends BaseApplication {

    private static final String TAG = "JPush";
    public PedometerApplication() {
        super();
    }
    public static PedometerApplication app;
    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationModule.initSingleton().onCreateMainProcess();
        app=this;
        // 记得使用这个fresco要在Application中初始化啊！
        //Fresco.initialize(this);
        // the following line is important
        Fresco.initialize(getApplicationContext());

        PlatformConfig.setWeixin("wxc7d35c05747b01f1", "a57e6ef8ef355fbd1258e279a9c4a548");
        //微信 appid appsecret

        PlatformConfig.setSinaWeibo("1959713611","e62e0d810039469a8852ba366afdb0bc");
        //新浪微博 appkey appsecret

        PlatformConfig.setQQZone("1105642894", "3cvNf8EoHe17VQ93");
        // QQ和Qzone appid appkey

        /*极光推送*/
        Log.d(TAG, "[MyBaseApplication] onCreate");
        super.onCreate();

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
    }
}
