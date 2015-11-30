package com.example.shuaizhe.musicplayer.utils;

import android.app.Application;

import com.example.shuaizhe.musicplayer.bean.MusicInfo;

import java.util.List;

/**
 * Created by shuaizheng on 15/11/30.
 */
public class UtilApplication extends Application {
    private List<MusicInfo> mMusicInfos;

    public void setMusicInfos(List<MusicInfo> infos){
        mMusicInfos = infos;
    }

    public List<MusicInfo> getMusicInfos(){
        return mMusicInfos;
    }
}
