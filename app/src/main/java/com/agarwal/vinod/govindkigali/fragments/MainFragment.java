package com.agarwal.vinod.govindkigali.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.adapters.SongAdapter;
import com.agarwal.vinod.govindkigali.api.SongService;
import com.agarwal.vinod.govindkigali.models.Song;
import com.agarwal.vinod.govindkigali.playerUtils.PlayerCommunication;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    public RecyclerView rvPlayList;
    public static final String TAG = "MAIN";
    private static ArrayList<Song> songlist = new ArrayList<>();
    SongAdapter adapter;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mainFragment = inflater.inflate(R.layout.fragment_main, container, false);

        rvPlayList = mainFragment.findViewById(R.id.rv_playlist);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvPlayList.setLayoutManager(layoutManager);
        adapter = new SongAdapter(getContext(), new ArrayList<Song>(), (PlayerCommunication) getActivity());
        rvPlayList.setAdapter(adapter);
        rvPlayList.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(RecyclerView.ViewHolder viewHolder) {
                //Log.d(TAG, "onAnimationFinished: Fuck yrself");
                //Toast.makeText(getContext(), "Fuccckkkkk", Toast.LENGTH_SHORT).show();
            }
        });

        if (songlist.size() == 0) {
            Log.d(TAG, "onCreateView: ======== Empty");
            SongService.getSongApi().getTracks().enqueue(new Callback<ArrayList<Song>>() {
                @Override
                public void onResponse(Call<ArrayList<Song>> call, Response<ArrayList<Song>> response) {
                    songlist = response.body();
                    Log.d(TAG, "onResponse: " + songlist.get(0).getTitle());
                    adapter.updateTracks(response.body());
                }

                @Override
                public void onFailure(Call<ArrayList<Song>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            adapter.updateTracks(songlist);
        }

        return mainFragment;
    }

    public void setSongAdapterFilter(String text){
        adapter.filter(text);
    }
}
