package com.giftedcat.screensaverlib.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.giftedcat.screensaverlib.R;

import java.util.Objects;

public class ScreenSaverDialog extends Dialog {

    private int bubbleColor = Color.parseColor("#ffffff");

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

    BubbleView bvScreenSaver;

    private OnBubbleViewClickListener listener;

    public ScreenSaverDialog(@NonNull Context context) {
        super(context, R.style.dialog_fragment);

        getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_screen_saver);

        WindowManager.LayoutParams params = Objects.requireNonNull(getWindow()).getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);

        bvScreenSaver = findViewById(R.id.bv_screen_saver);
        bvScreenSaver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null){
                    listener.onClick(v);
                }
            }
        });
        bvScreenSaver.changeColor(bubbleColor)
                .setMinBubbleRadius(minBubbleRadius)
                .setMaxBubbleRadius(maxBubbleRadius)
                .setRadiusRadio(radiusRadio)
                .setMinBubbleSpeedY(minBubbleSpeedY)
                .setMaxBubbleSpeedY(maxBubbleSpeedY)
                .setMaxBubbleCount(maxBubbleCount);
    }

    /**
     * 改变气泡颜色
     * */
    public void changeBubbleColor(int color){
        bubbleColor = color;
    }

    /**
     * 设置气泡最小半径
     * */
    public void setMinBubbleRadius(float minBubbleRadius) {
        this.minBubbleRadius = minBubbleRadius;
    }

    /**
     * 设置气泡最大半径
     * */
    public void setMaxBubbleRadius(float maxBubbleRadius) {
        this.maxBubbleRadius = maxBubbleRadius;
    }

    /**
     * 设置气泡大小变化率
     * */
    public void setRadiusRadio(float radiusRadio) {
        this.radiusRadio = radiusRadio;
    }

    /**
     * 设置气泡最小速度
     * */
    public void setMinBubbleSpeedY(float minBubbleSpeedY) {
        this.minBubbleSpeedY = minBubbleSpeedY;
    }

    /**
     * 设置气泡最大速度
     * */
    public void setMaxBubbleSpeedY(float maxBubbleSpeedY) {
        this.maxBubbleSpeedY = maxBubbleSpeedY;
    }

    /**
     * 设置气泡最大数量
     * */
    public void setMaxBubbleCount(int maxBubbleCount) {
        this.maxBubbleCount = maxBubbleCount;
    }

    public void setOnBubbleViewClickListener(OnBubbleViewClickListener listener){
        this.listener = listener;
    }

    public interface OnBubbleViewClickListener{

        void onClick(View view);

    }

}
