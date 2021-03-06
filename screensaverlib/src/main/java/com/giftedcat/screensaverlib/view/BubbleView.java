package com.giftedcat.screensaverlib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.giftedcat.screensaverlib.manager.ScreenSaverManager;
import com.giftedcat.screensaverlib.util.TypedValueUtil;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * author : GiftedCat
 * date   : 2022-04-07
 * desc   : 气泡view
 */
public class BubbleView extends View {

    /**
     * 以一定的速率变小,甚至消失
     */

    /*** 标记是否运行 */
    private boolean isStop = false;

    /**
     * 是否生成气泡
     */
    private boolean generateState;

    /*** 视图背景色 */
    private int backGroundColor = Color.parseColor("#32000000");

    private Paint mPaint = new Paint();

    private Random rd = new Random();

    private int color;

    /*** 气泡 */
    private BubbleController mBubbleController;
    /*** 气泡的最小半径 */
    private float minBubbleRadius = 10;
    /*** 气泡的最大半径 */
    private float maxBubbleRadius = 30;
    /*** 半径变化率 */
    private float radiusRadio = 0.1f;
    /*** 气泡的最小速度 */
    private float minBubbleSpeedY = 4;
    /*** 气泡的最大速度 */
    private float maxBubbleSpeedY = 10;
    /*** 气泡最多有几个 */
    private int maxBubbleCount = 100;

    private boolean isShowing = false;

    private Handler handler = new Handler();

    private WindowManager wm;
    WindowManager.LayoutParams wmParams;

    public BubbleView(Context context) {
        this(context, null);
    }

    public BubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wmParams == null) {
            wmParams = new WindowManager.LayoutParams();
        }
        init(context);
    }

    private void init(final Context context) {
        //设置高亮
        setKeepScreenOn(true);

        //设置气泡的最小半径和最大半径
        minBubbleRadius = TypedValueUtil.dp2px(context, minBubbleRadius);
        maxBubbleRadius = TypedValueUtil.dp2px(context, maxBubbleRadius);


        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {

                //气泡
                mBubbleController = new BubbleController.BubbleControllerBuilder()
                        .setColor(color)
                        .setRd(rd)
                        .setMinBubbleRadius(minBubbleRadius)
                        .setMaxBubbleRadius(maxBubbleRadius)
                        .setRadiusRadio(radiusRadio)
                        .setMinBubbleSpeedY(minBubbleSpeedY)
                        .setMaxBubbleSpeedY(maxBubbleSpeedY)
                        .setMaxBubbleCount(maxBubbleCount)
                        .setViewWidth(getWidth())
                        .setViewHeight(getHeight())
                        .build();

                changeColor(color);

                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);

        new Thread(mBubbleRunnable).start();
    }

    @Override
    public void onDrawForeground(Canvas canvas) {
        super.onDrawForeground(canvas);
        //设置背景色
        setBackgroundColor(backGroundColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制气泡
        if (mBubbleController != null) {
            mBubbleController.drawBubble(canvas, mPaint);
        }
        if (!isStop)
            handler.postDelayed(mBubbleRunnable, 16);
    }

    private Runnable mBubbleRunnable = new Runnable() {
        @Override
        public void run() {

            if (generateState) {
                if (mBubbleController != null) {
                    //生成气泡
                    mBubbleController.generateBubble();
                    //遍历气泡
                    mBubbleController.performTraversals();
                }

                postInvalidate();
            }

        }
    };

    public BubbleView setBackground(int color) {
        backGroundColor = color;
        setBackgroundColor(backGroundColor);
        return this;
    }

    /**
     * 改变颜色
     *
     * @param color
     */
    public BubbleView changeColor(int color) {
        //颜色如果一样,则不用改变颜色
        if (color == this.color) return this;
        this.color = color;

        if (mBubbleController != null) {
            mBubbleController.setColor(color);
        }

        return this;
    }


    /**
     * 设置气泡最小半径
     */
    public BubbleView setMinBubbleRadius(float minBubbleRadius) {
        this.minBubbleRadius = minBubbleRadius;
        return this;
    }

    /**
     * 设置气泡最大半径
     */
    public BubbleView setMaxBubbleRadius(float maxBubbleRadius) {
        this.maxBubbleRadius = maxBubbleRadius;
        return this;
    }

    /**
     * 设置气泡大小变化率
     */
    public BubbleView setRadiusRadio(float radiusRadio) {
        this.radiusRadio = radiusRadio;
        return this;
    }

    /**
     * 设置气泡最小速度
     */
    public BubbleView setMinBubbleSpeedY(float minBubbleSpeedY) {
        this.minBubbleSpeedY = minBubbleSpeedY;
        return this;
    }

    /**
     * 设置气泡最大速度
     */
    public BubbleView setMaxBubbleSpeedY(float maxBubbleSpeedY) {
        this.maxBubbleSpeedY = maxBubbleSpeedY;
        return this;
    }

    /**
     * 设置气泡最大数量
     */
    public BubbleView setMaxBubbleCount(int maxBubbleCount) {
        this.maxBubbleCount = maxBubbleCount;
        return this;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        generateState = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        generateState = false;
    }

    public void destroy() {
        isStop = true;
    }

    public void show() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        // 注意TYPE_SYSTEM_ALERT从Android8.0开始被舍弃了
//            wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
//        } else {
        // 从Android8.0开始悬浮窗要使用TYPE_APPLICATION_OVERLAY
//            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//        }
        wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;

        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        wmParams.alpha = 1.0f;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        wmParams.x = 0;
        wmParams.y = 0;
        wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        // 显示自定义悬浮窗口
        wm.addView(this, wmParams);
        isShowing = true;
    }

    public void dismiss() {
        wm.removeView(this);
        isShowing = false;
    }

    public boolean isShowing() {
        return isShowing;
    }
}
