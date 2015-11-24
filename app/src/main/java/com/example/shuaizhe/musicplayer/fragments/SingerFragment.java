package com.example.shuaizhe.musicplayer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shuaizhe.musicplayer.R;

/**
 * Created by shuaizhe on 10/19/2015.
 */
public class SingerFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle saved){
        View view = inflater.inflate(R.layout.singer_list_layout,null);
        return view;
    }
}
