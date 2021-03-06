package com.weiaibenpao.demo.chislim.sportoutdoor.presentation.presenter;


import com.weiaibenpao.demo.chislim.sportoutdoor.data.model.database.core.PedometerCardEntity;
import com.weiaibenpao.demo.chislim.sportoutdoor.data.model.pedometer.IGetPedometerResult;
import com.weiaibenpao.demo.chislim.sportoutdoor.data.model.pedometer.PedometerEvent;
import com.weiaibenpao.demo.chislim.sportoutdoor.data.repository.PedometerRepository;
import com.weiaibenpao.demo.chislim.sportoutdoor.presentation.common.BaseApplication;
import com.weiaibenpao.demo.chislim.sportoutdoor.presentation.module.ApplicationModule;
import com.weiaibenpao.demo.chislim.sportoutdoor.presentation.view.iview.IPedometerView;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by zoubo
 * 计步器presenter
 */
public class PedometerPresenter implements Presenter {
    private IPedometerView mView;
    private PedometerRepository mPedometerRepository;

    public PedometerPresenter(IPedometerView view) {
        BaseApplication.globalRegisterEvent(this);

        mView = view;
        mPedometerRepository = ApplicationModule.getInstance().getPedometerRepository();
    }

    @Override
    public void resume() {
        //第一次初始化数据:包括退出应用后再进入的数据初始化
        getPedometerStep();
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        BaseApplication.globalUnRegisterEvent(this);
    }

    /**
     * 监听数据
     */
    @Subscribe
    public void onEventMainThread(PedometerEvent event) {
        if (event.mIsUpdate) {
            getPedometerStep();
        }
    }

    /**
     * 获取计步器的数据
     */
    public void getPedometerStep() {
        mPedometerRepository.getPedometerStep(new IGetPedometerResult() {
            @Override
            public void onSuccessGet(PedometerCardEntity cardEntity) {
                if (mView != null) {
                    mView.onReaderPedometer(cardEntity);
                }
            }

        });
    }
}
