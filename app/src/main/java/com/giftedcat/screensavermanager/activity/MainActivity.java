package com.giftedcat.screensavermanager.activity;

import android.os.Bundle;

import com.giftedcat.screensaverlib.manager.ScreenSaverManager;
import com.giftedcat.screensavermanager.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ScreenSaverManager.getInstance()
                .init(this)
                .setCountDownTime(10)
                .setBubbleColorResource(R.color.white)
                .setMinBubbleRadius(10)
                .setMaxBubbleRadius(30)
                .setRadiusRadio(0.1f)
                .setMinBubbleSpeedY(4)
                .setMaxBubbleSpeedY(10)
                .setMaxBubbleCount(100);


    }
}
