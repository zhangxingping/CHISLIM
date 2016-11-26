package com.weiaibenpao.demo.chislim.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lichfaker.scaleview.BaseScaleView;
import com.lichfaker.scaleview.HorizontalScaleScrollView;
import com.lichfaker.scaleview.VerticalScaleScrollView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.bean.BooleanResult;
import com.weiaibenpao.demo.chislim.bean.UserBean;
import com.weiaibenpao.demo.chislim.model.UpdateUserModel;
import com.weiaibenpao.demo.chislim.util.CircleImg;
import com.weiaibenpao.demo.chislim.util.CircleTransform;
import com.weiaibenpao.demo.chislim.util.Default;
import com.weiaibenpao.demo.chislim.util.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserActivity extends AppCompatActivity {

    private EditText userEtName;
    private TextView updateSex;
    private TextView updateHeigh;
    private TextView updateWeight;
    private TextView updateBirth;
    private TextView updatePhone;
    private TextView updateEmail;
    private EditText userIntro;
    private TextView okUpdate;
    private TextView updateHobby;

    private ImageView myImage;
    private ImageView back;

    private Calendar cal;
    private int year;
    private int month;
    private int day;

    //用于图片的上传
    private Context context;
    Bitmap icon;
    private CircleImg avatarImg;// 头像图片
    //登录用户
    UserBean user;
    public static final String PREFS_NAME = "UserInfo";
    private SharedPreferences settings;

    // 自定义的头像编辑弹出框
    private SelectPicPopupWindow menuWindow;
    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";// 头像文件名称
    private static final int REQUESTCODE_PICK = 0;		// 相册选图标记
    private static final int REQUESTCODE_TAKE = 1;		// 相机拍照标记
    private static final int REQUESTCODE_CUTTING = 2;	// 图片裁切标记

    private String urlpath;			// 图片本地路径

    private String[] sexArry = new String[] { "女", "男","其他" };// 性别选择

    //日期选择框
    DatePicker datePicker;    //日期弹框

    //邮箱选择框
    EditText mEmail;  //弹出框邮箱地址

    //身高选择框
    VerticalScaleScrollView vScale;   //身高尺
    TextView heighData;

    //体重选择框
    HorizontalScaleScrollView hScale;      //体重尺
    TextView weightData;

    //性别选择框
    ImageView boyImg;                //性别选择框中的男
    ImageView girlImg;
    TextView sexText;

    //爱好选择框
    TextView myHobby;

    //--------修改用户数据后重新赋值user
    String name;
    String sex;
    int heigt;
    float weight;
    String birth;
    String email;
    String intro;
    String hobby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        settings = getSharedPreferences(PREFS_NAME, 0);

        context = UpdateUserActivity.this;
        icon = BitmapFactory.decodeResource(context.getResources(),R.mipmap.zw4);

        //获取一个日历对象
        cal = Calendar.getInstance();
        //获取年月日时分秒的信息
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH)+1;
        day = cal.get(Calendar.DAY_OF_MONTH);

        initdata();
        initView();
    }

    public void initdata() {
        user = UserBean.getUserBean();
    }


    public void initView(){
        //完成
        okUpdate = (TextView) findViewById(R.id.okUpdate);
        okUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
            }
        });
        //用户头像
        myImage = (ImageView) findViewById(R.id.myImage);
        Log.i("显示","111**************");
        Picasso.with(this)
                .load(user.userImage)
                .resize(200, 200)
                .placeholder(R.drawable.logo1)
                .error(R.drawable.logo1)
                .transform(new CircleTransform())
                .into(myImage);

        //用户昵称
        userEtName = (EditText) findViewById(R.id.userEtName);
        userEtName.setText(user.userName);

        //用户性别
        updateSex = (TextView) findViewById(R.id.updateSex);
        updateSex.setText(user.userSex);
/*        updateSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSexChooseDialog();
            }
        });*/

        //用户身高
        updateHeigh = (TextView) findViewById(R.id.updateHeigh);
        updateHeigh.setText(String.valueOf(user.userHeigh));

        //用户体重
        updateWeight = (TextView) findViewById(R.id.updateWeight);
        updateWeight.setText(String.valueOf(user.userWeight));

        //用户生日
        updateBirth = (TextView) findViewById(R.id.updateBirth);
        updateBirth.setText(user.userBirth);

        //用户的爱好
        updateHobby = (TextView) findViewById(R.id.updateHobby);
        updateHobby.setText(user.userHobby);

        //用户电话
        updatePhone = (TextView) findViewById(R.id.updatePhone);
        updatePhone.setText(user.userPhoone);

        updateEmail = (TextView) findViewById(R.id.updateEmail);
        updateEmail.setText(user.userEmail);

        userIntro = (EditText) findViewById(R.id.userIntro);
        userIntro.setText(user.userTntru);

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 生日弹框
     * @param v
     */
    public void updateBirth(View v){

        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.date_layout,null);
        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        //初始化DatePicker组件，初始化时指定监听器
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker arg0, int year, int month, int day) {
                // 显示当前日期、时间
                month = month+1;
                String date = year + "." + month + "." + day;
                Log.i("日期",date);

                updateBirth.setText(date);
                user.userBirth = date;
            }
        });
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        try {
                            String date = datePicker.getYear()+"."+datePicker.getMonth()+"."+datePicker.getDayOfMonth();

                            updateBirth.setText(date);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).create();

        alertDialog.show();
    }

    /**
     * 点击性别触发
     * @param v
     */
    public void updateSex(View v){
        sexDialog();
    }
    //点击邮箱触发
    public void myEmail(View v){
        emailDialog();
    }
    //点击身高触发
    public void userHeight(View v){
        heighDialog();
    }
    //点击体重触发
    public void userWeight(View v){
        weightDialog();
    }
    //点击体重触发
    public void updateHobby(View v){
        hobbyDialog();
    }


    /**
     * 点击更换头像
     * @param v
     */
    public void updateImage(View v){


        menuWindow = new SelectPicPopupWindow(UpdateUserActivity.this, itemsOnClick);
        menuWindow.showAtLocation(findViewById(R.id.activity_update_user),
                Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                // 拍照
                case R.id.takePhotoBtn:
                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //下面这句指定调用相机拍照后的照片存储的路径
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                    startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                    break;
                // 相册选择图片
                case R.id.pickPhotoBtn:
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(pickIntent, REQUESTCODE_PICK);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUESTCODE_PICK:// 直接从相册获取
                try {
                    startPhotoZoom(data.getData());
                } catch (NullPointerException e) {
                    e.printStackTrace();// 用户点击取消操作
                }
                break;
            case REQUESTCODE_TAKE:// 调用相机拍照
                File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                startPhotoZoom(Uri.fromFile(temp));
                break;
            case REQUESTCODE_CUTTING:// 取得裁剪后的图片
                if (data != null) {
                    setPicToView(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    /**
     * 保存裁剪之后的图片数据
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            // 取得SDCard图片路径做显示
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(null, photo);
            urlpath = FileUtil.saveFile(UpdateUserActivity.this, "temphead.jpg", photo);

            reg(user.userId,context,photo,"图片名称");
           /* myImage.setImageBitmap(photo);*/
            Log.i("地址",urlpath);
            /*Picasso.with(this)
                    .load("file://"+ urlpath)
                    .placeholder(R.mipmap.zw1)
                    .error(R.mipmap.zw2)
                    .transform(new CircleTransform())
                    .into(myImage);*/
        }
    }

    /**
     * 上传图片
     * @param userId
     * @param cont
     * @param photodata
     * @param regData
     */
    public void reg(int userId, final Context cont, Bitmap photodata, String regData) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            //将bitmap一字节流输出 Bitmap.CompressFormat.PNG 压缩格式，100：压缩率，baos：字节流
            photodata.compress(Bitmap.CompressFormat.PNG, 100, baos);
            baos.close();
            byte[] buffer = baos.toByteArray();
            System.out.println("图片的大小："+buffer.length);

            //将图片的字节流数据加密成base64字符输出
            String photo = Base64.encodeToString(buffer, 0, buffer.length,Base64.DEFAULT);

            //photo=URLEncoder.encode(photo,"UTF-8");
            RequestParams params = new RequestParams();
            //params.put("dowhat", "updateImage");
            params.put("userId", userId);
            params.put("photo", photo);
            params.put("name", regData);//传输的字符数据
            String url = "http://112.74.28.179:8080/Chislim/UserServlet?dowhat=updateImg";


            AsyncHttpClient client = new AsyncHttpClient();
            client.post(url, params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String imageUrl = new String(responseBody);
                    UserBean user = UserBean.getUserBean();
                    user.userImage = imageUrl;
                    Log.i("图片上传成功：",imageUrl);

                    UpdateUserActivity.this.changeImage();
                    Log.i("显示","222**************");
                    Picasso.with(UpdateUserActivity.this)
                            .load(user.userImage)
                            .resize(200, 200)
                            .transform(new CircleTransform())
                            .error(R.drawable.logo1)
                            .into(myImage);
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(cont,statusCode+"图片上传失败",Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //修改图片本地存放链接
    public void changeImage(){
        //获得SharedPreferences.Editor对象，使SharedPreferences对象变为可编辑状态（生成编辑器）
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("userImage",user.userImage);

        //提交
        edit.commit();
    }



    public int getInt(){
        if(user.userSex == "女"){
            return 0;
        }else{
            return 1;
        }
    }


    /* 性别选择框 */
    private void showSexChooseDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);// 自定义对话框
        builder.setSingleChoiceItems(sexArry, getInt(), new DialogInterface.OnClickListener() {// 2默认的选中

            @Override
            public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
                // showToast(which+"");
                updateSex.setText(sexArry[which]);
                dialog.dismiss();// 随便点击一个item消失对话框，不用点击确认取消
            }
        });

        builder.show();// 让弹出框显示
    }

    /**
     * 性别弹出框
     */
    public void sexDialog(){
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.sex_layout,null);
        sexText = (TextView) view.findViewById(R.id.sexText);
        boyImg = (ImageView) view.findViewById(R.id.boyImg);
        girlImg = (ImageView) view.findViewById(R.id.girlImg);
        boyImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boyImg.setAlpha((float) 1);
                girlImg.setAlpha((float) 0.5);
                sexText.setText("男");
                updateSex.setText("男");
            }
        });
        girlImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boyImg.setAlpha((float) 0.5);
                girlImg.setAlpha((float) 1);
                sexText.setText("女");
                updateSex.setText("女");
            }
        });
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {

                    }
                }).create();
        alertDialog.show();
    }
    /**
     * 邮箱弹出框
     */
    public void emailDialog(){
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.email_layout,null);
        mEmail = (EditText) view.findViewById(R.id.myEmail);
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        try {
                            String myEmail = mEmail.getText().toString().trim();

                            updateEmail.setText(myEmail);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).create();
        alertDialog.show();
    }

    /**
     * 身高弹出框
     */
    public void heighDialog(){
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.height_layout,null);
        heighData = (TextView) view.findViewById(R.id.heighData);
        vScale = (VerticalScaleScrollView) view.findViewById(R.id.verticalScale);
        vScale.setOnScrollListener(new BaseScaleView.OnScrollListener() {
            @Override
            public void onScaleScroll(int scale) {
                heighData.setText(String.valueOf(scale));
                updateHeigh.setText(String.valueOf(scale));
            }
        });
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {

                    }
                }).create();
        alertDialog.show();
    }

    /**
     * 体重弹框
     */
    public void weightDialog(){
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.weight_layout,null);
        weightData = (TextView) view.findViewById(R.id.weightData);
        hScale = (HorizontalScaleScrollView) view.findViewById(R.id.horizontalScale);
        hScale.setOnScrollListener(new BaseScaleView.OnScrollListener() {
            @Override
            public void onScaleScroll(int scale) {
                weightData.setText(String.valueOf(scale));
                updateWeight.setText(String.valueOf(scale));
            }
        });
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {

                    }
                }).create();
        alertDialog.show();
    }

    /**
     * 爱好弹框
     */
    public void hobbyDialog(){
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.hobby_layout,null);
        myHobby = (TextView) view.findViewById(R.id.myHobby);
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        try {
                            String hobby = myHobby.getText().toString().trim();

                            updateHobby.setText(hobby);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).create();
        alertDialog.show();
    }


    /**
     * 修改用户信息
     */
    public void updateUser(){

        name = userEtName.getText().toString().trim();
        sex = updateSex.getText().toString().trim();
        heigt = Integer.parseInt(updateHeigh.getText().toString().trim());
        weight = Float.parseFloat(updateWeight.getText().toString().trim());
        birth = updateBirth.getText().toString().trim();
        email = updateEmail.getText().toString().trim();
        hobby = updateHobby.getText().toString().trim();
        intro = userIntro.getText().toString().trim();

        Call<BooleanResult> call = UpdateUserModel.getModel().getService().getResult(Default.updateUser,user.userId,name,sex,heigt,(float) weight,birth,hobby,email,intro);

        call.enqueue(new Callback<BooleanResult>() {
            @Override
            public void onResponse(Call<BooleanResult> call, Response<BooleanResult> response) {
                if (response.isSuccessful()) {
                    BooleanResult result = response.body();
                    if (result.isFlag()) {

                        //获得SharedPreferences.Editor对象，使SharedPreferences对象变为可编辑状态（生成编辑器）
                        SharedPreferences.Editor edit = settings.edit();

                        //将数据设置给SharedPreferences.Editor对象
                        edit.putString("userName",name);
                        edit.putString("userBirth",birth);
                        edit.putString("userIntru",intro);
                        edit.putString("userSex",sex);
                        edit.putString("userEmail",email);
                        edit.putFloat("userWeight", weight);
                        edit.putInt("userHeigh",heigt);
                        edit.putString("userHobby",hobby);

                        //提交
                        edit.commit();

                        user.userName = name;
                        user.userSex = sex;
                        user.userHeigh = heigt;
                        user.userWeight = weight;
                        user.userBirth = birth;
                        user.userEmail = email;
                        user.userTntru = intro;
                        user.userHobby = hobby;

                    }else{

                    }
                }else{

                }
            }
            @Override
            public void onFailure(Call<BooleanResult> call, Throwable t) {

            }
        });
    }


}
