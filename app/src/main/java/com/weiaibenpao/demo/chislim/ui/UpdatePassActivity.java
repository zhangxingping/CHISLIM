package com.weiaibenpao.demo.chislim.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.bean.BooleanResult;
import com.weiaibenpao.demo.chislim.bean.UserResult;
import com.weiaibenpao.demo.chislim.model.UpdatePassModel;
import com.weiaibenpao.demo.chislim.model.UserModel;
import com.weiaibenpao.demo.chislim.util.Default;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePassActivity extends AppCompatActivity {

    private EditText oldPass;
    private EditText newPass;
    private EditText newPassTwo;
    private Button updatePass;

    private String oldPassStr;
    private String newPassStr;
    private String newPassStrTwo;
    private int userId;                             //值从本地文件获得，暂时未得到
    private String userPhone;                       //值从本地文件获得，暂时未得到
    private ImageView back;

    public static final String PREFS_NAME = "UserInfo";
    private SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_update_pass);

        settings = getSharedPreferences(PREFS_NAME, 0);

        userPhone = settings.getString("userPhone","");
        userId = settings.getInt("userId",0);

        initView();
    }

    public void initView(){
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        oldPass = (EditText) findViewById(R.id.oldPass);

        newPass = (EditText) findViewById(R.id.newPass);

        newPassTwo = (EditText) findViewById(R.id.newPassTwo);



        updatePass = (Button) findViewById(R.id.updatePass);
        //点击修改密码
        updatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldPassStr = oldPass.getText().toString().trim();
                newPassStr = newPass.getText().toString().trim();
                newPassStrTwo = newPassTwo.getText().toString().trim();
                /*拿到user中的对象，先进行原始密码的验证，通过后进行新密码的修改*/

                if(newPassStr == null || newPassStrTwo ==""){
                    Toast.makeText(UpdatePassActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }else if(newPassStrTwo == null || newPassStrTwo ==""){
                    Toast.makeText(UpdatePassActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }else if(!newPassStr.equals(newPassStrTwo)){
                    Toast.makeText(UpdatePassActivity.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
                    return;
                }
                //原始密码的验证
                getUserByPhone(userPhone,oldPassStr);
            }
        });
    }

    /**
     * 修改密码
     * @param dowhat
     * @param userID
     * @param userPass
     */
    public void updatePass(String dowhat,int userID,String userPass){
        Call<BooleanResult> call = UpdatePassModel.getModel().getService().getResult(dowhat,userID,userPass);

        call.enqueue(new Callback<BooleanResult>() {
            @Override
            public void onResponse(Call<BooleanResult> call, Response<BooleanResult> response) {
                if (response.isSuccessful()) {
                    BooleanResult result = response.body();
                    if (result.isFlag()) {
                        //获得SharedPreferences.Editor对象，使SharedPreferences对象变为可编辑状态（生成编辑器）
                        SharedPreferences.Editor edit = settings.edit();
                        //清除文件内容
                        edit.clear();
                        //提交
                        edit.commit();

                        Intent intent = new Intent(UpdatePassActivity.this,LoginActivity.class);
                        startActivity(intent);

                    }else{
                        Toast.makeText(UpdatePassActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BooleanResult> call, Throwable t) {
                Toast.makeText(UpdatePassActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 通过电话号码查询服务器端原始密码的验证
     */
    public void getUserByPhone(String userPhone,String userPass){
        Call<UserResult> call = UserModel.getModel().getService().getResult(Default.getOneByPhone,userPhone,userPass);

        call.enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                if (response.isSuccessful()) {
                    UserResult result = response.body();
                    Toast.makeText(UpdatePassActivity.this,result.getError()+"",Toast.LENGTH_SHORT).show();
                    if (result.getError() == 0) {
                        updatePass("updatePass",userId,newPassStr);

                    }else{
                        Toast.makeText(UpdatePassActivity.this,"原始密码错误",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
                Toast.makeText(UpdatePassActivity.this,"原始密码错误",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
