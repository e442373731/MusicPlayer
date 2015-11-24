package com.example.shuaizhe.musicplayer.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shuaizhe on 10/19/2015.
 */
public class MusicInfo implements Parcelable {
    private long musicId;
    private String musicName;
    private String artist;
    private long duration;
    private long size;
    private String url;

    public MusicInfo(){}

    public void setMusicId(long _id){
        musicId = _id;
    }

    public long getMusicId(){
        return musicId;
    }

    public void setMusicName(String name){
        musicName = name;
    }

    public String getMusicName(){
        return musicName;
    }

    public void setArtist(String _artist){
        artist = _artist;
    }

    public String getArtist(){
        return artist;
    }

    public void setDuration(long _duration){
        duration = _duration;
    }

    public long getDuration(){
        return duration;
    }

    public void setSize(long _size){
        size = _size;
    }

    public long getSize(){
        return size;
    }

    public void setUrl(String _url){
        url = _url;
    }

    public String getUrl(){
        return url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(musicId);
        dest.writeString(musicName);
        dest.writeString(artist);
        dest.writeLong(duration);
        dest.writeLong(size);
        dest.writeString(url);
    }

    public static final Parcelable.Creator<MusicInfo> CREATOR = new Parcelable.Creator<MusicInfo>(){

        @Override
        public MusicInfo createFromParcel(Parcel source) {
            return new MusicInfo(source);
        }

        @Override
        public MusicInfo[] newArray(int size) {
            return new MusicInfo[size];
        }
    };

    private MusicInfo(Parcel in){
        musicId = in.readLong();
        musicName = in.readString();
        artist = in.readString();
        duration = in.readLong();
        size = in.readLong();
        url = in.readString();
    }
}
