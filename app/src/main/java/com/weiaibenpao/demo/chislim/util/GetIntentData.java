package com.weiaibenpao.demo.chislim.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.weiaibenpao.demo.chislim.Interface.GetInterfaceApkVersionListener;
import com.weiaibenpao.demo.chislim.Interface.GetInterfaceBooleanListener;
import com.weiaibenpao.demo.chislim.Interface.GetInterfaceIntIdListener;
import com.weiaibenpao.demo.chislim.Interface.GetInterfaceSinerListener;
import com.weiaibenpao.demo.chislim.Interface.GetInterfaceVideoListener;
import com.weiaibenpao.demo.chislim.Interface.GetObjectListener;
import com.weiaibenpao.demo.chislim.bean.ApkVersionBean;
import com.weiaibenpao.demo.chislim.bean.BooleanResult;
import com.weiaibenpao.demo.chislim.bean.GetIntId;
import com.weiaibenpao.demo.chislim.bean.LunBoTuBeanResult;
import com.weiaibenpao.demo.chislim.bean.MainBeanResult;
import com.weiaibenpao.demo.chislim.bean.NewTeachGifImageResult;
import com.weiaibenpao.demo.chislim.bean.NewTeachPlanResut;
import com.weiaibenpao.demo.chislim.bean.NewTeachResult;
import com.weiaibenpao.demo.chislim.bean.NotesMessageResult;
import com.weiaibenpao.demo.chislim.bean.TeachResult;
import com.weiaibenpao.demo.chislim.bean.TravelInfoResult;
import com.weiaibenpao.demo.chislim.bean.TravelNotesResult;
import com.weiaibenpao.demo.chislim.bean.TravelNotesUserItemResult;
import com.weiaibenpao.demo.chislim.bean.TravelResult;
import com.weiaibenpao.demo.chislim.model.AddNotesThemeUpLoadModel;
import com.weiaibenpao.demo.chislim.model.AddNotesUpLoadModel;
import com.weiaibenpao.demo.chislim.model.AddSportModel;
import com.weiaibenpao.demo.chislim.model.ApkVersionModel;
import com.weiaibenpao.demo.chislim.model.GetStartModel;
import com.weiaibenpao.demo.chislim.model.LunBoTuModel;
import com.weiaibenpao.demo.chislim.model.MainBeanModel;
import com.weiaibenpao.demo.chislim.model.MusicSinerModel;
import com.weiaibenpao.demo.chislim.model.NewTeachGifImageModel;
import com.weiaibenpao.demo.chislim.model.NewTeachModel;
import com.weiaibenpao.demo.chislim.model.NewTeachPlanModel;
import com.weiaibenpao.demo.chislim.model.NewTeachTab1Model;
import com.weiaibenpao.demo.chislim.model.NewTeachTab2Model;
import com.weiaibenpao.demo.chislim.model.NewTeachTabModel;
import com.weiaibenpao.demo.chislim.model.NotesMessageModel;
import com.weiaibenpao.demo.chislim.model.SupportModel;
import com.weiaibenpao.demo.chislim.model.TeachModel;
import com.weiaibenpao.demo.chislim.model.TravelInfoModel;
import com.weiaibenpao.demo.chislim.model.TravelMessageModel;
import com.weiaibenpao.demo.chislim.model.TravelModel;
import com.weiaibenpao.demo.chislim.model.TravelNotesModel;
import com.weiaibenpao.demo.chislim.model.TravelNotesUserItemModel;
import com.weiaibenpao.demo.chislim.music.bean.MusicSinerResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by lenovo on 2016/9/27.
 */

public class GetIntentData {

    private ACache mCache;

    private GetInterfaceSinerListener sinerListener;
    private GetInterfaceVideoListener interfaceVideo;
    private GetInterfaceBooleanListener interfaceBoolean;
    private GetInterfaceIntIdListener interfaceIntId;
    private GetInterfaceApkVersionListener apkVersionListener;
    private GetObjectListener getObjectListener;

    //定义接口入住对象
    public void setGetIntentDataListener(GetInterfaceVideoListener interfaceVideo){
        this.interfaceVideo = interfaceVideo;
    }

    //定义接口入住对象
    public void setGetInterfaceBooleanListener(GetInterfaceBooleanListener interfaceBoolean){
        this.interfaceBoolean = interfaceBoolean;
    }

    //定义接口入住对象
    public void setGetInterfaceIntIdListener(GetInterfaceIntIdListener interfaceIntId){
        this.interfaceIntId = interfaceIntId;
    }

    //定义接口入住对象
    public void setGetInterfaceSinerListener(GetInterfaceSinerListener sinerListener){
        this.sinerListener = sinerListener;
    }


    //定义接口入住对象
    public void setGetInterfaceApkVersionListener(GetInterfaceApkVersionListener apkVersionListener){
        this.apkVersionListener = apkVersionListener;
    }

    //定义接口入住对象
    public void setGetObjectListener(GetObjectListener getObjectListener){
        this.getObjectListener = getObjectListener;
    }

    /**
     * 获取教程
     * @param context
     */
    public void getTeach(final Context context){
        mCache = ACache.get(context);    //实例化缓存
        Call<TeachResult> call = TeachModel.getModel().getService().getResult("getAll");

        call.enqueue(new Callback<TeachResult>() {
            @Override
            public void onResponse(Call<TeachResult> call, Response<TeachResult> response) {
                if (response.isSuccessful()) {
                    TeachResult result = response.body();

                    if (result.getError() == 0) {
                        mCache.put("teachResult",result);
                        interfaceVideo.getDateList((ArrayList) result.getTeach());

                    }else{
                        Toast.makeText(context,"教程获取失败",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TeachResult> call, Throwable t) {
                Toast.makeText(context,"教程获取失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 拉去小众景点
     * @param context
     * @param dowhat
     * @param tab
     * @param i
     * @param num
     */
    public void getTravel(final Context context, String dowhat, final String tab, int i, int num){
        mCache = ACache.get(context);    //实例化缓存
        Call<TravelResult> call = TravelModel.getModel().getService().getResult(dowhat,tab,i,num);

        call.enqueue(new Callback<TravelResult>() {
            @Override
            public void onResponse(Call<TravelResult> call, Response<TravelResult> response) {
                if (response.isSuccessful()) {
                    TravelResult result = response.body();

                    if(tab == "location" && result.getTravel().size() > 0){
                        mCache.put("travelNotesResultlocation",result);
                    }else if(tab == "foreign" && result.getTravel().size() > 0){
                        mCache.put("travelNotesResultforeign",result);
                    }else if(tab == "taiwan" && result.getTravel().size() > 0){
                        mCache.put("travelNotesResulttaiwan",result);
                    }else{
                        Toast.makeText(context,"暂无更多数据...",Toast.LENGTH_SHORT).show();
                    }
                    interfaceVideo.getDateList((ArrayList) result.getTravel());

                }
            }

            @Override
            public void onFailure(Call<TravelResult> call, Throwable t) {
                interfaceVideo.getDateList(new ArrayList());
                Toast.makeText(context,"检查网络连接...",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 拉去小众景点详情
     * @param context
     * @param dowhat
     */
    public void getTravelInfo(final Context context, String dowhat, final int t_id){
        mCache = ACache.get(context);    //实例化缓存
        Call<TravelInfoResult> call = TravelInfoModel.getModel().getService().getResult(dowhat,t_id);

        call.enqueue(new Callback<TravelInfoResult>() {
            @Override
            public void onResponse(Call<TravelInfoResult> call, Response<TravelInfoResult> response) {
                if (response.isSuccessful()) {
                    TravelInfoResult result = response.body();

                    if (result.getError() == 0) {
                        mCache.put("travelInfoResult" + t_id,result);
                        interfaceVideo.getDateList((ArrayList) result.getTravel_info());

                    }else{
                        Toast.makeText(context,"暂无更多",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TravelInfoResult> call, Throwable t) {
                Toast.makeText(context,"暂无更多",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 拉取游记
     * @param context
     * @param dowhat
     */
    public void getTravelNotes(final Context context,String dowhat,int i,int num){
        mCache = ACache.get(context);    //实例化缓存

        Call<TravelNotesResult> call = TravelNotesModel.getModel().getService().getResult(dowhat,i,num);

        call.enqueue(new Callback<TravelNotesResult>() {
            @Override
            public void onResponse(Call<TravelNotesResult> call, Response<TravelNotesResult> response) {
                if (response.isSuccessful()) {
                    TravelNotesResult result = response.body();
                    if( result.getTravel_notes().size() > 0){
                        mCache.put("travelNotesResult",result);
                    }
                    interfaceVideo.getDateList((ArrayList) result.getTravel_notes());
                }
            }

            @Override
            public void onFailure(Call<TravelNotesResult> call, Throwable t) {
                interfaceVideo.getDateList(new ArrayList());
            }
        });
    }

    /**
     * 点赞
     * @param context
     * @param dowhat
     */
    public void Support(final Context context,String dowhat,int userID,int tn_id,int tab1){
        Log.i("赞","点赞  "+userID+"----"+tn_id + "======" +tab1);
        //Call<BooleanResult> call = SupportModel.getModel().getService().getResult("addOneSupport",17,3,1);
        Call<BooleanResult> call = SupportModel.getModel().getService().getResult("delOneSupportNotes",17,3,0);

        call.enqueue(new Callback<BooleanResult>() {
            @Override
            public void onResponse(Call<BooleanResult> call, Response<BooleanResult> response) {
                if (response.isSuccessful()) {
                    BooleanResult result = response.body();
                    //interfaceBoolean.getBoolean(result.isFlag());
                }
            }
            @Override
            public void onFailure(Call<BooleanResult> call, Throwable t) {
                Toast.makeText(context,"请检查网络...",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 查询留言
     * @param context
     * @param dowhat
     */
    public void getMessage(final Context context,String dowhat,int tn_id){
        Call<NotesMessageResult> call = NotesMessageModel.getModel().getService().getResult(dowhat,tn_id);

        call.enqueue(new Callback<NotesMessageResult>() {
            @Override
            public void onResponse(Call<NotesMessageResult> call, Response<NotesMessageResult> response) {
                if (response.isSuccessful()) {
                    NotesMessageResult result = response.body();
                    if (result.getError() == 0) {
                        interfaceVideo.getDateList((ArrayList) result.getTravel_message());
                    }else{
                        Toast.makeText(context,"暂无更多",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<NotesMessageResult> call, Throwable t) {
                Toast.makeText(context,"暂无更多",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 查询某个用户发表的所有游记
     * @param context
     * @param dowhat
     */
    public void getNotesUserItemMessage(final Context context, String dowhat, final int tn_id){
        mCache = ACache.get(context);    //实例化缓存
        Call<TravelNotesUserItemResult> call = TravelNotesUserItemModel.getModel().getService().getResult(dowhat,tn_id);

        call.enqueue(new Callback<TravelNotesUserItemResult>() {
            @Override
            public void onResponse(Call<TravelNotesUserItemResult> call, Response<TravelNotesUserItemResult> response) {
                if (response.isSuccessful()) {
                    TravelNotesUserItemResult result = response.body();
                    if(result.getTravel_notes_item().size() > 0){
                        mCache.put("travelNotesUserItemResult" + tn_id,result);
                    }
                    interfaceVideo.getDateList((ArrayList) result.getTravel_notes_item());
                }
            }
            @Override
            public void onFailure(Call<TravelNotesUserItemResult> call, Throwable t) {
                interfaceVideo.getDateList(new ArrayList());
            }
        });
    }

    /**
     * 添加一个游记收藏
     * @param context
     * @param dowhat
     */
    public void getNotesStart(final Context context,String dowhat,int userID,int tn_id,int tab2){
        Call<BooleanResult> call = GetStartModel.getModel().getService().getResult(dowhat,userID,tn_id,tab2);

        call.enqueue(new Callback<BooleanResult>() {
            @Override
            public void onResponse(Call<BooleanResult> call, Response<BooleanResult> response) {
                if (response.isSuccessful()) {
                    BooleanResult result = response.body();
                    interfaceBoolean.getBoolean(result.isFlag());
                }
            }

            @Override
            public void onFailure(Call<BooleanResult> call, Throwable t) {
                Toast.makeText(context,"暂无更多",Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 查询所有教程
     * @param context
     * @param dowhat
     */
    public void getAllTeach (final Context context,String dowhat,int i,int n){
        mCache = ACache.get(context);    //实例化缓存
        Call<NewTeachResult> call = NewTeachModel.getModel().getService().getResult(dowhat,i,n);

        call.enqueue(new Callback<NewTeachResult>() {
            @Override
            public void onResponse(Call<NewTeachResult> call, Response<NewTeachResult> response) {
                if (response.isSuccessful()) {
                    NewTeachResult result = response.body();
                    if(result.getNewTeachBean().size() > 0){
                        mCache.put("newTeachResult",result);
                        interfaceVideo.getDateList((ArrayList) result.getNewTeachBean());
                    }else{
                        interfaceVideo.getDateList(new ArrayList());
                        Toast.makeText(context,"暂无更多数据...",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<NewTeachResult> call, Throwable t) {
                interfaceVideo.getDateList(new ArrayList());
                Toast.makeText(context,"请检查网络了解...",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 查询器械或非器械所有教程
     * @param context
     * @param dowhat
     */
    public void getAllTeachTab1 (final Context context,String dowhat,int tab1){
        Call<NewTeachResult> call = NewTeachTab1Model.getModel().getService().getResult(dowhat,tab1);

        call.enqueue(new Callback<NewTeachResult>() {
            @Override
            public void onResponse(Call<NewTeachResult> call, Response<NewTeachResult> response) {
                if (response.isSuccessful()) {
                    NewTeachResult result = response.body();
                    if (result.getError() == 0) {
                        interfaceVideo.getDateList((ArrayList) result.getNewTeachBean());
                    }else{
                        Toast.makeText(context,"暂无更多",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<NewTeachResult> call, Throwable t) {
                Toast.makeText(context,"暂无更多",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 查询身体不同部位所有教程
     * @param context
     * @param dowhat
     */
    public void getAllTeachTab2 (final Context context, String dowhat, final int tab2){
        mCache = ACache.get(context);    //实例化缓存
        Call<NewTeachResult> call = NewTeachTab2Model.getModel().getService().getResult(dowhat,tab2);

        call.enqueue(new Callback<NewTeachResult>() {
            @Override
            public void onResponse(Call<NewTeachResult> call, Response<NewTeachResult> response) {
                if (response.isSuccessful()) {
                    NewTeachResult result = response.body();
                    if (result.getError() == 0) {
                        mCache.put("newTeachResult" + tab2,result);
                        interfaceVideo.getDateList((ArrayList) result.getNewTeachBean());
                    }else{
                        Toast.makeText(context,"暂无更多数据...",Toast.LENGTH_SHORT).show();
                        interfaceVideo.getDateList((ArrayList) result.getNewTeachBean());
                    }
                }
            }

            @Override
            public void onFailure(Call<NewTeachResult> call, Throwable t) {
                Toast.makeText(context,"请检查您的网络...",Toast.LENGTH_SHORT).show();
                interfaceVideo.getDateList(new ArrayList());
            }
        });
    }

    /**
     * 查询身体不同部位所有教程
     * @param context
     * @param dowhat
     */
    public void getAllTeachTab (final Context context,String dowhat,int tab1,int tab2){
        Call<NewTeachResult> call = NewTeachTabModel.getModel().getService().getResult(dowhat,tab1,tab2);

        call.enqueue(new Callback<NewTeachResult>() {
            @Override
            public void onResponse(Call<NewTeachResult> call, Response<NewTeachResult> response) {
                if (response.isSuccessful()) {
                    NewTeachResult result = response.body();
                    if (result.getError() == 0) {
                        interfaceVideo.getDateList((ArrayList) result.getNewTeachBean());
                    }else{
                        Toast.makeText(context,"暂无更多",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<NewTeachResult> call, Throwable t) {
                Toast.makeText(context,"暂无更多",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 查询训练计划
     * @param context
     * @param dowhat
     */
    public void getAllTeachPlan (final Context context,String dowhat,int teachID){
        Call<NewTeachPlanResut> call = NewTeachPlanModel.getModel().getService().getResult(dowhat,teachID);

        call.enqueue(new Callback<NewTeachPlanResut>() {
            @Override
            public void onResponse(Call<NewTeachPlanResut> call, Response<NewTeachPlanResut> response) {
                if (response.isSuccessful()) {
                    NewTeachPlanResut result = response.body();
                    if (result.getError() == 0) {
                        interfaceVideo.getDateList((ArrayList) result.getNewTeachMenu());
                    }else{
                        Toast.makeText(context,"暂无更多",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<NewTeachPlanResut> call, Throwable t) {
                Toast.makeText(context,"暂无更多",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 查询gif动画
     * @param dowhat
     */
    public void getAllTeachGifIGifImage (final Context context, String dowhat, final int teachID){
        mCache = ACache.get(context);    //实例化缓存
        Call<NewTeachGifImageResult> call = NewTeachGifImageModel.getModel().getService().getResult(dowhat,teachID);

        call.enqueue(new Callback<NewTeachGifImageResult>() {
            @Override
            public void onResponse(Call<NewTeachGifImageResult> call, Response<NewTeachGifImageResult> response) {
                if (response.isSuccessful()) {
                    NewTeachGifImageResult result = response.body();
                    if (result.getError() == 0 && result.getNewTeachGifImageBean().size() != 0) {
                        mCache.put("newTeachGifImageResult" + String.valueOf(teachID),result);
                        interfaceVideo.getDateList((ArrayList) result.getNewTeachGifImageBean());
                    }else{
                        Toast.makeText(context,"暂无更多",Toast.LENGTH_SHORT).show();
                    }
                }else {

                }
            }

            @Override
            public void onFailure(Call<NewTeachGifImageResult> call, Throwable t) {
                Toast.makeText(context,"暂无更多",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 发布游记
     * @param dowhat
     */
    public void AddNotesUpLoad (final Context context,String dowhat,int tn_id,String tn_name,StringBuffer tn_image,String tn_text){
        Call<BooleanResult> call = AddNotesUpLoadModel.getModel().getService().getResult(dowhat,tn_id,tn_name,tn_image,tn_text);

        call.enqueue(new Callback<BooleanResult>() {
            @Override
            public void onResponse(Call<BooleanResult> call, Response<BooleanResult> response) {
                if (response.isSuccessful()) {
                    BooleanResult result = response.body();
                        //发布成功
                        interfaceBoolean.getBoolean(result.isFlag());
                    Toast.makeText(context,"成功",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BooleanResult> call, Throwable t) {
                Toast.makeText(context,"暂无更多",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 发布主题
     * @param dowhat
     */
    public void AddNotesThemeUpLoad (final Context context,String dowhat,String tn_title,String tn_address,int userID){
        Call<GetIntId> call = AddNotesThemeUpLoadModel.getModel().getService().getResult(dowhat,tn_title,tn_address,userID);

        call.enqueue(new Callback<GetIntId>() {
            @Override
            public void onResponse(Call<GetIntId> call, Response<GetIntId> response) {
                if (response.isSuccessful()) {
                    GetIntId result = response.body();
                    //发布成功
                    interfaceIntId.getDateIntId(result.getIntId());
                    Toast.makeText(context,"成功" + result.getIntId(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetIntId> call, Throwable t) {
                Toast.makeText(context,"暂无更多",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 获取歌手图片
     */
    public void getMusicSiner (String appiKey,String userName){
        Call<MusicSinerResult> call = MusicSinerModel.getModel().getService().getResult(appiKey,userName);

        call.enqueue(new Callback<MusicSinerResult>() {
            @Override
            public void onResponse(Call<MusicSinerResult> call, Response<MusicSinerResult> response) {
                if (response.isSuccessful()) {
                    MusicSinerResult result = response.body();
                    if(result.getCode() == 0){
                        sinerListener.getDateList(result.getData());
                    }else{
                        Log.i("错误码","---------" + result.getCode() + "---------");
                    }

                }
            }

            @Override
            public void onFailure(Call<MusicSinerResult> call, Throwable t) {
                Toast.makeText(context,"暂无更多",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 获取轮播图
     */
    public void getLunBoTu (Context context, String dowhat, final int tab, int i, int n){
        mCache = ACache.get(context);    //实例化缓存
        Call<LunBoTuBeanResult> call = LunBoTuModel.getModel().getService().getResult(dowhat,tab,i,n);

        call.enqueue(new Callback<LunBoTuBeanResult>() {
            @Override
            public void onResponse(Call<LunBoTuBeanResult> call, Response<LunBoTuBeanResult> response) {
                if (response.isSuccessful()) {
                    LunBoTuBeanResult result = response.body();
                    if(result.getError() == 0){
                        if(tab == 1){
                            mCache.put("lunBoTuBeanResultHappy",result);
                        }else if(tab == 2){
                            mCache.put("lunBoTuBeanResultTravel",result);
                        }else if(tab == 3){
                            mCache.put("lunBoTuBeanResultBuilding",result);
                        }

                        interfaceVideo.getDateList((ArrayList) result.getLunboTuBean());

                    }else{
                        Log.i("错误码","---------" + result.getError() + "---------");
                    }

                }
            }

            @Override
            public void onFailure(Call<LunBoTuBeanResult> call, Throwable t) {
            }
        });
    }

    /**
     * 获取apk版本
     */
    public void getApkVersion (String dowhat){
        Call<ApkVersionBean> call = ApkVersionModel.getModel().getService().getResult(dowhat,"chislim");

        call.enqueue(new Callback<ApkVersionBean>() {
            @Override
            public void onResponse(Call<ApkVersionBean> call, Response<ApkVersionBean> response) {
                if (response.isSuccessful()) {
                    ApkVersionBean result = response.body();
                    apkVersionListener.getDateApkVersion(result.getApkVersion(),result.getApkText());

                }else{

                }
            }
            @Override
            public void onFailure(Call<ApkVersionBean> call, Throwable t) {
            }
        });
    }


    /**
     * 游记留言
     */
    public void TravelMessageSend (String dowhat,int tra_tm_id,int tn_id,int userID,String tm_tex){
        Call<BooleanResult> call = TravelMessageModel.getModel().getService().getResult(dowhat,tra_tm_id,tn_id,userID,tm_tex);

        call.enqueue(new Callback<BooleanResult>() {
            @Override
            public void onResponse(Call<BooleanResult> call, Response<BooleanResult> response) {
                if (response.isSuccessful()) {
                    BooleanResult result = response.body();
                    interfaceBoolean.getBoolean(result.isFlag());
                }else{

                }
            }

            @Override
            public void onFailure(Call<BooleanResult> call, Throwable t) {
                Toast.makeText(context,"暂无更多",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 获取首页数据
     */
    public void GetMainData (final Context context, String dowhat, int talk, int talkNum, int activity, int activityNum){
        mCache = ACache.get(context);    //实例化缓存
        Call<MainBeanResult> call = MainBeanModel.getModel().getService().getResult(dowhat,talk,talkNum,activity,activityNum);

        call.enqueue(new Callback<MainBeanResult>() {
            @Override
            public void onResponse(Call<MainBeanResult> call, Response<MainBeanResult> response) {
                if (response.isSuccessful()) {
                    MainBeanResult result = response.body();
                    mCache.put("mainData",result);
                    getObjectListener.getObject(result);
                }else{

                }
            }
            @Override
            public void onFailure(Call<MainBeanResult> call, Throwable t) {
                Toast.makeText(context,"暂无更多",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 获取首页数据
     */
    public void GetSportData (final Context context, String dowhat, int userID, String dayTime, int distance, int calories,int sportTime,String successes,int project){
        mCache = ACache.get(context);    //实例化缓存
        Call<BooleanResult> call = AddSportModel.getModel().getService().getResult(dowhat,userID,dayTime,distance,calories,sportTime,successes,project);

        call.enqueue(new Callback<BooleanResult>() {
            @Override
            public void onResponse(Call<BooleanResult> call, Response<BooleanResult> response) {
                if (response.isSuccessful()) {
                    BooleanResult result = response.body();
                }else{

                }
            }
            @Override
            public void onFailure(Call<BooleanResult> call, Throwable t) {
                Toast.makeText(context,"暂无更多",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
