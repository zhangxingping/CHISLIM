package com.weiaibenpao.demo.chislim.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.Config;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.bean.UserBean;
import com.weiaibenpao.demo.chislim.bean.UserResult;
import com.weiaibenpao.demo.chislim.model.UserGetByQQModel;
import com.weiaibenpao.demo.chislim.model.UserGetByWboModel;
import com.weiaibenpao.demo.chislim.model.UserGetByWxinModel;
import com.weiaibenpao.demo.chislim.model.UserModel;
import com.weiaibenpao.demo.chislim.util.Default;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.umeng.socialize.bean.SHARE_MEDIA.QQ;


public class LoginActivity extends AppCompatActivity {


    private EditText userNumber;
    private EditText userPass;
    private Button myLogin;
    private TextView registText;
    private TextView findPasss;

    public static final String PREFS_NAME = "UserInfo";
    private SharedPreferences settings;

    private ImageView myImageAnmi;

    //-----------------------------------------------------------
    /**
     * 首先获取UMShareAPI
     */
    UMShareAPI mShareAPI;

    SHARE_MEDIA platform;

    UserBean user = UserBean.getUserBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
       //获取SharedPreferences对象，第一个参数文件名，第二个参数值
        settings = getSharedPreferences(PREFS_NAME, 0);

        //如果文件中有用户数据保存，直接进入首页
       if(settings.getBoolean("content",false)){

            Intent intent = new Intent(LoginActivity.this,MainActivity.class);

            startActivity(intent);

            finish();
        }

        //实例化控件
        initView();

        /**
         * 首先获取UMShareAPI
         */
         mShareAPI = UMShareAPI.get(this);


        //微博的回调地址
        Config.REDIRECT_URL="http://www.chislim.com";

        //第三方登录跳转回登录界面展示动画
        myImageAnmi = (ImageView) findViewById(R.id.myImageAnmi);
    }

    /**
     * 授权监听
     */
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(final SHARE_MEDIA platform, int action, Map<String, String> data) {
            Log.i("授权","------------------succeed----------------");
            /*初始化UMShareAPI，然后进行用户信息获取：*/
            //获取用户信息,
            /**
             * 新浪微博获取用户信息
             */

            /**
             * QQ   和  微信   获取信息
             */
            mShareAPI.getPlatformInfo(LoginActivity.this, platform, new UMAuthListener(){

                @Override
                public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

                   /*Set<String> set = map.keySet();
                        for (String string : set) {
                        Log.i("msg", "=======================Map=========================");
                        Log.i("msg", string + "::::" + map.get(string));
                            Toast.makeText(LoginActivity.this,i + "------------------获取数据成功----------------",Toast.LENGTH_SHORT).show();
                        String str = map.get(string);
                        // 设置头像
                        }*/

                    switch (platform){
                        case QQ:
                            user.userImage = map.get("profile_image_url");
                            user.userName = map.get("screen_name");
                            user.userOpenid = map.get("openid");
                            getUserByQQ(Default.getOneByQQ,map.get("screen_name"),map.get("openid"),map.get("profile_image_url"));
                            break;
                        case SINA:
                            user.userImage = map.get("profile_image_url");
                            user.userName = map.get("screen_name");
                            user.userOpenid = map.get("id");
                            getUserByWbo(Default.getOneByWbo,map.get("screen_name"),map.get("id"),map.get("profile_image_url"));
                            break;
                        case WEIXIN:
                            user.userImage = map.get("profile_image_url");
                            user.userName = map.get("screen_name");
                            user.userOpenid = map.get("openid");
                            getUserByWxin(Default.getOneByWxin,map.get("screen_name"),map.get("openid"),map.get("profile_image_url"));
                            break;
                    }
                }

                @Override
                public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                    Toast.makeText(LoginActivity.this,i + "------------------获取数据失败----------------",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel(SHARE_MEDIA share_media, int i) {
                    Toast.makeText(LoginActivity.this,i + "------------------获取数据取消----------------",Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Log.i("授权",action + "------------------失败----------------");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Log.i("授权","------------------取消----------------");
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

       // mShareAPI.onActivityResult(requestCode, resultCode, data);
        Log.i("第三方登录回调",requestCode + " " + resultCode + " " + data.toString()+"=========");

        myImageAnmi.setBackgroundResource(R.drawable.frame);
        //启动动画
        AnimationDrawable anim = (AnimationDrawable) myImageAnmi.getBackground();
        anim.start();
    }

    public void initView(){

        userNumber = (EditText) findViewById(R.id.userNumber);
        userPass = (EditText) findViewById(R.id.userPass);
        myLogin = (Button) findViewById(R.id.myLogin);
        //登陆监听
        myLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userPhoneStr = userNumber.getText().toString();
                String userPassStr = userPass.getText().toString();
                if(userPhoneStr == "" || userPhoneStr == null){
                    Toast.makeText(LoginActivity.this,"电话号码不能为空",Toast.LENGTH_SHORT).show();
                }else if(userPhoneStr.length() != 11){
                    Toast.makeText(LoginActivity.this,"请输入正确的手机号码",Toast.LENGTH_SHORT).show();
                }
                //调用电话登录
                loginByPhone(userPhoneStr,userPassStr);
            }
        });
        registText = (TextView) findViewById(R.id.registText);
        //前往注册跳转
        registText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegistActivity.class);
                startActivity(intent);
                finish();
            }
        });
        findPasss = (TextView) findViewById(R.id.findPasss);
        //找回密码
        findPasss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,FindPassActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     *通过电话登录
     */
    public void loginByPhone(String userPhone,String userPass){
        this.getUserByPhone(userPhone,userPass);
    }


    /**
     * QQ登录
     * @param v
     */
    public void loginByQq(View v){
        /*获取客户端安装信息*/
        boolean qqBoolean  =  mShareAPI.isInstall(this, QQ);
        /*选取需要授权的平台，并进行授权，其中umAuthLisrener是回调监听器*/
        if(qqBoolean){
            platform = QQ;
            mShareAPI.doOauthVerify(this, platform, umAuthListener);
        }
    }

    /**
     * 微博登录
     * @param v
     */
    public void loginByWbo(View v){
        /*获取客户端安装信息*/
        boolean wbBoolean  =  mShareAPI.isInstall(this, SHARE_MEDIA.SINA);
        /*选取需要授权的平台，并进行授权，其中umAuthLisrener是回调监听器*/
        //if(wbBoolean){
            platform = SHARE_MEDIA.SINA;
            mShareAPI.doOauthVerify(this, platform, umAuthListener);
        //}
    }

    /**
     * 微信登陆
     * @param v
     */
    public void loginByWxin(View v){
        //判断是否安装了客户端
        boolean wxBoolean  =  mShareAPI.isInstall(this, SHARE_MEDIA.WEIXIN);
        /*选取需要授权的平台，并进行授权，其中umAuthLisrener是回调监听器*/
        if(wxBoolean == true){
            platform = SHARE_MEDIA.WEIXIN;
            mShareAPI.doOauthVerify(this, platform, umAuthListener);
        }
    }

    /**
     * 通过电话号码查询服务器端
     */
    public void getUserByPhone(String userPhone,String userPass){

        Call<UserResult> call = UserModel.getModel().getService().getResult(Default.getOneByPhone, userPhone,userPass);

        call.enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                if (response.isSuccessful()) {
                    UserResult result = response.body();
                    if (result.getError() == 0) {

                        List<UserResult.UserBean> userList = result.getUser();
                        UserResult.UserBean user1 = userList.get(0);

                        //向文件中写入数据
                        writeUser(user1);

                        //跳转
                        tiaoZhuan();
                    }else{
                        Toast.makeText(LoginActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 通过QQ号查询服务器
     */
    public void getUserByQQ(String dowhat,String userName,String openId,String userImage){
        Call<UserResult> call = UserGetByQQModel.getModel().getService().getResult(dowhat, userName,openId,"55555",userImage);

        call.enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                if (response.isSuccessful()) {
                    UserResult result = response.body();
                    Log.i("返回的错误码",result.getError()+"----");
                    if (result.getError() == 0) {
                        List<UserResult.UserBean> userList = result.getUser();
                        UserResult.UserBean user1 = userList.get(0);

                        //向文件中写入数据
                        writeUser(user1);
                        //跳转
                        tiaoZhuan();
                    }else{
                        Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 通过微信号查询服务器端
     */
    public void getUserByWxin(String dowhat,String userName,String openId,String userImage){
        Call<UserResult> call = UserGetByWxinModel.getModel().getService().getResult(dowhat, userName,openId,"55555",userImage);

        call.enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                if (response.isSuccessful()) {
                    UserResult result = response.body();
                    Log.i("返回的错误码",result.getError()+"----");
                    if (result.getError() == 0) {
                        List<UserResult.UserBean> userList = result.getUser();
                        UserResult.UserBean user1 = userList.get(0);

                        //向文件中写入数据
                        writeUser(user1);
                        //跳转
                        tiaoZhuan();
                    }else{
                        Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 通过微博号查询服务器端
     */
    public void getUserByWbo(String dowhat,String userName,String openId,String userImage){
        Call<UserResult> call = UserGetByWboModel.getModel().getService().getResult(dowhat, userName,openId,"55555",userImage);

        call.enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                if (response.isSuccessful()) {
                    UserResult result = response.body();
                    Log.i("返回的错误码",result.getError()+"----");
                    if (result.getError() == 0) {
                        List<UserResult.UserBean> userList = result.getUser();
                        UserResult.UserBean user1 = userList.get(0);

                        //向文件中写入数据
                        writeUser(user1);
                        //跳转
                        tiaoZhuan();
                    }else{
                        Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 将用户信息写入文件
     */
    public void writeUser(UserResult.UserBean user1){
        /**
         * 保存数据
         */
        //获得SharedPreferences.Editor对象，使SharedPreferences对象变为可编辑状态（生成编辑器）
        SharedPreferences.Editor edit = settings.edit();
        //清除文件内容
        edit.clear();

        //将数据设置给SharedPreferences.Editor对象
        /**
         * 在登录界面保存数据到文件中，在注册完成后的信息完善界面中也有信息保存
         * 在mainActivity中有数据的获取，若添加保存数据，这三者要同步，不然会出现异常
         */

        edit.putBoolean("content",true);
        edit.putInt("isFirst",1);
        edit.putString("userPhone",user1.getUserPhone());
        user.userPhoone = user1.getUserPhone();
        edit.putString("userName",user1.getUserName());
        user.userName = user1.getUserName();
        edit.putInt("userId",user1.getUserId());
        user.userId = user1.getUserId();
        edit.putString("userBirth",user1.getUserBirth());
        user.userBirth = user1.getUserBirth();
        edit.putString("userImage",user1.getUserImage());
        user.userImage = user1.getUserImage();
        edit.putString("userIntru",user1.getUserIntru());
        user.userTntru = user1.getUserIntru();
        edit.putString("userSex",user1.getUserSex());
        user.userSex = user1.getUserSex();
        edit.putInt("userProject",user1.getProject());
        user.userProject = user1.getProject();
        edit.putString("userEmail",user1.getUserEmail());
        user.userEmail = user1.getUserEmail();
        edit.putFloat("userWeight", (float) user1.getUserWeight());
        user.userWeight = (float) user1.getUserWeight();
        edit.putInt("userHeigh",user1.getUserHeigh());
        user.userHeigh = user1.getUserHeigh();
        edit.putInt("userBmi",user1.getBmi());
        user.userBmi = user1.getBmi();
        edit.putInt("userMark",user1.getUserMark());
        user.userMark = user1.getUserMark();
        edit.putString("userHobby",user1.getHobby());
        user.userHobby = user1.getHobby();

        //提交
        edit.commit();
    }

    /**
     * 界面的跳转
     */
    public void tiaoZhuan(){
        //跳转至首界面
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);

        finish();
    }
}
