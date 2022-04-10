package com.giftedcat.screensavermanager.activity;

import android.os.Bundle;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.giftedcat.screensaverlib.manager.ScreenSaverManager;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                /** 用户触摸屏幕后  屏保重新计时*/
                ScreenSaverManager.getInstance().active();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

}
