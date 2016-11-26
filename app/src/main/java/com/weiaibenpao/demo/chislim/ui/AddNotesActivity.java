package com.weiaibenpao.demo.chislim.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.weiaibenpao.demo.chislim.Interface.GetInterfaceBooleanListener;
import com.weiaibenpao.demo.chislim.R;
import com.weiaibenpao.demo.chislim.util.GetIntentData;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddNotesActivity extends Activity {

    OkHttpClient client;
    Request request;
    String url = "http://112.74.28.179:8080/Chislim/Travel_notes_Servlet?dowhat=getUpLoadToken";
    UploadManager uploadManager;

    String token;

    private static final int REQUEST_CODE = 732;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.notesAddress)
    EditText notesAddress;
    @BindView(R.id.notesTitle)
    EditText notesTitle;
    @BindView(R.id.selectPic)
    Button selectPic;
    @BindView(R.id.notesIntro)
    EditText notesIntro;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.pb_progressbar)
    ProgressBar pbProgressbar;

    private ArrayList<String> mResultsPath = new ArrayList<>();
    //private ArrayList<String> mResultsName = new ArrayList<>();
    StringBuffer mResultsName;
    GetIntentData getIntentData;

    int num = 0;
    int tn_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notes);
        ButterKnife.bind(this);

        //第一步 初始化
        initConfig();

        getIntentData = new GetIntentData();
        getIntentData.setGetInterfaceBooleanListener(new GetInterfaceBooleanListener() {
            @Override
            public void getBoolean(boolean flag) {
                if (flag) {
                    Toast.makeText(AddNotesActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddNotesActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //实例化client
        new Thread(new Runnable() {
            @Override
            public void run() {
                //创建OkHttpClient
                client = new OkHttpClient();
                // 创建请求
                request = new Request.Builder()//
                        .url(url)//
                        .get()//
                        .build();
            }
        }).start();

        Intent intent = getIntent();
        tn_id = intent.getIntExtra("intId",1);

        pbProgressbar.setProgress(0);
    }


    //上传设置
    public void initConfig() {
        Configuration config = new Configuration.Builder()
                .chunkSize(256 * 1024)  //分片上传时，每片的大小。 默认256K
                .putThreshhold(512 * 1024)  // 启用分片上传阀值。默认512K
                .connectTimeout(10) // 链接超时。默认10秒
                .responseTimeout(60) // 服务器响应超时。默认60秒
                //       .recorder(recorder)  // recorder分片上传时，已上传片记录器。默认null
                //       .recorder(recorder, keyGen)  // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                .zone(Zone.zone0) // 设置区域，指定不同区域的上传域名、备用域名、备用IP。默认 Zone.zone0
                .build();
        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
        uploadManager = new UploadManager(config);
    }

    public void initView() {

    }

    //第二步获取tocken
    public void getTocken() {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                token = response.body().string();
                Log.i("七牛", token);

                //第三步获取文件路径
                getUrl(10);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("七牛", e.getMessage());
            }
        });
    }


    /**
     * 第三步获取文件路径
     */
    public void getUrl(int n) {
        // start multiple photos selector
        Intent intent = new Intent(AddNotesActivity.this, ImagesSelectorActivity.class);
        // max number of images to be selected
        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, n);
        // min size of image which will be shown; to filter tiny images (mainly icons)
        intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
        // show camera or not
        intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
        // pass current selected images as the initial value
        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResultsPath);
        // start the selector
        startActivityForResult(intent, REQUEST_CODE);
    }

    //图片返回
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // get selected images from selector
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mResultsPath = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);

                Log.i("七牛", " ---------- " + mResultsPath.size() + " ---------- ");

                //上传
                upLoad();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //第四步上传
    public void upLoad() {
        for (int i = 0; i < mResultsPath.size(); i++) {
            if (!mResultsPath.get(i).equals("camera_default")) {
                getUpimg(mResultsPath.get(i),i);
            }
        }
    }

    public void getUpimg(final String imagePath, final int i) {
        mResultsName = new StringBuffer();
        new Thread() {
            public void run() {
                // 图片上传到七牛 重用 uploadManager。一般地，只需要创建一个 uploadManager 对象
                UploadManager uploadManager = new UploadManager();
                uploadManager.put(imagePath, null, token,
                        new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info,
                                                 JSONObject res) {
                                // res 包含hash、key等信息，具体字段取决于上传策略的设置。
                                Log.i("七牛", key + ",\r\n " + info + ",\r\n "
                                        + res);
                                try {
                                    String key1 = (String) res.get("key");
                                    Log.i("七牛七牛",key1);
                                    mResultsName.append( res.get("key")+",");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new UploadOptions(null, null, false,
                                new UpProgressHandler() {
                                    public void progress(String key, double percent) {
                                        Log.i("七牛", key + ": " + percent);
                                        if(percent == 1){
                                            num++;
                                            initProgressbar(num);
                                        }
                                    }
                                }, null));
            }
        }.start();
    }

    public void initProgressbar(int n){
        pbProgressbar.setMax(1);
        pbProgressbar.setProgress((int)(n%mResultsPath.size()));
    }

    @OnClick({R.id.back, R.id.selectPic, R.id.submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.selectPic:
                getTocken();
                break;
            case R.id.submit:

                getIntentData.AddNotesUpLoad(this, "addOneNotesItem", tn_id, "测试名称", mResultsName, "这里是测试数据");
                break;
        }
    }
}
