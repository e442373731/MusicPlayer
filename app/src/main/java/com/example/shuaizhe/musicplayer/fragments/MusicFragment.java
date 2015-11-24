package com.example.shuaizhe.musicplayer.fragments;

import android.app.Activity;
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
import com.example.shuaizhe.musicplayer.utils.MusicLoder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shuaizhe on 10/19/2015.
 */
public class MusicFragment extends Fragment
        implements View.OnClickListener{
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

    @Override
    public void onCreate(Bundle save) {
        Log.d(TAG, "onCreate");
        super.onCreate(save);
        Worker worker = new Worker();
        worker.execute();
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
        Log.d(TAG,"onActivityCreated");
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

    private void changeOrPauseMusic(int position){
        if(position == mCurrentPosition){
            if(mIsplaying){
                mPlayingButton.setImageResource(R.drawable.stop_selector);
                //TODO:stop play music
                mIsplaying = false;
            }else {
                mPlayingButton.setImageResource(R.drawable.playing_selector);
                //TODO:continue music
                mIsplaying = true;
            }
        }else {
            mIsplaying = true;
            mCurrentPosition = position;
            mPlayingButton.setImageResource(R.drawable.playing_selector);
            mMusicAdapter.setmCurrentPosition(position);
            mBlowMusicName.setText(infos.get(position).getMusicName());
            mBlowArtistName.setText(infos.get(position).getArtist());
        }
    }

    private void initViews(){
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
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        outState.putParcelableArrayList("Data", (ArrayList<MusicInfo>) infos);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //TODO:set on click listener
        }
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
            mMusicAdapter = new MusicAdapter(infos, getActivity());
            mListView.setAdapter(mMusicAdapter);
            super.onPostExecute(aVoid);
        }
    }

}
