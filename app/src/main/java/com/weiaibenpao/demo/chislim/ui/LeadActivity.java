package com.weiaibenpao.demo.chislim.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.bean.UserBean;


public class LeadActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "UserInfo";
    private SharedPreferences settings;
    //用户信息
    UserBean user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);



        settings = getSharedPreferences(PREFS_NAME, 0);

        Handler x = new Handler();//定义一个handle对象

        x.postDelayed(new splashhandler(),2000);//设置3秒钟延迟执行splashhandler线程。其实你这里可以再新建一个线程去执行初始化工作，如判断SD,网络状态等

        user = UserBean.getUserBean();


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


    class splashhandler implements Runnable{
        public void run() {
            getUser();
            /*startActivity(new Intent(getApplication(),MainActivity.class));// 这个线程的作用3秒后就是进入到你的主界面
            LeadActivity.this.finish();// 把当前的LaunchActivity结束掉*/
        }
    }


    public void getUser(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                //如果文件中有用户数据保存，直接进入首页
                if(settings.getBoolean("content",false)){
                    //从SharedPreferences拿到数据，第一个参数为键，第二个为默认替换值
                    user.userId = settings.getInt("userId",0);
                    user.userName = settings.getString("userName","未登录");
                    user.userPhoone = settings.getString("userPhone","");
                    user.userBirth = settings.getString("userBirth","");
                    user.userSex = settings.getString("userSex","");
                    user.userEmail = settings.getString("userEmail","");
                    user.userImage = settings.getString("userImage","");
                    user.userTntru = settings.getString("userIntru","");
                    user.userBmi = settings.getInt("userBmi",0);
                    user.userHeigh = settings.getInt("userHeigh",0);
                    user.userWeight = settings.getFloat("userWeight",0);
                    user.userProject = settings.getInt("userProject",0);
                    user.userMark = settings.getInt("userMark",0);
                    user.userHobby = settings.getString("userHobby","");

                    Message msg = new Message();

                    msg.arg1 = 1;

                    handler.sendMessage(msg);

                }else{
                    Message msg = new Message();

                    msg.arg1 = 0;

                    handler.sendMessage(msg);

                }
            }
        }).start();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Intent intent;
           switch (msg.arg1){
               case 1:                  //跳转至首页
                   intent = new Intent(LeadActivity.this,MainActivity.class);
                   startActivity(intent);
                   LeadActivity.this.finish();
                   break;
               case 0:                 //跳转至登录界面
                   intent = new Intent(LeadActivity.this,LoginActivity.class);
                   startActivity(intent);
                   LeadActivity.this.finish();
                   break;
           }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    /**
     * 6.0的权限监测
     */
    /*public void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode,grantResults);
    }


    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
            } else {
                // Permission Denied
            }
        }
    }*/
}
