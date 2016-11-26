package com.weiaibenpao.demo.chislim.map.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.map.util.LogUtil;
import com.weiaibenpao.demo.chislim.service.Map_Service;
import com.weiaibenpao.demo.chislim.sportoutdoor.data.model.database.core.PedometerCardEntity;
import com.weiaibenpao.demo.chislim.sportoutdoor.presentation.common.utils.HardwarePedometerUtil;
import com.weiaibenpao.demo.chislim.sportoutdoor.presentation.module.ApplicationModule;
import com.weiaibenpao.demo.chislim.sportoutdoor.presentation.presenter.PedometerPresenter;
import com.weiaibenpao.demo.chislim.sportoutdoor.presentation.view.component.WindmillView;
import com.weiaibenpao.demo.chislim.sportoutdoor.presentation.view.iview.IPedometerView;
import com.weiaibenpao.demo.chislim.ui.SportStyleActivity;
import com.weiaibenpao.demo.chislim.util.ACache;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.weiaibenpao.demo.chislim.util.Default.url;

/**
 * Created by yhy on 2016/5/4.
 */
public class DrawTraceActivity extends Activity implements LocationSource, IPedometerView {


    private static final int LOCATION_TIME_INTERVAL = 4000;

    Bundle mSavedInstanceState;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.toplayout)
    RelativeLayout toplayout;
    @BindView(R.id.dis)
    TextView dis;
    @BindView(R.id.sportTime)
    Chronometer sportTime;
    @BindView(R.id.tv_steps)
    TextView tvSteps;
    @BindView(R.id.myCalories)
    TextView myCalories;
    @BindView(R.id.view_windmill)
    WindmillView viewWindmill;
    @BindView(R.id.tv_target_steps)
    TextView tvTargetSteps;
    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.showView)
    LinearLayout showView;
    @BindView(R.id.myTime)
    TextView myTime;
    @BindView(R.id.timeView)
    RelativeLayout timeView;
    @BindView(R.id.textWord)
    TextView textWord;
    @BindView(R.id.sportType)
    TextView sportType;
    @BindView(R.id.btn_goto)
    Button btnGoto;
    @BindView(R.id.startView)
    RelativeLayout startView;
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.share)
    TextView share;

    private ACache mCache;   //保存零食数据

    private AMap aMap;
    private UiSettings mUiSettings;
    // private OnLocationChangedListener mLocationLinstener;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    PedometerPresenter mPedometerPresenter;

    private ArrayList<LatLng> mLocationList = new ArrayList<LatLng>();

    private boolean mIsFirstLocation = true;
    private Marker mMarkMyLocation;
    private double mLocatinLat;
    private double mLocationLon;
    private double mBestLat;
    private double mBestLon;
    private double currLength;
    private long lastTime = 0;
    private long currTime = 0;
    private int errorCnt = 0;
    private long minusTime;
    //当前经纬度
    private LatLng mCurrentLatLng;
    //上次经纬度
    private LatLng mLastLatLng;
    private LatLng currLa;
    private LatLng lastLa = new LatLng(0, 0);
    private LatLng overLa = new LatLng(0, 0);

    private boolean mIsStart = false;

    private Map_Service map_Service;  //服务对象

    MyBroadcast myBroadcast;  //广播对象
    float disStyle;   //用来判断运动模式

    Context context;

    /**
     * 0 服务启动
     * 1 正常运动
     * 2 暂停
     * 3 继续
     * 4 停止
     */
    private String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_trace);
        ButterKnife.bind(this);
//        mapView = (MapView) findViewById(R.id.map);
//        mapView.onCreate(savedInstanceState);// 此方法必须重写

        mSavedInstanceState = savedInstanceState;
        context = getApplicationContext();
        mCache = ACache.get(context);
        mCache.put("state", "0");
        //mapView.onCreate(savedInstanceState);// 此方法必须重写
        initView();            //实例化定位
        init();               //实例化计步
        initStyleView();      //运动模式
        initTrimer();         //计时器
        getState();

        //配置广播
        myBroadcast = new MyBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.lijianbao.mapLocationLatLong");
        DrawTraceActivity.this.registerReceiver(myBroadcast, filter);
    }


    protected void initView() {
        mapView.onCreate(mSavedInstanceState);// 此方法必须重写
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
                @Override
                public void onMapLoaded() {
                    aMap.setMapType(AMap.MAP_TYPE_NORMAL);
                    setMyLocationStyleIcon();
//                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(laQuick, loQuick), 17));
                }
            });

        }
        mUiSettings = aMap.getUiSettings();
        mUiSettings.setLogoPosition(2);//设置高德地图logo位置
        mUiSettings.setZoomControlsEnabled(false);
        // mUiSettings.setTiltGesturesEnabled(false);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.setMyLocationEnabled(true);
        //     initLocation();

    }

    private void setMyLocationStyleIcon() {
//		 自定义系统定位小蓝点

        MyLocationStyle myLocationStyle = new MyLocationStyle();
        ImageView iv = new ImageView(this);
        FrameLayout.LayoutParams fmIv = new FrameLayout.LayoutParams(1, 1);
        iv.setImageResource(R.mipmap.location);
        iv.setLayoutParams(fmIv);
        BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromView(iv);
        myLocationStyle.myLocationIcon(markerIcon);          // 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));       // 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));      // 设置圆形的填充颜色
        myLocationStyle.strokeWidth(0f);// 设置圆形的边框粗细
//				myLocationStyle.
        myLocationStyle.anchor(0.5f, 0.9f);
        aMap.setMyLocationStyle(myLocationStyle);

//        aMap.setMyLocationEnabled(true);
////				// 设置定位的类型为 跟随模式
//        aMap.setMyLocationType(AMap.MAP_TYPE_NORMAL);
    }


    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        //mLocationLinstener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        // mLocationLinstener = null;
    }


    public void initTrimer() {
        sportTime.setBase(SystemClock.elapsedRealtime());//计时器清零
        int hour = (int) ((SystemClock.elapsedRealtime() - sportTime.getBase()) / 1000 / 60);
        sportTime.setFormat("0" + String.valueOf(hour) + ":%s");
    }

    public void getState() {
        try {
            state = mCache.getAsString("state");
            Log.i("状态", state);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (state.equals("1")) {
            Log.i("状态", state);
            startView.setVisibility(View.GONE);
            timeView.setVisibility(View.GONE);
        } else if (state.equals("0")) {
            startView.setVisibility(View.VISIBLE);
            timeView.setVisibility(View.GONE);
        }
    }

    public void getLatLong() {


        if (mIsFirstLocation) {
            mIsFirstLocation = false;
//                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLocatinLat, mLocationLon), 17));
            setMyStopLoca(new LatLng(mLocatinLat, mLocationLon));
////                    setMyLocationStyleIcon();
////                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(latitude, longitude)));
//                    setMyStopLoca(new LatLng(latitude, longitude));
//
            mLocationList.add(new LatLng(mLocatinLat, mLocationLon));
//                    mTrueLat = latitude;
//                    mTrueLon = longitude;
//
//                    mSendLat = latitude;
//                    mSendLon = longitude;
//
//
//                    mCenterLat = latitude;
//                    mCenterLon = longitude;
        } else {
//
            if (mLastLatLng == null) {
                mLastLatLng = new LatLng(mLocatinLat, mLocationLon);
            } else {
                findBest();
            }
        }
    }

    private void setMyStopLoca(final LatLng latlng) {
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 17f));

        if (mMarkMyLocation != null) {
            mMarkMyLocation.destroy();
            mMarkMyLocation = null;
        }

        if (mMarkMyLocation == null) {
            final MarkerOptions markerOptions = new MarkerOptions();
            //markerOptions.snippet(dogId);
            // 设置Marker点击之后显示的标题R.drawable.local2R.mipmap.location
            //markerOptions.setFlat(false);
            markerOptions.anchor(0.5f, 0.7f);
            markerOptions.zIndex(25);
            markerOptions.zIndex(90);
            ImageView iv = new ImageView(this);
            FrameLayout.LayoutParams fmIv = new FrameLayout.LayoutParams(100, 100);
            iv.setImageResource(R.drawable.local2);
            iv.setLayoutParams(fmIv);
            BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromView(iv);
            markerOptions.icon(markerIcon);
            markerOptions.position(latlng);
            mMarkMyLocation = aMap.addMarker(markerOptions);

        } else {
            mMarkMyLocation.setPosition(latlng);
        }
    }


    private Polyline TotalLine;

    private void DrawRideTraceTotal() {
        if (TotalLine != null) {
            TotalLine.remove();
            TotalLine = null;
        }
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(mLocationList);
        polylineOptions.visible(true).width(30).zIndex(200);
//        加入对应的颜色,使用colorValues 即表示使用多颜色，使用color表示使用单色线
        // polylineOptions.colorValues(WalkUtil.getColorList(mLocationList.size()/144+1,this));
        //加上这个属性，表示使用渐变线
//        polylineOptions.useGradient(true);
        TotalLine = aMap.addPolyline(polylineOptions);
    }

    private boolean mOver = false;

    private void findBest() {

        currLa = new LatLng(mLocatinLat, mLocationLon);

        currTime = System.currentTimeMillis();
        LogUtil.d("test walk la is" + currLa + "");
        LogUtil.d("test walk last is" + lastLa + "");
        String move;
//        if(mIsMove){
//            move = "move中";
//        }else {
//            move = "臭不要脸的静止中";
//        }

//        LogUtil.d("yhy 位置来啦，给不给轩哥发？位置是"+ currLa.toString()+"但是"+move);
//        MyToast.showMsg(MainActivity.this, "位置来啦，给不给轩哥发？位置是" + currLa.toString());
        currLength = AMapUtils.calculateLineDistance(
                lastLa, currLa);
        //TODO 传感器检测
//        if(mIsMove){
        if (!lastLa.equals(currLa)) {
//            LogUtil.d("yhy 发给轩哥了");
//
            minusTime = currTime - lastTime;
//            if(minusTime>=20000&&!firstOverTime){
//                firstOverTime = true;
//                overTimeLat = currLa;
////                        if(overLength>((minusTime+1)/1000)*5){
////                            mOver = true;
////                        }
//            }else if(minusTime>=20000){
//                overLength = AMapUtils.calculateLineDistance(
//                        overTimeLat, currLa);
//                if(overLength<=20){
//                    mOver = true;
//                }else {
//                    lastTime = currTime;
//                }
//                firstOverTime = false;
//            }

            LogUtil.d("yhy time testzzz" + minusTime);
//            if(currLength<(errorCnt+1)*5||errorCnt>=20){
            if (currLength < ((minusTime + 1) / 1000) * 5) {
//                if(currLength<((minusTime+1)/1000)*5||mOver){
                errorCnt = 0;
                lastLa = currLa;
                lastTime = currTime;

                mBestLat = mLocatinLat;
                mBestLon = mLocationLon;

                mCurrentLatLng = new LatLng(mBestLat, mBestLon);
                LogUtil.d("yhy 发给轩哥了>>>>>>>>>" + currLength);
                mLocationList.add(mCurrentLatLng);
                mMarkMyLocation.setPosition(mCurrentLatLng);
                DrawRideTraceTotal();
//                lastTime = currTime;
//                    everyTime = currTime;
            } else if (minusTime >= 20000) {

                if (mOver) {

                    if (!overLa.equals(currLa)) {
//                   if(mOverCnt!=mLocationCnt){
                        errorCnt = 0;
                        lastLa = currLa;
                        lastTime = currTime;
                        LogUtil.d("yhy 确定大于距离，发给轩哥>>>>>>>>>" + currLength);
//                            Toast.makeText(MainActivity.this,"确定大于距离，发给轩哥>>>>>>>>>" + currLength,Toast.LENGTH_SHORT).show();


                        mBestLat = mLocatinLat;
                        mBestLon = mLocationLon;

                        mCurrentLatLng = new LatLng(mBestLat, mBestLon);
                        mLocationList.add(mCurrentLatLng);
                        mMarkMyLocation.setPosition(mCurrentLatLng);
                        DrawRideTraceTotal();
//                            lastTime = currTime;
//                            everyTime = currTime;
                        mOver = false;
                    } else {
                        errorCnt = 0;
//                       lastLa = currLa;
                        lastTime = currTime;
                        mOver = false;
                    }
                } else {
                    if (currLength > ((minusTime + 1) / 1000) * 5) {
                        mOver = true;
                        overLa = currLa;
                        LogUtil.d("yhy 第一次大于距离" + currLength);
//                            mOverCnt = mLocationCnt;
                    }
                }


            } else {
                errorCnt++;
                LogUtil.d("yhy +++++++++++++++++++++++++++++++++++++++++++++距离太大，是漂移，不发" + currLength + "定位是" + mLocatinLat + "^^^" + mLocationLon);
//                    Toast.makeText(MainActivity.this,"++++距离太大，是漂移，不发"+currLength,Toast.LENGTH_SHORT).show();
//                everyTime = currTime;
//                lastLa = currLa;
//                lastTime = currTime;
//                  currLa = lastLa;
            }

//            LogUtil.d("test walk last change is"+lastLa+"");


        } else {
            LogUtil.d("yhy -------------------------------------------------距离太小，没有移动，不发");
//                Toast.makeText(MainActivity.this,"------距离太小，没有移动，不发",Toast.LENGTH_SHORT).show();
            lastTime = currTime;
        }
//        }

    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent1 = new Intent(DrawTraceActivity.this, Map_Service.class);
        intent1.putExtra("isStart", 0);    //是否开始跑步
        startService(intent1);
    }

    /**
     * 获取服务对象
     */
    public ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Map_Service.My_binder binder = (Map_Service.My_binder) service;
            map_Service = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            map_Service.onDestroy();
        }
    };

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (null != locationClient) {
            locationClient.onDestroy();
        }
    }

    @Override
    public void onReaderPedometer(PedometerCardEntity cardEntity) {
        if (cardEntity != null) {
            //获取实时步数
            tvSteps.setText(cardEntity.getStepCount() + "步");
            //设置目标步数
            cardEntity.setTargetStepCount(10);
            //获取目标步数
            tvTargetSteps.setText("目标步数：" + cardEntity.getTargetStepCount() + "步");
            //当前进度
            //mProgress = (int) (100 * cardEntity.getStepCount() / (cardEntity.getTargetStepCount() * 1.0f));
           /*
            //进度条动画
            RotateAnimation rotateAnimation = AnimationUtils.initRotateAnimation(false, 1500, true,
                    Animation.INFINITE);
            fanPic.startAnimation(rotateAnimation);
            leafLoading.setProgress(mProgress);
            */
            Log.i("计步", cardEntity.getDate() + "--" + cardEntity.getDescription() + "--"
                    + cardEntity.getCalories() + "--" + cardEntity.getDistanceInMeters() + "--"
                    + cardEntity.getStatus() + "--" + cardEntity.getStepCount() + "--" + cardEntity.getId());
        }
    }

    @OnClick({R.id.back, R.id.sportTime, R.id.btn_start, R.id.sportType, R.id.btn_goto,R.id.share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.sportTime:

                break;
            case R.id.btn_start:                  //倒计时后  开始 结束 的按钮
                if (!mIsStart) {
                    mIsStart = true;
                    ApplicationModule.getInstance().getPedometerManager().startPedometerService();
                    //开始计时
                    sportTime.start();
                    btnStart.setText("停止");
                    //开始动画，风扇动画
                    viewWindmill.stopAnim();

                    Intent intent1 = new Intent(DrawTraceActivity.this, Map_Service.class);
                    intent1.putExtra("isStart", 1);    //是否开始跑步
                    bindService(intent1, connection, Context.BIND_AUTO_CREATE);

                    mCache.put("state", "1");

                } else {
                    mIsStart = false;
                    ApplicationModule.getInstance().getPedometerManager().stopPedometerService();
                    //停止计时
                    sportTime.stop();
                    btnStart.setText("开始");
                    //开始动画，风扇动画
                    viewWindmill.startAnim();
                    mCache.put("state", "0");
                }
                break;
            case R.id.sportType:                               //跑步模式选择
                Intent intent = new Intent(DrawTraceActivity.this, SportStyleActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_goto:                               //倒计时前的开始按钮
                startView.setVisibility(View.GONE);
                timeView.setVisibility(View.VISIBLE);
                setTime();
                Intent intent1 = new Intent(DrawTraceActivity.this, Map_Service.class);
                intent1.putExtra("isStart", 1);    //是否开始跑步
                bindService(intent1, connection, Context.BIND_AUTO_CREATE);
                mCache.put("state", "1");
                break;
            case R.id.share:
                String media = GetandSaveCurrentImage();
                //mListener.shareSport("IPO时刻为您的健康服务", "爱运动爱生活", "http://112.74.28.179:8080/Weiaibenpao/Image/test6.png", "http://112.74.28.179:8080/Weiaibenpao/video/qms006.mp4");
                Log.i("截屏",media);
               /*目前支持文本、图片（本地及URL）、音频及视频URL的分享,要分享图片的URL*/
                UMImage image = new UMImage(DrawTraceActivity.this, media);
                final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
                        {
                                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.SINA,
                                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
                        };
                new ShareAction(this).setDisplayList( displaylist )
                        .withText( "------------" )
                        .withTitle("++++++++++")
                        .withTargetUrl(url)
                        .withMedia(image)
                        .setListenerList(umShareListener)
                        .open();
                break;
        }
    }

    /**
     * 获取和保存当前屏幕的截图
     */
    private String GetandSaveCurrentImage()
    {
        String filepath = null;
        //构建Bitmap
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int w = display.getWidth();
        int h = display.getHeight();
        Bitmap Bmp = Bitmap.createBitmap( w, h, Bitmap.Config.ARGB_8888 );
        //获取屏幕
        View decorview = this.getWindow().getDecorView();
        decorview.setDrawingCacheEnabled(true);
        Bmp = decorview.getDrawingCache();
        //图片存储路径
        String SavePath = getSDCardPath()+"/Demo/ScreenImages";
        //保存Bitmap
        try {
            File path = new File(SavePath);
            //文件
            filepath = SavePath + "/Screen_1.png";
            File file = new File(filepath);
            if(!path.exists()){
                path.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = null;
            fos = new FileOutputStream(file);
            if (null != fos) {
                Bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
                Toast.makeText(context, "截屏文件已保存至SDCard/ScreenImages/目录下",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filepath;
    }


    /**
     * 获取SDCard的目录路径功能
     * @return
     */
    private String getSDCardPath(){
        File sdcardDir = null;
          //判断SDCard是否存在
        boolean sdcardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if(sdcardExist){
            sdcardDir = Environment.getExternalStorageDirectory();
        }
        return sdcardDir.toString();
    }


    //三秒倒计时
    public void setTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 3; i >= -1; i--) {
                    Message msg = Message.obtain();
                    try {
                        Thread.sleep(1000);
                        msg.what = 0;
                        msg.arg1 = i;
                        hander.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    myTime.setText(msg.arg1 + "");
                    if (msg.arg1 == 0) {
                        myTime.setText("go!");
                    } else if (msg.arg1 == -1) {
                        timeView.setVisibility(View.GONE);
                        showView.setVisibility(View.VISIBLE);
                        mIsStart = true;
                        ApplicationModule.getInstance().getPedometerManager().startPedometerService();
                        btnStart.setText("停止");

                        // 将计时器清零
                        sportTime.setBase(SystemClock.elapsedRealtime());
                        //开始计时
                        sportTime.start();
                    }
                    break;
            }
        }
    };

    private void init() {
        mPedometerPresenter = new PedometerPresenter(this);
        mPedometerPresenter.resume();
        if (HardwarePedometerUtil.supportsHardwareAccelerometer(this)) {                           //判断是否支持Accelerometer算法

        } else {

        }
        if (HardwarePedometerUtil.supportsHardwareStepCounter(this)) {                             //判断是否支持Step_counter算法

        } else {

        }

        //开始动画，风扇动画
        //viewWindmill.startAnim();
    }

    /**
     * 内部类实现进度条和界面动态显示
     */
    public class MyBroadcast extends BroadcastReceiver {

        public MyBroadcast() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            LatLng latLng = (LatLng) intent.getParcelableExtra("latlong");

            mLocatinLat = latLng.latitude;
            mLocationLon = latLng.longitude;                          //当前定位
            mLocationList = intent.getParcelableArrayListExtra("mLocationList");     //当前运动过路线
            dis.setText((int) intent.getFloatExtra("dis", 0) + " ");   //当前运动近距离
            Log.i("轨迹", latLng.toString() + "---------" + intent.getFloatExtra("dis", 0) + "---------" + state);
            getLatLong();
        }
    }

    public void initStyleView() {
        /**
         *  0  普通模式
         *
         *  10 十分钟跑步
         *  20 二十分钟跑步
         *  30 三十分钟跑步
         *  60 一小时跑步
         *  120 两小时跑步
         *
         *  1 一公里跑步
         *  2 两公里跑步
         *  3 三公里跑步
         *  5 五公里跑步
         *
         *  500 21.0975半程马拉松
         *  600 42.195全程马拉松
         *
         *  1000 1000步跑步
         *
         *  20000  20000步跑步
         */
        Intent intent = getIntent();
        disStyle = intent.getIntExtra("disNum", 0);
        Log.i("测试", disStyle + " ");
        if (disStyle == 0) {
            sportType.setText("模式:普通模式        >");
        } else if (disStyle > 0 && disStyle < 10) {
            sportType.setText("模式:" + disStyle + "公里模式        >");
        } else if (disStyle >= 10 && disStyle < 150) {
            sportType.setText("模式:" + disStyle + "分钟模式        >");
        } else if (disStyle == 500) {
            sportType.setText("模式:" + "21.0975半程马拉松        >");
        } else if (disStyle == 600) {
            sportType.setText("模式:" + "42.195全程马拉松        >");
        } else if (disStyle >= 1000 && disStyle < 50000) {
            sportType.setText("模式:" + disStyle + "步模式        >");
        }
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
            Toast.makeText(DrawTraceActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
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
                new ShareAction(DrawTraceActivity.this).setPlatform(share_media).setCallback(umShareListener)
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

}
