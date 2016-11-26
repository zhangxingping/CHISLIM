package com.weiaibenpao.demo.chislim.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lichfaker.scaleview.HorizontalScaleScrollView;
import com.lichfaker.scaleview.VerticalScaleScrollView;
import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.bean.BooleanResult;
import com.weiaibenpao.demo.chislim.bean.UserBean;
import com.weiaibenpao.demo.chislim.model.UpdateUserModel;
import com.weiaibenpao.demo.chislim.util.Default;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 完善个人信息，在注册完后出现
 */
public class PerfectActivity extends AppCompatActivity {

    //性别布局中的控件
    private ImageView boy;
    private ImageView girl;
    private Button sexUp;
    private Button sexNext;
    private TextView mySexText;

    //昵称布局中的控件
    private Button nameNext;

    //身高选择布局整的控件
    private Button heightUp;
    private Button heightNext;
    private TextView myHeightText;

    //体重布局控件
    private Button weightUp;
    private Button weightNext;
    private TextView myWeightText;

    //日期布局
    private DatePicker mDatePicker;
    // 定义5个记录当前时间的变量
    private int year;
    private int month;
    private int day;

    private Button datebut;


    FrameLayout viewGroup;

    RelativeLayout viewName;    //昵称布局
    RelativeLayout viewSex;     //性别布局
    RelativeLayout viewWeight;   //体重布局
    RelativeLayout viewHeight;   //身高布局
    RelativeLayout viewBirth;     //出生年月

    UserBean user;
    //昵称
    EditText userNameText;

    public static final String PREFS_NAME = "UserInfo";
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect);

        user = UserBean.getUserBean();

        //获取SharedPreferences对象，第一个参数文件名，第二个参数值
        settings = getSharedPreferences(PREFS_NAME, 0);

        initLayout();
        initView();
    }

    /**
     * 实例化布局控件
     */
    public void initLayout(){

        viewGroup = (FrameLayout) findViewById(R.id.activity_perfect);

        viewName = (RelativeLayout)viewGroup.findViewById(R.id.myName);
        viewSex = (RelativeLayout)viewGroup.findViewById(R.id.mySex);
        viewWeight = (RelativeLayout)viewGroup.findViewById(R.id.myWeight);
        viewHeight = (RelativeLayout)viewGroup.findViewById(R.id.myHeight);
        viewBirth = (RelativeLayout)viewGroup.findViewById(R.id.myBirth);

        viewName.setVisibility(View.VISIBLE);
        viewSex.setVisibility(View.GONE);
        viewWeight.setVisibility(View.GONE);
        viewHeight.setVisibility(View.GONE);
        viewBirth.setVisibility(View.GONE);
    }

    /**
     * 实例化控件
     */
    public void initView(){
        //性别选择
        mySexText = (TextView) findViewById(R.id.mySexText);
        boy = (ImageView) findViewById(R.id.boy);
        boy.setAlpha((float) 1);
        user.userSex = "男";
        boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boy.setAlpha((float) 1);
                girl.setAlpha((float) 0.5);
                user.userSex = "男";
                mySexText.setText("男");
            }
        });

        girl = (ImageView) findViewById(R.id.girl);
        girl.setAlpha((float) 0.5);
        girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boy.setAlpha((float) 0.5);
                girl.setAlpha((float) 1);
                user.userSex = "女";
                mySexText.setText("女");
            }
        });

        //性别上一步
        sexUp = (Button) findViewById(R.id.sexUp);
        sexUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                viewSex.setVisibility(View.GONE);

                viewName.setVisibility(View.VISIBLE);
            }
        });

        //性别下一步
        sexNext = (Button) findViewById(R.id.sexNext);
        sexNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                viewSex.setVisibility(View.GONE);
                viewHeight.setVisibility(View.VISIBLE);
            }
        });

        userNameText = (EditText) findViewById(R.id.userNameText);
        user.userName = "运动达人";
        //昵称下一步
        nameNext = (Button) findViewById(R.id.nameNext);
        nameNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.userName = userNameText.getText().toString().trim();
                viewName.setVisibility(View.GONE);
                viewSex.setVisibility(View.VISIBLE);
            }
        });

        //身高选择
        myHeightText = (TextView) findViewById(R.id.myHeightText);
        user.userHeigh=171;
        VerticalScaleScrollView vScaleScrollView = (VerticalScaleScrollView) findViewById(R.id.verticalScale);
        vScaleScrollView.setOnScrollListener(new HorizontalScaleScrollView.OnScrollListener() {
            @Override
            public void onScaleScroll(int scale) {
                user.userHeigh = scale;
                myHeightText.setText(String.valueOf(scale));
            }
        });

        //身高中的上一步
        heightUp = (Button) findViewById(R.id.heightUp);
        heightUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewSex.setVisibility(View.VISIBLE);
                viewHeight.setVisibility(View.GONE);
            }
        });

        //身高中的下一步
        heightNext = (Button) findViewById(R.id.heightNext);
        heightNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHeight.setVisibility(View.GONE);
                viewWeight.setVisibility(View.VISIBLE);
            }
        });



        //体重选择
        myWeightText = (TextView) findViewById(R.id.myWeightText);
        user.userWeight = (float)65.5;
        HorizontalScaleScrollView scaleScrollView = (HorizontalScaleScrollView) findViewById(R.id.horizontalScale);
        scaleScrollView.setOnScrollListener(new HorizontalScaleScrollView.OnScrollListener() {
            @Override
            public void onScaleScroll(int scale) {
                user.userWeight = (float)scale;
                myWeightText.setText(String.valueOf(scale));
            }
        });

        //体重中的上一步;
        weightUp = (Button) findViewById(R.id.weightUp);
        weightUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewWeight.setVisibility(View.GONE);
                viewHeight.setVisibility(View.VISIBLE);
            }
        });

        //体重中的下一步
        weightNext = (Button) findViewById(R.id.weightNext);
        weightNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewWeight.setVisibility(View.GONE);
                viewBirth.setVisibility(View.VISIBLE);
            }
        });


        //日期布局
        mDatePicker = (DatePicker) findViewById(R.id.datePicker);
        user.userBirth = "1990.07.07";
        Calendar c = Calendar.getInstance();
        /*year = c.get(Calendar.YEAR);*/
        year = 1990;
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        //初始化DatePicker组件，初始化时指定监听器
        mDatePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker arg0, int year, int month, int day) {
                // 显示当前日期、时间
                String date = year + "." + month + "." + day;
                Log.i("日期",date);

                user.userBirth = date;
            }
        });

        //完成
        datebut = (Button) findViewById(R.id.datebut);
        datebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获得SharedPreferences.Editor对象，使SharedPreferences对象变为可编辑状态（生成编辑器）
                SharedPreferences.Editor edit = settings.edit();

                //将数据设置给SharedPreferences.Editor对象
                edit.putBoolean("content",true);
                edit.putInt("userId",user.userId);
                edit.putString("userName",user.userName);
                edit.putString("userPhone",user.userPhoone);
                edit.putString("userBirth",user.userBirth);
                edit.putString("userHobby","我的爱好");
                edit.putString("userSex",user.userSex);
                edit.putString("userEmail","------");
                edit.putString("userImage","http://112.74.28.179:8080/Weiaibenpao/Image/20160818197303.PNG");
                edit.putString("userIntru","展现自我，为爱奔跑");
                edit.putInt("userBmi",0);
                edit.putInt("userMark",0);
                edit.putInt("userHeigh",user.userHeigh);
                edit.putFloat("userWeight", user.userWeight);
                edit.putInt("userProject",0);

                //提交
                edit.commit();

                //上传数据
                startMain();
            }
        });
    }

    /**
     * 上传用户数据，跳转至首界面
     */
    public void startMain(){
        Call<BooleanResult> call = UpdateUserModel.getModel().getService().getResult(Default.updateUser,user.userId,user.userName,user.userSex,user.userHeigh,(float) user.userWeight,user.userBirth,"","","");

        call.enqueue(new Callback<BooleanResult>() {
            @Override
            public void onResponse(Call<BooleanResult> call, Response<BooleanResult> response) {
                if (response.isSuccessful()) {
                    BooleanResult result = response.body();
                    if (result.isFlag()) {

                        Intent intent = new Intent(PerfectActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();

                    }else{

                    }
                }
            }

            @Override
            public void onFailure(Call<BooleanResult> call, Throwable t) {
                Toast.makeText(PerfectActivity.this,"OK---",Toast.LENGTH_SHORT).show();
            }
        });
    }


}


