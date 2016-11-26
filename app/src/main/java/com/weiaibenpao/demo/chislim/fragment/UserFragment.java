package com.weiaibenpao.demo.chislim.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.weiaibenpao.demo.chislim.Interface.OnGetPro;
import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.base.BaseFragment;
import com.weiaibenpao.demo.chislim.bean.BooleanResult;
import com.weiaibenpao.demo.chislim.bean.GetOneSportResult;
import com.weiaibenpao.demo.chislim.bean.RegistResult;
import com.weiaibenpao.demo.chislim.bean.UserBean;
import com.weiaibenpao.demo.chislim.model.GetDisModel;
import com.weiaibenpao.demo.chislim.model.GetOneSportModel;
import com.weiaibenpao.demo.chislim.model.UpdateProjectModel;
import com.weiaibenpao.demo.chislim.ui.AddThemeActivity;
import com.weiaibenpao.demo.chislim.ui.DistenceActivity;
import com.weiaibenpao.demo.chislim.ui.SetActivity;
import com.weiaibenpao.demo.chislim.ui.UpdateUserActivity;
import com.weiaibenpao.demo.chislim.util.CircleTransform;
import com.weiaibenpao.demo.chislim.util.Default;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Administrator on 2016/4/22.
 */
public class UserFragment extends BaseFragment implements View.OnClickListener {

    private TextView addNotes;
    private TextView getStart;
    /*界面控件*/
    private ImageView userImage;      //用户头像
    private TextView userName;      //用户名称
    private ImageView userSet;       //用户设置
    private TextView updateUser;     //修改用户信息
    private TextView userHeight;    //用户身高
    private TextView userWeight;     //用户体重
    private TextView userBMI;         //用户的BMI
    private TextView userSportData;   //用户积分
    private TextView userDistance;       //用户的今日运动距离
    private ImageView successImage;     //用户获得的成就图标
    private TextView successText;        //用户获得的成就名称
    private TextView userProject;       //设置用户的运动量
    private LinearLayout sportProject;   //用户计划

    private Button myShare;          //分享

    private ListView disListView;


    //目标设置
    EditText myProject;

    private View view;

    private UserBean user;

    Context context;

    int dis;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_user, container, false);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }

        user = UserBean.getUserBean();
        context = getActivity();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        Picasso.with(context)
                .load(user.userImage)
                .resize(200, 200)
                .placeholder(R.drawable.logo1)
                .error(R.drawable.logo1)
                .transform(new CircleTransform())
                .into(userImage);

        userName.setText(user.userName);
        userHeight.setText(String.valueOf(user.userHeigh));
        userWeight.setText(String.valueOf(user.userWeight));
        userBMI.setText(String.valueOf(user.userBmi));


    }


    @Override
    public void initView(View view) {
        userImage = (ImageView) view.findViewById(R.id.userImage);
        /*Picasso.with(getContext())
                .load(user.userImage)
				.placeholder(R.mipmap.ic_launcher)
				.error(R.mipmap.ic_launcher)
				.transform(new CircleTransform())
				.into(userImage);*/

        userImage.setOnClickListener(this);

        userName = (TextView) view.findViewById(R.id.userName);
        userName.setOnClickListener(this);

        userSet = (ImageView) view.findViewById(R.id.userSet);
        userSet.setOnClickListener(this);

        updateUser = (TextView) view.findViewById(R.id.updateUser);
        updateUser.setOnClickListener(this);

        userHeight = (TextView) view.findViewById(R.id.heighData);
        userHeight.setOnClickListener(this);

        userWeight = (TextView) view.findViewById(R.id.weightData);
        userWeight.setOnClickListener(this);

        userBMI = (TextView) view.findViewById(R.id.bmiData);
        userBMI.setOnClickListener(this);

        userSportData = (TextView) view.findViewById(R.id.userSportData);
        userSportData.setText(String.valueOf(user.userMark));

        userDistance = (TextView) view.findViewById(R.id.userDistance);

        successImage = (ImageView) view.findViewById(R.id.successImage);
        successText = (TextView) view.findViewById(R.id.successTitle);

        userProject = (TextView) view.findViewById(R.id.userProject);
        userProject.setText(String.valueOf(user.userProject));

        sportProject = (LinearLayout) view.findViewById(R.id.sportProject);
        sportProject.setOnClickListener(this);

        addNotes = (TextView) view.findViewById(R.id.addNotes);
        addNotes.setOnClickListener(this);

        getStart = (TextView) view.findViewById(R.id.getStart);
        getStart.setOnClickListener(this);

        //获取用户的今日运动距离
        getOneSport(user.userId, getNowDate());

        //获取用户的当月累计运动距离
        getDis(user.userId);

        disListView = (ListView) view.findViewById(R.id.disListView);

        //分享
        myShare = (Button) view.findViewById(R.id.myShare);
        myShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /**
                 * 参数1：内容
                 * 参数2：标题
                 * 参数3：展示图片
                 * 参数4：媒体文件
                 * http://112.74.28.179:8080/Weiaibenpao/video/qms006.mp4
                 */
                mListener.shareSport("IPO时刻为您的健康服务", "爱运动爱生活", "http://112.74.28.179:8080/Weiaibenpao/Image/test6.png", "http://112.74.28.179:8080/Weiaibenpao/video/qms006.mp4");

            }
        });
    }


    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userImage:
                goUserInfo();
                break;
            case R.id.userName:
                goUserInfo();
                break;
            case R.id.userSet:
                userSet();
                break;
            case R.id.updateUser:
                goUserInfo();
                break;
            case R.id.heighData:
                goUserInfo();
                break;
            case R.id.weightData:
                goUserInfo();
                break;
            case R.id.bmiData:
                goUserInfo();
                break;
            case R.id.sportProject:
                projectDistance();
                break;
            case R.id.addNotes:
                Intent intent = new Intent(context, AddThemeActivity.class);
                startActivity(intent);
                break;
            case R.id.getStart:
                Toast.makeText(context,"1111",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 跳转到个人详情页面
     */
    public void goUserInfo() {
        Intent intent = new Intent(context, UpdateUserActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到设置界面
     */
    public void userSet() {
        Intent intent = new Intent(context, SetActivity.class);
        startActivity(intent);
    }


    //运动目标设置
    public void projectDistance() {

		/*View view = LayoutInflater.from(context).inflate(R.layout.project_layout,null);
        myProject = (EditText) view.findViewById(R.id.myProject);
		AlertDialog alertDialog = new AlertDialog.Builder(context)
				.setView(view)
				.setPositiveButton("确定",new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						try {
							String project = myProject.getText().toString().trim();

							user.userProject = Integer.parseInt(project);
							userProject.setText(project);

							SetProject(user.userProject);

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).create();
		alertDialog.show();*/

        Intent intent = new Intent(context, DistenceActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            Log.i("MainActivity", String.valueOf(data.getIntExtra("position", 0)));
            int dis = data.getIntExtra("position", 0);

            user.userProject = dis;
            userProject.setText(String.valueOf(user.userProject));
            SetProject(user.userProject);
        }
    }

    //上传计划
    public void SetProject(int project) {
        Call<BooleanResult> call = UpdateProjectModel.getModel().getService().getResult(Default.updateProject, user.userId, project);

        call.enqueue(new Callback<BooleanResult>() {
            @Override
            public void onResponse(Call<BooleanResult> call, Response<BooleanResult> response) {
                if (response.isSuccessful()) {
                    BooleanResult result = response.body();
                    if (result.isFlag()) {
                        mListener.getPro(user.userProject);
                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<BooleanResult> call, Throwable t) {

            }
        });
    }

    /**
     * 根据日期获取用户今日运动距离
     */
    public void getOneSport(int userId, String date) {
        Call<GetOneSportResult> call = GetOneSportModel.getModel().getService().getResult(Default.getOneSport, userId, date);

        call.enqueue(new Callback<GetOneSportResult>() {
            @Override
            public void onResponse(Call<GetOneSportResult> call, Response<GetOneSportResult> response) {
                if (response.isSuccessful()) {
                    GetOneSportResult result = response.body();
                    if (result.getError() == 0) {
                        List<GetOneSportResult.EveryDaySportBean> sportResultList = result.getEveryDaySport();

                        GetOneSportResult.EveryDaySportBean sportResult = sportResultList.get(0);

                        userDistance.setText(String.valueOf(sportResult.getDistance()));
                    } else {

                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<GetOneSportResult> call, Throwable t) {

            }
        });
    }

    /**
     * 从服务器获取距离
     */
    public void getDis(int userId) {
        Call<RegistResult> call = GetDisModel.getModel().getService().getResult(Default.countDis, userId);

        call.enqueue(new Callback<RegistResult>() {
            @Override
            public void onResponse(Call<RegistResult> call, Response<RegistResult> response) {
                if (response.isSuccessful()) {
                    RegistResult result = response.body();
                    if (result.getNum() != -1) {

                        dis = result.getNum();
                        //设置运动成就
                        Log.i("运动距离", dis + "===========");
                        setSuccess(dis);

                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<RegistResult> call, Throwable t) {

            }
        });
    }

    /**
     * 获取系统当前日期
     *
     * @return
     */
    public String getNowDate() {
        Date dt = new Date();
        SimpleDateFormat matter1 = new SimpleDateFormat("yyyy.MM.dd");
        String dateStr = matter1.format(dt);
        Log.i("当前时间", dateStr);
        return dateStr;
    }

    /**
     * 根据用户的累计运动距离设置运动成就
     */
    public void setSuccess(int distance) {

        /**
         * 因为没有图片，暂时先注释了
         */
        /*if(distance >= 3 && distance <5){
			Picasso.with(context)
					.load(R.mipmap.ic_launcher)
					.into(successImage);
			successText.setText("3公里成就");
		}else if(distance >= 5 && distance <10){
			Picasso.with(context)
					.load(R.mipmap.ic_launcher)
					.into(successImage);
			successText.setText("5公里成就");
		}else if(distance >= 10 && distance <21){
			Picasso.with(context)
					.load(R.mipmap.ic_launcher)
					.into(successImage);
			successText.setText("10公里成就");
		}else if(distance >= 21 && distance <42){
			Picasso.with(context)
					.load(R.mipmap.ic_launcher)
					.into(successImage);
			successText.setText("21公里成就");
		}else if(distance >= 42 && distance <50){
			Picasso.with(context)
					.load(R.mipmap.ic_launcher)
					.into(successImage);
			successText.setText("42公里成就");
		}else if(distance >= 50 && distance <100){
			Picasso.with(context)
					.load(R.mipmap.ic_launcher)
					.into(successImage);
			successText.setText("50公里成就");
		}else if(distance >= 100){
			Picasso.with(context)
					.load(R.mipmap.ic_launcher)
					.into(successImage);
			successText.setText("100公里成就");
		}*/
    }

/*	//接口回调
	public interface OnGetPro {
		public void getPro(int pro);
		public void shareSport(String text,String title,String url,String media);
	}*/

    OnGetPro mListener;

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        //在onAttach方法中实例化myListener
        mListener = (OnGetPro) activity;
    }


   /* @OnClick(R.id.addNotes)
    public void onClick() {
        Intent intent = new Intent(context, AddThemeActivity.class);
        startActivity(intent);
    }*/

}
