package com.example.shuaizhe.musicplayer.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.example.shuaizhe.musicplayer.bean.MusicInfo;
import com.example.shuaizhe.musicplayer.utils.UtilApplication;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by shuaizheng on 15/11/25.
 */
public class PlayingService extends Service {
    private MediaPlayer mPlayer;
    private String mUrl;
    private int mCurrent = 0;
    private int mStatus = 2;
    private StatusReceiver mReceiver;
    private int mSize;
    private List<MusicInfo> mInfos;
    private int mCurrentTime;

    public static final String UPDATE_ACTION = "com.shuaizheng.update.status.action";
    public static final String NEXT_MUSIC_ACTION = "com.shuaizheng.next.music.action";
    public static final String MUSIC_DURATION = "com.shuaizheng.music.duration";
    public static final String CURRENT_DURATION = "com.shuaizheng.current.duration";

    private Handler mDurationhandler = new Myhandler(PlayingService.this);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mReceiver = new StatusReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPDATE_ACTION);
        registerReceiver(mReceiver, filter);
        mInfos = ((UtilApplication)getApplication()).getMusicInfos();

        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                switch (mStatus) {
                    case 1:
                        //单曲循环
                        mPlayer.start();
                        break;
                    case 2:
                        //顺序播放
                        mCurrent++;
                        if(mCurrent > mSize - 1){
                            mCurrent = 0;
                        }
                        Intent sendIntent = new Intent(NEXT_MUSIC_ACTION);
                        sendIntent.putExtra("CurrentPosition",mCurrent);
                        mUrl = mInfos.get(mCurrent).getUrl();
                        sendBroadcast(sendIntent);
                        play(0);
                        break;
                    case 3:
                        //随机播放
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            boolean isPlay = intent.getBooleanExtra("playing", false);
            mSize = intent.getIntExtra("musicSize", 0);
            mCurrent = intent.getIntExtra("position", 0);
            if (isPlay) {
                String url = intent.getStringExtra("Url");
                if (url != null) {
                    try {
                        if (mPlayer.isPlaying()) {
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
            } else {
                mPlayer.pause();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPlayer != null){
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        unregisterReceiver(mReceiver);
        mDurationhandler.removeCallbacksAndMessages(null);
        stopSelf();
    }

    //TODO;
    private void play(int position) {
        try {
            mPlayer.reset();
            mPlayer.setDataSource(mUrl);
            mPlayer.prepare();
            mPlayer.setOnPreparedListener(new MyPreparedListener(position));
            mDurationhandler.sendEmptyMessage(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pause() {

    }

    private void stop() {

    }

    /**
     * 播放方式广播接收器
     */
    private class StatusReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int control = intent.getIntExtra("control", 0);
            switch (control) {
                case 1:
                    mStatus = 1;
                    break;
                case 2:
                    mStatus = 2;
                    break;
                case 3:
                    mStatus = 3;
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 准备好后的监听接口
     */
    private final class MyPreparedListener implements MediaPlayer.OnPreparedListener{
        private int currentTime;
        public MyPreparedListener(int position){
            currentTime = position;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();
            if(currentTime > 0){
                mp.seekTo(currentTime);
            }
            //TODO 通过广播更新UI中的歌曲时间 action为DURATION
        }
    }

    /**
     * 本地Handler
     */
    private static class Myhandler extends Handler{
        private WeakReference<PlayingService> service;

        public Myhandler(PlayingService playingService){
            service = new  WeakReference<>(playingService);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if(service.get().mPlayer != null){
                        service.get().mCurrentTime = service.get().mPlayer.getCurrentPosition();
                        Intent sendIntent = new Intent(CURRENT_DURATION);
                        sendIntent.putExtra("currentTime",service.get().mCurrentTime);
                        service.get().sendBroadcast(sendIntent);
                        sendEmptyMessageAtTime(1,1000);
                    }
            }
        }
    }

}
