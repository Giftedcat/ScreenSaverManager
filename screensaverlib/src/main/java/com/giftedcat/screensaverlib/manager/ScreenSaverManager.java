package com.giftedcat.screensaverlib.manager;

import android.app.Activity;
import android.os.Handler;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.giftedcat.screensaverlib.view.ScreenSaverDialog;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ScreenSaverManager {

    private WeakReference<Activity> wrfActivity;

    public static ScreenSaverManager instance;

    /*** 线程池 */
    private Executor mExecutor;

    private Handler mHandler = new Handler();

    private boolean isStop;

    /** 空闲时间*/
    private int freeTime;
    /** 屏保出现时间*/
    private int countdown = 60;

    ScreenSaverDialog screenSaverDialog;

    public static ScreenSaverManager getInstance() {
        if (instance == null) {
            instance = new ScreenSaverManager();
        }

        return instance;
    }

    public ScreenSaverManager() {

    }

    public ScreenSaverManager init(Activity activity) {
        wrfActivity = new WeakReference<>(activity);
        isStop = false;


        screenSaverDialog = new ScreenSaverDialog();
        screenSaverDialog.setOnBubbleViewClickListener(new ScreenSaverDialog.OnBubbleViewClickListener() {
            @Override
            public void onClick(View view) {
                freeTime = 0;
            }
        });

        mExecutor = new ThreadPoolExecutor(3, 5, 15,
                TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(8));

        mExecutor.execute(mRunnable);
        return instance;
    }

    /**
     * 设置倒计时时长
     * */
    public ScreenSaverManager setCountDownTime(int countdown){
        this.countdown = countdown;
        freeTime = 0;
        return instance;
    }

    /**
     * 设置气泡颜色
     * */
    public ScreenSaverManager setBubbleColor(int color){
        screenSaverDialog.changeBubbleColor(color);
        return instance;
    }

    /**
     * 设置气泡颜色
     * */
    public ScreenSaverManager setBubbleColorResource(int resource){
        screenSaverDialog.changeBubbleColor(wrfActivity.get().getResources().getColor(resource));
        return instance;
    }

    /**
     * 设置气泡最小半径
     * */
    public ScreenSaverManager setMinBubbleRadius(float minBubbleRadius) {
        screenSaverDialog.setMinBubbleRadius(minBubbleRadius);
        return instance;
    }

    /**
     * 设置气泡最大半径
     * */
    public ScreenSaverManager setMaxBubbleRadius(float maxBubbleRadius) {
        screenSaverDialog.setMaxBubbleRadius(maxBubbleRadius);
        return instance;
    }

    /**
     * 设置气泡大小变化率
     * */
    public ScreenSaverManager setRadiusRadio(float radiusRadio) {
        screenSaverDialog.setRadiusRadio(radiusRadio);
        return instance;
    }

    /**
     * 设置气泡最小速度
     * */
    public ScreenSaverManager setMinBubbleSpeedY(float minBubbleSpeedY) {
        screenSaverDialog.setMinBubbleSpeedY(minBubbleSpeedY);
        return instance;
    }

    /**
     * 设置气泡最大速度
     * */
    public ScreenSaverManager setMaxBubbleSpeedY(float maxBubbleSpeedY) {
        screenSaverDialog.setMaxBubbleSpeedY(maxBubbleSpeedY);
        return instance;
    }

    /**
     * 设置气泡最大数量
     * */
    public ScreenSaverManager setMaxBubbleCount(int maxBubbleCount) {
        screenSaverDialog.setMaxBubbleCount(maxBubbleCount);
        return instance;
    }

    /**
     * 屏保重新计时
     * */
    public void active(){
        freeTime = 0;
        if (screenSaverDialog != null && screenSaverDialog.getDialog() != null && screenSaverDialog.getDialog().isShowing()){
            screenSaverDialog.dismiss();
        }
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

            if (wrfActivity.get() == null){
                /** 页面已销毁*/
                destroy();
            }

            if (!isStop) {

                if (freeTime == countdown) {
                    screenSaverDialog.show(((FragmentActivity) wrfActivity.get()).getSupportFragmentManager(), "");
                }

                freeTime += 1;

                mHandler.postDelayed(this, 1000);
            }
        }
    };

    public void destroy() {
        isStop = true;
    }

}
