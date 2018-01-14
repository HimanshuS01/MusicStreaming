package com.agarwal.vinod.govindkigali.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agarwal.vinod.govindkigali.MainActivity;
import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.models.Song;
import com.agarwal.vinod.govindkigali.playerUtils.PlayerCommunication;
import com.agarwal.vinod.govindkigali.playerUtils.PlayerService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

/**
 * Created by darsh on 22/12/17.
 */

public class SongImageAdapter extends RecyclerView.Adapter<SongImageAdapter.SongImageviewHolder> {

    Context context;
    ArrayList<Song> playList = new ArrayList<>();
    public static final String TAG = "SIA";
    private PlayerCommunication playerCommunication;

    public SongImageAdapter(Context context, PlayerCommunication playerCommunication) {
        this.context = context;
        this.playerCommunication = playerCommunication;
    }

    public void updateImage(ArrayList<Song> playList) {
        this.playList = playList;
        notifyDataSetChanged();
    }

    @Override
    public SongImageviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new SongImageviewHolder(inflater.inflate(R.layout.layout_image, parent, false));
    }

    @Override
    public void onBindViewHolder(SongImageviewHolder holder, int position) {
        holder.bindView(playList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return playList.size();
    }

    class SongImageviewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView tvName;

        SongImageviewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_Image);
            tvName = itemView.findViewById(R.id.tv_song_name);
        }

        void bindView(Song song, final Integer pos) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.photo);
            requestOptions.error(R.drawable.photo);
            requestOptions.centerCrop();
            Glide.with(context).load(song.getArtwork_url()).apply(requestOptions).into(ivImage);

            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ConnectivityManager cm =
                            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

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

            tvName.setText(song.getTitle());

        }
    }
}
