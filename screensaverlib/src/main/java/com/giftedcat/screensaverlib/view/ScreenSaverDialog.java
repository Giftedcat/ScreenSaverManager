package com.giftedcat.screensaverlib.view;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.giftedcat.screensaverlib.R;

public class ScreenSaverDialog extends DialogFragment {

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.dialog_fragment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_screen_saver, container, false);

        bvScreenSaver = view.findViewById(R.id.bv_screen_saver);
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
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
