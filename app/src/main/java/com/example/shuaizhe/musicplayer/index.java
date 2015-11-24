package com.example.shuaizhe.musicplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by shuaizhe on 10/19/2015.
 */
public class index extends Activity {
    private Timer mTimer;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            mTimer.cancel();
            Intent intent = new Intent(index.this, MainActivity.class);
            startActivity(intent);
            finish();
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(Bundle save){
        super.onCreate(save);
        setContentView(R.layout.index);

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        }, 1500, 1000);
    }
}
