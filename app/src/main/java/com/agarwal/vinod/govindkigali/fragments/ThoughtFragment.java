package com.agarwal.vinod.govindkigali.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.adapters.ThoughtAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThoughtFragment extends Fragment {

    ViewPager viewPager;

    public ThoughtFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_thought, container, false);
       viewPager = view.findViewById(R.id.viewPagerThought);
       viewPager.setAdapter(new ThoughtAdapter(getContext()));
       return view;
    }

}
