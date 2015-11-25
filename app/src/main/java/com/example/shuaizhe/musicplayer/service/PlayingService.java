package com.example.shuaizhe.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * Created by shuaizheng on 15/11/25.
 */
public class PlayingService extends Service {
    private MediaPlayer mPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        mPlayer = new MediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean isPlay = intent.getBooleanExtra("playing", false);
        if(isPlay){
            String url = intent.getStringExtra("Url");
            if(url != null){
                try {
                    if(mPlayer.isPlaying()){
                        mPlayer.stop();
                    }
                    mPlayer.reset();
                    mPlayer.setDataSource(url);
                    mPlayer.prepare();
                    mPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            mPlayer.pause();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.release();
        stopSelf();
    }
}
