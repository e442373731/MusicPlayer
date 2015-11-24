package com.example.shuaizhe.musicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shuaizhe.musicplayer.R;
import com.example.shuaizhe.musicplayer.bean.MusicInfo;

import java.util.List;

/**
 * Created by shuaizhe on 10/19/2015.
 */
public class MusicAdapter extends BaseAdapter {
    private List<MusicInfo> mMusics;
    private Context mContext;

    private int mCurrentPosition = -1;

    public MusicAdapter(List<MusicInfo> list, Context context) {
        mMusics = list;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mMusics.size();
    }

    @Override
    public Object getItem(int position) {
        return mMusics.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MusicInfo info = mMusics.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.music_list_item, null);
            holder = new ViewHolder();
            holder.count = (TextView) convertView.findViewById(R.id.count_text);
            holder.artistName = (TextView) convertView.findViewById(R.id.artist_name);
            holder.duration = (TextView) convertView.findViewById(R.id.music_duration);
            holder.musicName = (TextView) convertView.findViewById(R.id.music_name);
            holder.isPlaying = (ImageView) convertView.findViewById(R.id.playing_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.count.setText(String.valueOf((position + 1)));
        holder.musicName.setText(info.getMusicName());
        holder.duration.setText(getTime(info.getDuration()));
        holder.artistName.setText(info.getArtist());
        if(position == mCurrentPosition){
            holder.isPlaying.setVisibility(View.VISIBLE);
            holder.count.setVisibility(View.GONE);
        }else {
            holder.isPlaying.setVisibility(View.GONE);
            holder.count.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView count;
        TextView musicName;
        TextView artistName;
        TextView duration;
        ImageView isPlaying;
    }

    private String getTime(long duration) {
        StringBuilder time = new StringBuilder();
        duration = duration / 1000;
        long min = duration / 60;
        duration %= 60;
        time.append("" + min + ":");
        if (duration >= 10) {
            time.append(""+duration);
        }else {
            time.append("0"+duration);
        }
        return time.toString();
    }

    public void setmCurrentPosition(int position){
        mCurrentPosition = position;
        notifyDataSetInvalidated();
    }
}
