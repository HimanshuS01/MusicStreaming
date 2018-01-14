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
import com.agarwal.vinod.govindkigali.adapters.PopListAdapter;
import com.agarwal.vinod.govindkigali.models.Song;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayListsFragment extends Fragment {

    RecyclerView rvPlayList;
    PopListAdapter adapter;
    DatabaseReference listReference = FirebaseDatabase.getInstance().getReference("pop");
    private ArrayList<String> List = new ArrayList<>();
    public static final String TAG = "FAV";
    public PlayListsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View listFragment =  inflater.inflate(R.layout.fragment_multi, container, false);

        rvPlayList = listFragment.findViewById(R.id.rv_multi);

        listReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List.clear();
                for (DataSnapshot popSnapshot : dataSnapshot.getChildren()) {
                    String name = popSnapshot.getKey();
                    List.add(name);
                    Log.d(TAG, "onDataChange: " + popSnapshot.getKey());
                }
                adapter.updateList(List);
                Log.d(TAG, "onDataChange: :)   :)   :)" + dataSnapshot.getChildren());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: :(   :(   :(");
            }
        });

        rvPlayList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PopListAdapter(getContext(), new Song(), false, getActivity());
        rvPlayList.setAdapter(adapter);

        return listFragment;
    }

}
