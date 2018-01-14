package com.agarwal.vinod.govindkigali.fragments.mymusic;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.adapters.SongAdapter;
import com.agarwal.vinod.govindkigali.models.Song;
import com.agarwal.vinod.govindkigali.playerUtils.PlayerCommunication;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubPlayListFragment extends Fragment {

    RecyclerView rvsubList;
    SongAdapter adapter;
    String name;
    DatabaseReference playListReference = FirebaseDatabase.getInstance().getReference("pop");
    private ArrayList<Song> subPlayList = new ArrayList<>();
    public static final String TAG = "FAV";
    public SubPlayListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getArguments().getString("Name");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View subFragment = inflater.inflate(R.layout.fragment_multi, container, false);

        rvsubList = subFragment.findViewById(R.id.rv_multi);

        Log.d(TAG, "onCreate: " + name);
        DatabaseReference subListReference = playListReference.child(name);

        subListReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                subPlayList.clear();
                for (DataSnapshot favSnapshot : dataSnapshot.getChildren()) {
                    Song song = favSnapshot.getValue(Song.class);
                    subPlayList.add(song);
                }
                adapter.updateTracks(subPlayList);
                Log.d(TAG, "onDataChange: :)   :)   :)");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: :(   :(   :(");
            }
        });

        rvsubList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SongAdapter(getContext(), new ArrayList<Song>(), (PlayerCommunication) getActivity());
        rvsubList.setAdapter(adapter);

        return subFragment;
    }

}
