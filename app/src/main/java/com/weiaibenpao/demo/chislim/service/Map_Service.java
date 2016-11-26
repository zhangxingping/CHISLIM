package com.weiaibenpao.demo.chislim.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.model.LatLng;
import com.weiaibenpao.demo.chislim.sportoutdoor.data.model.database.core.PedometerCardEntity;
import com.weiaibenpao.demo.chislim.sportoutdoor.presentation.view.iview.IPedometerView;

import java.util.ArrayList;

public class Map_Service extends Service implements LocationSource, AMapLocationListener, IPedometerView {

    private static final int LOCATION_TIME_INTERVAL = 4000;
    private static final int HAS_LA_LONG = 0;

    private final IBinder binder = new My_binder();

    private ArrayList<LatLng> mLocationList = new ArrayList<LatLng>();

    private LocationSource.OnLocationChangedListener mLocationLinstener;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    private double mLocatinLat;
    private double mLocationLon;

    private Intent intent1;
    LatLng latLng1 = null;

    private int num,n = 1;
    private float dis = 0;

    public class My_binder extends Binder {
        public Map_Service getService(){
            return Map_Service.this;
        }
    }

    @Override
    public void onCreate() {
        intent1 = new Intent();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        num = intent.getIntExtra("isStart",1);
        if(num == 1){                      //开始跑步
            mLocationList.clear();
        }else if(num == 2){                //暂停

        }else if(num == 3){                 //继续

        }else if(num == 4){                 //停止

        }
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initLocation();
        return super.onStartCommand(intent, flags, startId);
    }

    private void initLocation() {

        locationClient = new AMapLocationClient(getApplicationContext());
        locationOption = new AMapLocationClientOption();

        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        locationOption.setNeedAddress(true);
        // 设置定位监听
        locationClient.setLocationListener(this);
        //每两秒定位一次
        locationOption.setInterval(LOCATION_TIME_INTERVAL);
        locationOption.setOnceLocation(false);
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {

                if (mLocationLinstener != null) {
                    mLocationLinstener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                }
                mLocatinLat = aMapLocation.getLatitude();
                mLocationLon = aMapLocation.getLongitude();

                LatLng latLng = new LatLng(mLocatinLat, mLocationLon);
                mLocationList.add(latLng);
                getLatLong(latLng);

                if(n > 1){
                    dis = dis + AMapUtils.calculateLineDistance(latLng1, new LatLng(mLocatinLat, mLocationLon));

                    Log.i("轨--迹",mLocatinLat + " " +  mLocationLon + "----------" + dis + "---------" + latLng1.toString() + "-----" + num);
                }
                n++;
                latLng1 = new LatLng(mLocatinLat, mLocationLon);

            }
        } else {
            //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
//            LogUtil.d("location Error, ErrCode:"
//                    + aMapLocation.getErrorCode() + ", errInfo:"
//                    + aMapLocation.getErrorInfo());
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mLocationLinstener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        mLocationLinstener = null;
    }

    @Override
    public void onReaderPedometer(PedometerCardEntity cardEntity) {

    }


    public void getLatLong(final LatLng latLng){
        new Thread(new Runnable() {
            @Override
            public void run() {
                        Message message = new Message();
                        message.what = HAS_LA_LONG;
                        message.obj = latLng;
                        handler.sendMessage(message);
            }
        }).start();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HAS_LA_LONG:
                    intent1.putExtra("latlong", (Parcelable) msg.obj);
                    intent1.putParcelableArrayListExtra("mLocationList",mLocationList);
                    intent1.putExtra("dis",dis);
                    intent1.putExtra("state",num);
                    intent1.setAction("com.lijianbao.mapLocationLatLong");
                    sendBroadcast(intent1);
                    break;
            }
        }
    };
}
