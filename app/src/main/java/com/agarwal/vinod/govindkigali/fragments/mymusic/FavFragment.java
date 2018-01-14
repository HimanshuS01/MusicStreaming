package com.agarwal.vinod.govindkigali.fragments.mymusic;


import android.os.Bundle;
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
public class FavFragment extends Fragment {

    RecyclerView rvFav;
    SongAdapter adapter;
    DatabaseReference favReference = FirebaseDatabase.getInstance().getReference("fav");
    private ArrayList<Song> favList = new ArrayList<>();
    public static final String TAG = "FAV";
    public FavFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View recentsFragment =  inflater.inflate(R.layout.fragment_multi, container, false);

        rvFav = recentsFragment.findViewById(R.id.rv_multi);

        favReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                favList.clear();
                for (DataSnapshot favSnapshot : dataSnapshot.getChildren()) {
                    Song song = favSnapshot.getValue(Song.class);
                    favList.add(song);
                }
                adapter.updateTracks(favList);
                Log.d(TAG, "onDataChange: :)   :)   :)");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: :(   :(   :(");
            }
        });

        rvFav.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SongAdapter(getContext(), new ArrayList<Song>(), (PlayerCommunication) getActivity());
        rvFav.setAdapter(adapter);

        return recentsFragment;
    }

}
