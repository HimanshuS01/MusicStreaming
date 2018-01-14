package com.agarwal.vinod.govindkigali.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agarwal.vinod.govindkigali.MainActivity;
import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.fragments.mymusic.FavFragment;
import com.agarwal.vinod.govindkigali.fragments.mymusic.PlayListsFragment;
import com.agarwal.vinod.govindkigali.fragments.mymusic.RecentsFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyMusicFragment extends Fragment {

    TextView tvRecents, tvFav, tvPlaylists;
    FragmentManager fragmentManager;
    public MyMusicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myMusicFragment = inflater.inflate(R.layout.fragment_mymusic, container, false);

        tvFav = myMusicFragment.findViewById(R.id.tv_fav);
        tvRecents = myMusicFragment.findViewById(R.id.tv_recently_played);
        tvPlaylists = myMusicFragment.findViewById(R.id.tv_play_list);
        fragmentManager = getActivity().getSupportFragmentManager();

        tvFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ++MainActivity.fragmentCheck;
                fragmentManager.beginTransaction()
                        .replace(R.id.fg, new FavFragment())
                        .addToBackStack("remove")
                        .commit();
            }
        });

        tvPlaylists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ++MainActivity.fragmentCheck;
                fragmentManager.beginTransaction()
                        .replace(R.id.fg, new PlayListsFragment())
                        .addToBackStack("remove")
                        .commit();
            }
        });

        tvRecents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ++MainActivity.fragmentCheck;
                fragmentManager.beginTransaction()
                        .replace(R.id.fg, new RecentsFragment())
                        .addToBackStack("remove")
                        .commit();
            }
        });

        return myMusicFragment;
    }

}
