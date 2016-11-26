package com.weiaibenpao.demo.chislim.ui;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.music.SendData.SendDataListener;
import com.weiaibenpao.demo.chislim.music.adapter.MyMusicAdapter;
import com.weiaibenpao.demo.chislim.music.bean.Bean_music;
import com.weiaibenpao.demo.chislim.music.dao.Dao_Get_music;
import com.weiaibenpao.demo.chislim.music.service.Start_Service;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MusicListActivity extends AppCompatActivity implements SendDataListener {

    View view;
    @BindView(R.id.back)
    ImageView back;
    //本地歌曲列表
    private ListView localMusicListView;
    //获取本地歌曲数据源
    private Dao_Get_music get_music;
    //用来存放本地歌曲
    private ArrayList localMusicList;
    //定义适配器
    private MyMusicAdapter localMusicAdapter;

    //底部正在播放歌曲名和图片
    private View myMusicShow;
    private ImageView myMusicImage;
    private TextView musicNameTV;
    private TextView singerNameTV;
    private ImageView upmusicIV;
    private ImageView startIV;
    private ImageView nextIV;

    //服务
    private Intent intentservice;
    private Start_Service start_service;  //服务对象


    MyBroadcast myBroadcast;  //广播对象
    int pross = 0;     //当前播放进度
    private int postion;  //当前播放歌曲的序列号

    //当前播放歌曲对象
    private Bean_music music;

    Context context;

    boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        ButterKnife.bind(this);
        context = getApplicationContext();

        initView(view);

        //配置广播
        myBroadcast = new MyBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.lijianbao.cn");
        registerReceiver(myBroadcast, filter);

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new MyPhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);
    }

    public void initView(View view) {
        initListView();
        initBottobLayout();
    }

    /**
     * 实例化ListView控件
     */
    public void initListView() {

        localMusicListView = (ListView) findViewById(R.id.localMusicListView);

        music = new Bean_music();

        get_music = new Dao_Get_music(context);
        localMusicList = new ArrayList();
        localMusicList = get_music.getMusic_Array();

        localMusicAdapter = new MyMusicAdapter(context, localMusicList);

        localMusicListView.setAdapter(localMusicAdapter);
        localMusicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                start_service.play(0, (Bean_music) localMusicList.get(i), i);
            }
        });
    }


    /**
     * 启动服务,向服务中传本地歌曲localMusicList
     */
    @Override
    public void onStart() {
        super.onStart();

        intentservice = new Intent(MusicListActivity.this, Start_Service.class);
        Bundle bundle = new Bundle();
        //getFraMusic();   //获取本地歌曲数据源
        bundle.putParcelableArrayList("music", localMusicList);
        intentservice.putExtras(bundle);

        startService(intentservice);
        bundle.putInt("postion", 0);
        getApplicationContext().bindService(intentservice, connection, Context.BIND_AUTO_CREATE);

        Log.i("服务", "服务" + "-----------------");
    }


    /**
     * 获取服务对象
     */
    public ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Start_Service.My_binder binder = (Start_Service.My_binder) service;
            start_service = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            start_service.onDestroy();
        }
    };


    /**
     * 实例化底部图片和歌名布局
     * 获取对应歌曲图片及歌曲信息
     * 点击歌曲图片及歌曲信息跳转至歌曲播放界面
     */
    public void initBottobLayout() {
        //底部图片
        myMusicImage = (ImageView) findViewById(R.id.myMusicImage);

        //歌曲名称
        musicNameTV = (TextView) findViewById(R.id.musicNameTV);

        //作者名称
        singerNameTV = (TextView) findViewById(R.id.singerNameTV);

        //点击底部左侧布局是进入歌曲播放界面
        myMusicShow = findViewById(R.id.myMusicShow);
        myMusicShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MusicListActivity.this, MusicActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("localMusicList", localMusicList);
                startActivity(intent);
            }
        });


        //上一曲
        upmusicIV = (ImageView) findViewById(R.id.upmusicIV);
        upmusicIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_service.up();
            }
        });

        //播放暂停
        startIV = (ImageView) findViewById(R.id.startIV);
        startIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (start_service.getState() == true) {
                    start_service.pause();
                } else {
                    start_service.play(pross, (Bean_music) localMusicList.get(postion), postion);
                }
            }
        });

        //下一曲
        nextIV = (ImageView) findViewById(R.id.nextIV);
        nextIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_service.next();
            }
        });
    }

    @Override
    public void sendPos(int position) {
        this.postion = position;
        initBottomView();
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }

    /**
     * 底部布局显示内容
     */
    public class MyBroadcast extends BroadcastReceiver {

        public MyBroadcast() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            //获取当前播放歌曲序列号
            postion = intent.getIntExtra("position", 0);
            //获取当前实时进度
            pross = intent.getIntExtra("pross", 0);
            music = intent.getParcelableExtra("musicBean");

            //返回当前播放状态
            flag = intent.getBooleanExtra("isPlaying", false);
            initBottomView();
        }
    }

    /**
     * 底部歌曲信息更新
     */
    public void initBottomView() {
        Picasso.with(context)
                .load(R.mipmap.musicpic1)
                .into(myMusicImage);
        musicNameTV.setText(((Bean_music) localMusicList.get(postion)).getMusic_title());
        singerNameTV.setText(((Bean_music) localMusicList.get(postion)).getMusic_singer());

        if (flag) {
            Picasso.with(this)
                    //      .load(music.getSong_image())
                    .load(R.mipmap.stop)
                    .placeholder(R.mipmap.stop)
                    .error(R.mipmap.stop)
                    .into(startIV);
        } else {
            Picasso.with(this)
                    //      .load(music.getSong_image())
                    .load(R.mipmap.start)
                    .placeholder(R.mipmap.start)
                    .error(R.mipmap.start)
                    .into(startIV);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopService(intentservice);
    }

    /**
     * 只有电话来了之后才暂停音乐的播放
     */
    private final class MyPhoneListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING://电话来了
                    start_service.pause();
                    break;
                case TelephonyManager.CALL_STATE_IDLE: //通话结束
                    //player.callIsDown();
                    break;
            }
        }
    }
}
