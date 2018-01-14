package com.agarwal.vinod.govindkigali.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.models.Song;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by darsh on 11/12/17.
 */

public class PlayListAdapter extends BaseAdapter {

    private ArrayList<Song> playList = new ArrayList<>();
    private Context context;

    public PlayListAdapter(ArrayList<Song> playList, Context context) {
        this.playList = playList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return playList.size();
    }

    @Override
    public Object getItem(int i) {
        return playList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rootView = view;
        if (rootView == null) {
//            rootView = LayoutInflater.from(context).inflate(R.layout.layout_item, null);
//
//            ImageView ivSong = rootView.findViewById(R.id.iv_song);
//
//            Glide.with(context).load(playList.get(i).getImageUrl())
//                    .into(ivSong);
        }
        return rootView;
    }
}
