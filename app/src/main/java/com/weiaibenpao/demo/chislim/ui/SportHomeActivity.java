package com.weiaibenpao.demo.chislim.ui;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.bean.BluResult;
import com.weiaibenpao.demo.chislim.bean.Blutetooth;
import com.weiaibenpao.demo.chislim.bean.SportTypeBean;
import com.weiaibenpao.demo.chislim.bean.UserBean;
import com.weiaibenpao.demo.chislim.ble.adapter.DeviceAdapter;
import com.weiaibenpao.demo.chislim.ble.entity.EntityDevice;
import com.weiaibenpao.demo.chislim.ble.service.BLEService;
import com.weiaibenpao.demo.chislim.ble.utils.BluetoothController;
import com.weiaibenpao.demo.chislim.ble.utils.ConstantUtils;
import com.weiaibenpao.demo.chislim.ble.utils.Util;
import com.weiaibenpao.demo.chislim.util.BluDataUtil;
import com.weiaibenpao.demo.chislim.util.Default;
import com.weiaibenpao.demo.chislim.util.GetIntentData;
import com.weiaibenpao.demo.chislim.util.RippleView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SportHomeActivity extends Activity {

    @BindView(R.id.stateText)
    TextView stateText;
    @BindView(R.id.stateImg)
    ImageView stateImg;
    @BindView(R.id.xiaoqiu)
    RelativeLayout xiaoqiu;
    @BindView(R.id.mySpeedAdd)
    ImageView mySpeedAdd;
    @BindView(R.id.speedText)
    TextView speedText;
    @BindView(R.id.mySpeedMinu)
    ImageView mySpeedMinu;
    @BindView(R.id.speedLayout)
    LinearLayout speedLayout;
    @BindView(R.id.mySlopeAdd)
    ImageView mySlopeAdd;
    @BindView(R.id.slopeText)
    TextView slopeText;
    @BindView(R.id.mySlopeMinu)
    ImageView mySlopeMinu;
    @BindView(R.id.slopeLayout)
    LinearLayout slopeLayout;

    @BindView(R.id.root_rv)
    RippleView rootRv;
    @BindView(R.id.root_tv)
    TextView rootTv;

    BluetoothAdapter mAdapter;             //蓝牙适配器
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.dis)
    TextView dis;
    @BindView(R.id.cor)
    TextView cor;
    @BindView(R.id.heart)
    TextView heart;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.myMode)
    TextView myMode;
    @BindView(R.id.speedAll)
    TextView speedAll;
    private PopupWindow mPopupWindow;
    private ListView listView;
    private Blutetooth blutetooth;
    private static final String TAG = "蓝牙";
    private ArrayList bluList;
    private ArrayList<EntityDevice> list = new ArrayList<EntityDevice>();
    Intent intentService;
    DeviceAdapter deviceAdapter;
    private MsgReceiver receiver;
    BluetoothController controller = BluetoothController.getInstance();
    BluResult bluResult;
    Util u = new Util();
    BluDataUtil bluDataUtil = new BluDataUtil();
    Context context;

    public int bluState = 0;
    public int sportDis;   //运动里程
    public int sportTime;   //运动时间
    public int sportCor;    //消耗卡路里

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_home);
        ButterKnife.bind(this);
        context = getApplicationContext();

        initView();
        initReceiver2();          //蓝牙模块状态
        initRipple();
        initService();
        initData();
        registerReceiver();
        MyConTrol(0);
    }

    public void initView(){
        Intent intent = getIntent();
        SportTypeBean sportTypeBean = intent.getParcelableExtra("sportTypeBean");
        stateText.setText(sportTypeBean.getTypeIntro());
    }
    //注册广播，用于监听蓝牙状态
    public void initReceiver2() {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bluetoothState, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(bluetoothState, filter);
    }

    //接收蓝牙模块状态广播
    BroadcastReceiver bluetoothState = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String stateExtra = BluetoothAdapter.EXTRA_STATE;
            int state = intent.getIntExtra(stateExtra, -1);
            switch (state) {
                case BluetoothAdapter.STATE_TURNING_ON:
                    Log.i("蓝牙", "蓝牙模块正在打开");
                    break;
                case BluetoothAdapter.STATE_ON:
                    Log.i("蓝牙", "蓝牙已打开");

                    list.clear();//清空以前发现的蓝牙
                    deviceAdapter.notifyDataSetChanged();
                    if (!BluetoothController.getInstance().initBLE()) {///手机不支持蓝牙
                        Toast.makeText(SportHomeActivity.this, "您的手机不支持蓝牙",
                                Toast.LENGTH_SHORT).show();
                        return;//手机不支持蓝牙就啥也不用干了，关电脑睡觉去吧
                    }
                    new GetDataTask().execute();// 搜索任务

                    //-------------------------------------------------------------------------
                    //Log.i("蓝牙",device.toString());
                    start();                     //水波纹启动,运行一下
                    MyConTrol(1);
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    Log.i("蓝牙", "蓝牙模块正在关闭");
                    stop();
                    break;
                case BluetoothAdapter.STATE_OFF:
                    Log.i("蓝牙", "蓝牙模块处于关闭状态");
                    stop();
                    break;
            }
        }
    };

    //修改app界面水波球上的文字
    public void upTextStayle(String str1, String str2) {
        rootTv.setText(str1);
    }


    //实例化水波球
    public void initRipple() {

/** 设置空心内圆颜色 */
        rootRv.setInStrokePaintColor(getResources().getColor(R.color.colorAccent));
        /** 设置实心内圆颜色 */
        rootRv.setInPaintColor(getResources().getColor(R.color.colorAccent));
        /** 设置空心外圆颜色 */
        rootRv.setOutStrokePaintColor(getResources().getColor(R.color.colorAccent));

        //设置动画次数
        rootRv.setRepeatCount(20);

        //设置动画时间
        rootRv.setDuration(3000);

        //水波纹状态监听
        rootRv.setRippleStateListener(new RippleView.RippleStateListener() {
            @Override
            public void startRipple() {

            }

            @Override
            public void stopRipple() {

            }

            @Override
            public void onRippleUpdate(ValueAnimator animation) {

            }
        });
    }


    //开始动画
    public void start() {
        rootRv.startRipple();
    }

    //结束动画
    public void stop() {
        rootRv.stopRipple();
    }

    /**
     * 0--9 有坡度的跑步机
     * 10--19  没坡度的跑步机
     *
     * @param stateNum
     */
    public void MyConTrol(int stateNum) {
        switch (stateNum) {
            case 0:                                           //蓝牙未开启状态
                speedLayout.setVisibility(View.GONE);
                slopeLayout.setVisibility(View.GONE);
                stateImg.setBackgroundColor(getResources().getColor(R.color.red_bg));
                break;
            case 1:                                           //蓝牙未连接状态
                speedLayout.setVisibility(View.GONE);
                slopeLayout.setVisibility(View.GONE);
                stateImg.setBackgroundColor(getResources().getColor(R.color.yellow_bg));
                break;
            case 2:                                           //蓝牙连接成功状态
                speedLayout.setVisibility(View.VISIBLE);
                slopeLayout.setVisibility(View.VISIBLE);
                stateImg.setBackgroundColor(getResources().getColor(R.color.green_bg));
                break;
            case 3:
                break;
            case 4:
                break;
            case 10:
                speedLayout.setVisibility(View.GONE);
                slopeLayout.setVisibility(View.GONE);
                break;
            case 11:
                break;
            case 12:
                break;
            case 13:
                break;
            case 14:
                Log.i("蓝牙", "连接成功！");
                break;
        }
    }

    /**
     * 更新界面
     */
    public void changeActivity(BluResult bluResult) {
        speedText.setText(bluResult.speed + "");
        slopeText.setText(bluResult.slope + "");
        time.setText(bluResult.time + "");
        cor.setText(bluResult.calories + "");
        heart.setText(bluResult.heart + "");
        dis.setText(bluResult.distance + "");
        if (bluState == 20) {
            upTextStayle("暂停", "蓝牙已连接");
        } else if (bluState == 10) {
            upTextStayle("继续", "蓝牙已连接");
        }
        MyConTrol(2);
        stop();
    }


    /**
     * 显示蓝牙搜索结果
     *
     * @param parent
     */
    private void showPopwindow(View parent, final ArrayList bluList) {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;

        if (mPopupWindow == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(this);
            View contentView = mLayoutInflater.inflate(R.layout.group_list, null);
            listView = (ListView) contentView.findViewById(R.id.lv_group);

            deviceAdapter = new DeviceAdapter(this, bluList);
            listView.setAdapter(deviceAdapter);

            mPopupWindow = new PopupWindow(contentView, screenWidth / 1, screenHeight / 3);
        }
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);  //设置点击屏幕其它地方弹出框消失
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());

        // 显示的位置为:屏幕的宽度的1/16
        // mPopupWindow.showAsDropDown(parent, screenWidth / 1, screenHeight/2);
        mPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //蓝牙设备选择连接，点击列表中的蓝牙名称，连接对应设备
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                if (BluetoothController.reBtGatt() == null) {
                    //连接设备
                    BluetoothController.getInstance().connect((EntityDevice) bluList.get(position));
                } else {
                    BluetoothController.clearBtGatt();
                    BluetoothController.getInstance().connect((EntityDevice) bluList.get(position));
                }

                start();
                upTextStayle("蓝牙正在连接", "蓝牙正在连接");
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
            }
        });
    }

    @OnClick({R.id.root_rv, R.id.root_tv, R.id.mySpeedAdd, R.id.mySpeedMinu, R.id.mySlopeAdd, R.id.mySlopeMinu,R.id.back, R.id.myMode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.root_rv:
                break;
            case R.id.root_tv:

                if (mAdapter == null) {
                    mAdapter = BluetoothAdapter.getDefaultAdapter();
                }
                if (!mAdapter.isEnabled()) {                       //判断蓝牙是否打开
                    initReceiver2();
                    //弹出对话框提示用户是后打开
                    Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enabler, Default.FIND);
                    //不做提示，强行打开
                    // mAdapter.enable();
                }

                if (mAdapter.isEnabled() && bluState == 0) {                //判断蓝牙是否连接
                    list.clear();//清空以前发现的蓝牙
                    deviceAdapter.notifyDataSetChanged();
                    if (!BluetoothController.getInstance().initBLE()) {///手机不支持蓝牙
                        Toast.makeText(SportHomeActivity.this, "您的手机不支持蓝牙",
                                Toast.LENGTH_SHORT).show();
                        return;//手机不支持蓝牙就啥也不用干了，关电脑睡觉去吧
                    }
                    new GetDataTask().execute();// 搜索任务

                    //-------------------------------------------------------------------------
                    //Log.i("蓝牙",device.toString());
                    start();                     //水波纹启动,运行一下
                }
                if (bluState == 10) {    //正在运转
                    controller.write(bluDataUtil.startStop("1"));     //启动

                } else if (bluState == 20) {    //正在停止
                    controller.write(bluDataUtil.startStop("0"));         //停止
                    //controller.write(bluDataUtil.addSpeed("0.0"));         //暂停
                }
                break;
            case R.id.mySpeedAdd:
                Log.i(TAG, "速度增加");
                float speed1 = Float.parseFloat(speedText.getText().toString());
                if (speed1 <= 17) {
                    speed1 = speed1 + 1;
                } else {
                    speed1 = (float) 18.0;
                }
                controller.write(bluDataUtil.addSpeed(speed1 + ""));
                break;
            case R.id.mySpeedMinu:
                Log.i(TAG, "速度减少");
                float speed2 = Float.parseFloat(speedText.getText().toString());
                if (speed2 >= 1) {
                    speed2 = speed2 - 1;
                } else {
                    speed2 = (float) 0.0;
                }
                controller.write(bluDataUtil.addSpeed(speed2 + ""));
                break;
            case R.id.mySlopeAdd:
                Log.i(TAG, "坡度增加");
                int slope1 = Integer.parseInt(slopeText.getText().toString());
                if (slope1 < 15) {
                    slope1 = slope1 + 1;
                } else {
                    slope1 = 15;
                }
                controller.write(bluDataUtil.addSlope(slope1 + "")); //坡度增加
                break;
            case R.id.mySlopeMinu:
                Log.i(TAG, "坡度减少");
                int slope2 = Integer.parseInt(slopeText.getText().toString());
                if (slope2 > 1) {
                    slope2 = slope2 - 1;
                } else {
                    slope2 = 0;
                }
                controller.write(bluDataUtil.addSlope(slope2 + ""));   //坡度减少
                break;
            case R.id.back:
                finish();
                break;
            case R.id.myMode:
                Intent intent = new Intent(SportHomeActivity.this,SportHomeTypeActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void initData() {
        bluList = new ArrayList();
        deviceAdapter = new DeviceAdapter(this, list);
    }

    /**
     * 实例化服务
     */
    private void initService() {
        //开始服务
        intentService = new Intent(SportHomeActivity.this, BLEService.class);
        startService(intentService);
        //初始化蓝牙
        BluetoothController.getInstance().initBLE();
    }

    /**
     * 注册广播
     */
    private void registerReceiver() {
        receiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantUtils.ACTION_UPDATE_DEVICE_LIST);
        intentFilter.addAction(ConstantUtils.ACTION_CONNECTED_ONE_DEVICE);
        intentFilter.addAction(ConstantUtils.ACTION_RECEIVE_MESSAGE_FROM_DEVICE);
        intentFilter.addAction(ConstantUtils.ACTION_STOP_CONNECT);
        registerReceiver(receiver, intentFilter);
    }


    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            if (BluetoothController.getInstance().isBleOpen()) {
                BluetoothController.getInstance().startScanBLE();
            }
            ;// 开始扫描
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
        }
    }

    /**
     * 接收广播，接收蓝牙模块发过来的值
     */
    public class MsgReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(
                    ConstantUtils.ACTION_UPDATE_DEVICE_LIST)) {
                String name = intent.getStringExtra("name");
                String address = intent.getStringExtra("address");
                boolean found = false;                                 //记录该条记录是否已在list中，
                for (EntityDevice device : list) {
                    if (device.getAddress().equals(address)) {
                        found = true;
                        break;
                    }
                }// for
                if (!found) {                                                       //发现蓝牙
                    EntityDevice temp = new EntityDevice();
                    temp.setName(name);
                    temp.setAddress(address);
                    list.add(temp);
                    showPopwindow(getWindow().getDecorView(), list);              //显示蓝牙列表
                    stop();
                    deviceAdapter.notifyDataSetChanged();
                }
            } else if (intent.getAction().equalsIgnoreCase(ConstantUtils.ACTION_CONNECTED_ONE_DEVICE)) {
                // connectedDevice.setText("连接的蓝牙是："+intent.getStringExtra("address"));
            } else if (intent.getAction().equalsIgnoreCase(ConstantUtils.ACTION_STOP_CONNECT)) {
                // connectedDevice.setText("");
                toast("连接已断开");
            } else if (intent.getAction().equalsIgnoreCase(ConstantUtils.ACTION_RECEIVE_MESSAGE_FROM_DEVICE)) {
                //receivedMessage.append(u.toStringHex(intent.getStringExtra("message")).trim());
                //Log.i("蓝牙数据",u.toStringHex(intent.getStringExtra("message")).trim());
                bluResult = bluDataUtil.SplitStr(u.toStringHex(intent.getStringExtra("message")).trim());
                if (bluResult != null) {
                    if (bluResult.code.equals("0")) {
                        bluState = Integer.parseInt(bluResult.state);
                        sportDis = Integer.parseInt(bluResult.distance);
                        sportCor = Integer.parseInt(bluResult.calories);
                        sportTime = Integer.parseInt(bluResult.time);
                        changeActivity(bluResult);
                    }
                }
            }
        }
    }

    private void toast(String str) {
        Toast.makeText(SportHomeActivity.this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GetIntentData intentData = new GetIntentData();
        intentData.GetSportData(context,"addOne",UserBean.getUserBean().userId,"2016.09.01",sportDis,sportCor,sportTime,"钻石",55);
    }

    /**
     * 销毁
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intentService);
        unregisterReceiver(receiver);
    }
}
