package com.agarwal.vinod.govindkigali.adapters;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agarwal.vinod.govindkigali.MainActivity;
import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.fragments.MyMusicFragment;
import com.agarwal.vinod.govindkigali.fragments.mymusic.SubPlayListFragment;
import com.agarwal.vinod.govindkigali.models.Song;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by darsh on 15/12/17.
 */

public class PopListAdapter extends RecyclerView.Adapter<PopListAdapter.PopListViewHolder> {


    private ArrayList<String> playList = new ArrayList<>();
    private Context context;
    private Song song;
    private Boolean flag;
    private FragmentActivity activity;
    public static final String TAG = "POAD";

    public PopListAdapter(Context context, Song song, Boolean flag, FragmentActivity activity) {
        this.context = context;
        this.song = song;
        this.flag = flag;
        this.activity = activity;
    }

    public void updateList(ArrayList<String> playList) {
        Log.d(TAG, "updateNews: " + playList.size());
        this.playList = playList;
        notifyDataSetChanged();
    }

    @Override
    public PopListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new PopListViewHolder(inflater.inflate(R.layout.layout_song, parent, false));
    }

    @Override
    public void onBindViewHolder(PopListViewHolder holder, int position) {
        holder.bindView(playList.get(position));
    }

    @Override
    public int getItemCount() {
        return playList.size();
    }

    class PopListViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        LinearLayout llSong;
        PopListViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            llSong = itemView.findViewById(R.id.ll_song);
        }

        void bindView(final String name) {
            tvName.setText(name);

            if (flag) {
                llSong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseDatabase.getInstance().getReference("pop").child(name).child(song.getId()).setValue(song);
                        Toast.makeText(context, "Song added to " + name, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                llSong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SubPlayListFragment fragment = new SubPlayListFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("Name", name);
                        fragment.setArguments(bundle);
                        ++MainActivity.fragmentCheck;
                        activity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fg, fragment)
                                .addToBackStack("remove2")
                                .commit();
                    }
                });
            }
        }
    }
}
