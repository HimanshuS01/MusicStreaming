package com.agarwal.vinod.govindkigali.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agarwal.vinod.govindkigali.MainActivity;
import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.models.Song;
import com.agarwal.vinod.govindkigali.playerUtils.PlayerCommunication;
import com.agarwal.vinod.govindkigali.playerUtils.PlayerService;
import com.agarwal.vinod.govindkigali.utils.PrefManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;


/**
 * Created by darsh on 11/12/17.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private ArrayList<Song> playList = new ArrayList<>();
    public ArrayList<Song> playListOnDisplay = new ArrayList<>();
    private String currentQuery;
    private Context context;
    private PlayerCommunication playerCommunication;
    public static final String TAG = "SA";

    public SongAdapter(Context context, ArrayList<Song> playList, PlayerCommunication playerCommunication) {
        this.context = context;
        this.playList = playList;
        playListOnDisplay.addAll(playList);
        currentQuery = "";
        this.playerCommunication = playerCommunication;
    }

    public void updateTracks(ArrayList<Song> playList) {
        Log.d(TAG, "updateNews: " + playList.size());
        this.playList = playList;
        notifyDataSetChanged();
        filter(currentQuery);
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        return new SongViewHolder(inflater.inflate(R.layout.layout_song, parent, false));
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        holder.bindView(playListOnDisplay.get(position), position);
    }

    @Override
    public int getItemCount() {
        return playListOnDisplay.size();
    }

    public void filter(String text) {
        currentQuery = text;
        playListOnDisplay.clear();
        notifyDataSetChanged();
        if(text.isEmpty()){
            playListOnDisplay.addAll(playList);
            notifyDataSetChanged();
        } else{
            text = text.toLowerCase();
            for(Song item: playList){
//                if(item.getTitle().toLowerCase().contains(text) || item.getDescription().toLowerCase().contains(text)){
                if(item.getTitle().toLowerCase().contains(text)){
                    playListOnDisplay.add(item);
//                    notifyItemInserted(playListOnDisplay.size() - 1);

                }
            }
        }
        notifyDataSetChanged();
    }

    class SongViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        ImageView ivImage;
        LinearLayout llSong;

        private SongViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            ivImage = itemView.findViewById(R.id.iv_image);
            llSong = itemView.findViewById(R.id.ll_song);
            PrefManager preferenceManager = new PrefManager(context);
            if(preferenceManager.isNightModeEnabled2()){
                llSong.setBackgroundColor(Color.parseColor("#000000"));
                tvName.setTextColor(Color.parseColor("#ffffff"));
            }

        }

        void bindView(final Song song, final int pos) {
            tvName.setText(song.getTitle());

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.photo);
            requestOptions.error(R.drawable.photo);
            requestOptions.centerCrop();
            Glide.with(context).load(song.getArtwork_url()).apply(requestOptions).into(ivImage);

            llSong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ConnectivityManager cm =
                            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                    assert cm != null;
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    final boolean isConnected = activeNetwork != null &&
                            activeNetwork.isConnectedOrConnecting();
                    Log.d(TAG, "onClick: " + isConnected);
                    if (isConnected) {
                        PlayerService.focus = false;
                        playerCommunication.playSong(playList, pos);
                        Log.d(TAG, "onClick: checking loss first :)");
                    } else {
                        Toast.makeText(context, "Internet not available!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}
