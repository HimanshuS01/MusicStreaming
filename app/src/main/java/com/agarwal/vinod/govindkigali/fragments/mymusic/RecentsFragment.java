package com.agarwal.vinod.govindkigali.fragments.mymusic;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
public class RecentsFragment extends Fragment {

    RecyclerView rvRecents;
    SongAdapter adapter;
    DatabaseReference recentsReference = FirebaseDatabase.getInstance().getReference("recents");
    private ArrayList<Song> recentList = new ArrayList<>();
    public static final String TAG = "FAV";
    public RecentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View recentsFragment =  inflater.inflate(R.layout.fragment_multi, container, false);

        rvRecents = recentsFragment.findViewById(R.id.rv_multi);

        recentsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recentList.clear();
                for (DataSnapshot favSnapshot : dataSnapshot.getChildren()) {
                    Song song = favSnapshot.getValue(Song.class);
                    recentList.add(song);
                }
                adapter.updateTracks(recentList);
                Log.d(TAG, "onDataChange: :)   :)   :)");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: :(   :(   :(");
            }
        });

        rvRecents.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SongAdapter(getContext(), new ArrayList<Song>(), (PlayerCommunication) getActivity());
        rvRecents.setAdapter(adapter);

        return recentsFragment;
    }



}
