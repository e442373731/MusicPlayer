package com.example.shuaizhe.musicplayer.utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.shuaizhe.musicplayer.bean.MusicInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shuaizhe on 10/19/2015.
 */
public class MusicLoder {
    private static final String TAG = "In Class MusicLoader";
    private List<MusicInfo> musics ;
    private ContentResolver contentResolver;
    private static final Uri MUSIC_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    private static final String MUSIC_SORT_ORDER = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;

    public MusicLoder(ContentResolver resolver){
        contentResolver = resolver;
        musics = new ArrayList<>();
    }

    public List<MusicInfo> loader(){
        Cursor cursor = contentResolver.query(MUSIC_URI,null,null,null,MUSIC_SORT_ORDER);
        if(cursor == null){
            Log.d(TAG, "  Cursor is Null");
        }else if(!cursor.moveToFirst()){
            Log.d(TAG, "  Cursor has no data!");
        }else {
            int displayNameCol = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
            int idCol = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int durationCol = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int sizeCol = cursor.getColumnIndex(MediaStore.Audio.Media.SIZE);
            int artistCol = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int urlCol = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            do {
                int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
                if(isMusic == 0){
                    String musicName = cursor.getString(displayNameCol);
                    String artist = cursor.getString(artistCol);
                    String url = cursor.getString(urlCol);
                    long id = cursor.getLong(idCol);
                    long duration = cursor.getLong(durationCol);
                    long size = cursor.getLong(sizeCol);

                    String[] name = musicName.split("\\.");
                    MusicInfo musicInfo = new MusicInfo();
                    musicInfo.setArtist(artist);
                    musicInfo.setDuration(duration);
                    musicInfo.setMusicId(id);
                    musicInfo.setMusicName(name[0]);
                    musicInfo.setSize(size);
                    musicInfo.setUrl(url);
                    musics.add(musicInfo);
                }
            }while (cursor.moveToNext());
        }
        return musics;
    }
}
