/*
package com.agarwal.vinod.govindkigali.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.adapters.UpcomingAdapter;
import com.agarwal.vinod.govindkigali.adapters.UpcomingAdapterN;
import com.agarwal.vinod.govindkigali.api.UpcomingService;
import com.agarwal.vinod.govindkigali.models.Upcoming;
import com.agarwal.vinod.govindkigali.models.upcomings.Year;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

*/
/**
 * A simple {@link Fragment} subclass.
 *//*

public class UpcomingFragmentN extends Fragment {


    public UpcomingFragmentN() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming_fragment_n, container, false);
        RecyclerView rvExt = view.findViewById(R.id.rvExt);
        UpcomingAdapterN adapter = new UpcomingAdapterN(getContext());
        rvExt.setAdapter(adapter);

        UpcomingService.getUpcomingApi().getUpcomingYears().enqueue(new Callback<ArrayList<Year>>() {
            @Override
            public void onResponse(Call<ArrayList<Year>> call, Response<ArrayList<Year>> response) {

            }

            @Override
            public void onFailure(Call<ArrayList<Year>> call, Throwable t) {

            }
        });


        UpcomingService.getUpcomingApi().getUpcomings().enqueue(new Callback<ArrayList<Upcoming>>() {
            @Override
            public void onResponse(Call<ArrayList<Upcoming>> call, Response<ArrayList<Upcoming>> response) {
                for (int i = 0; i < response.body().size(); i++) {
                    Log.d("Yooooooooo", "onResponse: " + response.body().get(i).getmVenue());
                }
                feededUpcomings = response.body();
                adapter.update(feededUpcomings);
                Log.d("UPCOMING", "onResponse: " + adapter.getNextEventPos());
                rvUpcoming.smoothScrollToPosition(adapter.getNextEventPos());
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ArrayList<Upcoming>> call, Throwable t) {
                Log.d("Yooo", "onFailure: ");
                t.printStackTrace();
            }
        });

        return view;
    }

}
*/
