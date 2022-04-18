package com.giftedcat.screensaverlib.manager;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.giftedcat.screensaverlib.view.BubbleView;
import com.giftedcat.screensaverlib.view.ScreenSaverDialog;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ScreenSaverManager {

    public static ScreenSaverManager instance;

    private Context mContext;

    private Handler mHandler = new Handler();

    private boolean isStop;

    /** 空闲时间*/
    private int freeTime;
    /** 屏保出现时间*/
    private int countdown = 60;

    private BubbleView bubbleView;

    public static ScreenSaverManager getInstance() {
        if (instance == null) {
            instance = new ScreenSaverManager();
        }

        return instance;
    }

    public ScreenSaverManager() {

    }

    public ScreenSaverManager init(Context context) {
        isStop = false;
        mContext = context;

        bubbleView = new BubbleView(mContext);
        bubbleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                active();
            }
        });

        new Thread(mRunnable).start();
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
        bubbleView.changeColor(color);
        return instance;
    }

    /**
     * 设置气泡颜色
     * */
    public ScreenSaverManager setBackgroundColorResource(int resource){
        bubbleView.setBackground(mContext.getResources().getColor(resource));
        return instance;
    }

    /**
     * 设置气泡颜色
     * */
    public ScreenSaverManager setBackgroundColor(int color){
        bubbleView.setBackground(color);
        return instance;
    }

    /**
     * 设置气泡颜色
     * */
    public ScreenSaverManager setBubbleColorResource(int resource){
        bubbleView.changeColor(mContext.getResources().getColor(resource));
        return instance;
    }

    /**
     * 设置气泡最小半径
     * */
    public ScreenSaverManager setMinBubbleRadius(float minBubbleRadius) {
        bubbleView.setMinBubbleRadius(minBubbleRadius);
        return instance;
    }

    /**
     * 设置气泡最大半径
     * */
    public ScreenSaverManager setMaxBubbleRadius(float maxBubbleRadius) {
        bubbleView.setMaxBubbleRadius(maxBubbleRadius);
        return instance;
    }

    /**
     * 设置气泡大小变化率
     * */
    public ScreenSaverManager setRadiusRadio(float radiusRadio) {
        bubbleView.setRadiusRadio(radiusRadio);
        return instance;
    }

    /**
     * 设置气泡最小速度
     * */
    public ScreenSaverManager setMinBubbleSpeedY(float minBubbleSpeedY) {
        bubbleView.setMinBubbleSpeedY(minBubbleSpeedY);
        return instance;
    }

    /**
     * 设置气泡最大速度
     * */
    public ScreenSaverManager setMaxBubbleSpeedY(float maxBubbleSpeedY) {
        bubbleView.setMaxBubbleSpeedY(maxBubbleSpeedY);
        return instance;
    }

    /**
     * 设置气泡最大数量
     * */
    public ScreenSaverManager setMaxBubbleCount(int maxBubbleCount) {
        bubbleView.setMaxBubbleCount(maxBubbleCount);
        return instance;
    }

    /**
     * 屏保重新计时
     * */
    public void active(){
        freeTime = 0;
        if (bubbleView != null && bubbleView.isShowing()){
            bubbleView.dismiss();
        }
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

            if (!isStop) {

                if (freeTime == countdown) {
                    bubbleView.show();
                }

                freeTime += 1;

                mHandler.postDelayed(this, 1000);
            }
        }
    };

    public void destroy() {
        isStop = true;
        bubbleView.destroy();
    }

}
