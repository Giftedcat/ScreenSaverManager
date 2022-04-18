package com.giftedcat.screensavermanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.giftedcat.screensaverlib.manager.ScreenSaverManager;
import com.giftedcat.screensaverlib.view.BubbleView;
import com.giftedcat.screensavermanager.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ScreenSaverManager.getInstance()
                .init(this)
                .setBackgroundColorResource(R.color.black)
                .setCountDownTime(10)
                .setBubbleColorResource(R.color.white)
                .setMinBubbleRadius(10)
                .setMaxBubbleRadius(30)
                .setRadiusRadio(0.1f)
                .setMinBubbleSpeedY(4)
                .setMaxBubbleSpeedY(10)
                .setMaxBubbleCount(20);

        findViewById(R.id.tv_hello).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ScreenSaverManager.getInstance().destroy();
    }
}
