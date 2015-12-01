package com.example.shuaizhe.musicplayer.fragments;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.shuaizhe.musicplayer.R;
import com.example.shuaizhe.musicplayer.adapter.MusicAdapter;
import com.example.shuaizhe.musicplayer.bean.MusicInfo;
import com.example.shuaizhe.musicplayer.service.PlayingService;
import com.example.shuaizhe.musicplayer.utils.MusicLoder;
import com.example.shuaizhe.musicplayer.utils.UtilApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shuaizhe on 10/19/2015.
 */
public class MusicFragment extends Fragment
        implements View.OnClickListener {
    private MusicLoder mMusicLoder;
    private MusicAdapter mMusicAdapter;
    private List<MusicInfo> infos = new ArrayList<>();
    private ListView mListView;
    private TextView mEmpty;
    private ProgressBar mProgressBar;

    private View mView;
    private TextView mBlowMusicName;
    private TextView mBlowArtistName;
    private ImageView mPreButton;
    private ImageView mNextButton;
    private ImageView mPlayingButton;

    private static final String TAG = "MusicFragment";
    private int mCurrentPosition = -1;
    private boolean mIsplaying = false;
    private boolean mIsStoping = true;

    private UtilApplication mApplication;
    private HomeReceiver mReceiver;

    public static final String UPDATE_ACTION = "com.shuaizheng.update.status.action";
    public static final String NEXT_MUSIC_ACTION = "com.shuaizheng.next.music.action";
    public static final String MUSIC_DURATION = "com.shuaizheng.music.duration";
    public static final String CURRENT_DURATION = "com.shuaizheng.current.duration";

    @Override
    public void onCreate(Bundle save) {
        Log.d(TAG, "onCreate");
        super.onCreate(save);
        mApplication = (UtilApplication) getActivity().getApplication();
        Worker worker = new Worker();
        worker.execute();
        mReceiver = new HomeReceiver();
        IntentFilter filter = new IntentFilter(NEXT_MUSIC_ACTION);
        getActivity().registerReceiver(mReceiver, filter);
    }

    //at first
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "onAttach");
    }

    //at last
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved) {
        Log.d(TAG, "onCreateView");
        if (saved != null) {
            infos = saved.getParcelableArrayList("Data");
        }

        if (mView == null) {
            initViews();
            mView = inflater.inflate(R.layout.music_list_layout, null);
            mListView = (ListView) mView.findViewById(R.id.music_list_view);
            mEmpty = (TextView) mView.findViewById(R.id.is_empty);
            mProgressBar = (ProgressBar) mView.findViewById(R.id.progress);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    changeOrPauseMusic(position);
                }
            });
        }
        return mView;
    }

    private void changeOrPauseMusic(int position) {
        if (position == mCurrentPosition) {
            if (mIsplaying) {
                mPlayingButton.setImageResource(R.drawable.stop_selector);
                //TODO:stop play music
                stopPlayMusic();
                mIsplaying = false;
            } else {
                mPlayingButton.setImageResource(R.drawable.playing_selector);
                //TODO:continue music
                playMusic();
                mIsplaying = true;
            }
        } else {
            mIsplaying = true;
            mCurrentPosition = position;
            mPlayingButton.setImageResource(R.drawable.playing_selector);
            mMusicAdapter.setmCurrentPosition(position);
            mBlowMusicName.setText(infos.get(position).getMusicName());
            mBlowArtistName.setText(infos.get(position).getArtist());
            playMusic();
        }
    }

    private void changeUI(int position){
        mIsplaying = true;
        mCurrentPosition = position;
        mPlayingButton.setImageResource(R.drawable.playing_selector);
        mMusicAdapter.setmCurrentPosition(position);
        mBlowMusicName.setText(infos.get(position).getMusicName());
        mBlowArtistName.setText(infos.get(position).getArtist());
    }

    private void initViews() {
        mBlowArtistName = (TextView) getActivity().findViewById(R.id.blow_playing_artist_name);
        mBlowMusicName = (TextView) getActivity().findViewById(R.id.blow_playing_music_name);
        mPreButton = (ImageView) getActivity().findViewById(R.id.pre_button);
        mNextButton = (ImageView) getActivity().findViewById(R.id.next_button);
        mPlayingButton = (ImageView) getActivity().findViewById(R.id.playing_button);
        mPreButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        mPlayingButton.setOnClickListener(this);
    }

    private void showEmpty() {
        if (infos.size() == 0) {
            mEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        outState.putParcelableArrayList("Data", (ArrayList<MusicInfo>) infos);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //TODO:set on click listener
        }
    }

    private void playMusic() {
        Intent intent = new Intent(getActivity(), PlayingService.class);
        intent.putExtra("playing", true);
        intent.putExtra("Url", infos.get(mCurrentPosition).getUrl());
        intent.putExtra("position", mCurrentPosition);
        intent.putExtra("musicSize", infos.size());
        getActivity().startService(intent);
    }

    /**
     * TODO
     */
    private void stopPlayMusic() {
        Intent intent = new Intent(getActivity(), PlayingService.class);
        intent.putExtra("playing", false);
        getActivity().startService(intent);
    }

    private class Worker extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            infos = new ArrayList<>();
            mMusicLoder = new MusicLoder(getActivity().getContentResolver());
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            infos = mMusicLoder.loader();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mProgressBar.setVisibility(View.GONE);
            showEmpty();
            mApplication.setMusicInfos(infos);
            mMusicAdapter = new MusicAdapter(infos, getActivity());
            mListView.setAdapter(mMusicAdapter);
            super.onPostExecute(aVoid);
        }
    }

    /**
     * 监听Service的接收器
     */
    private class HomeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(NEXT_MUSIC_ACTION.equals(action)){
                int current = intent.getIntExtra("CurrentPosition",0);
                changeUI(current);
            }
        }
    }

}
