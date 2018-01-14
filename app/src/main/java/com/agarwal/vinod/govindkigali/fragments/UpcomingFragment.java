package com.agarwal.vinod.govindkigali.fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.agarwal.vinod.govindkigali.LinearLayoutManagerWithSmoothScroller;
import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.adapters.UpcomingAdapter;
import com.agarwal.vinod.govindkigali.adapters.UpcomingSpinnerAdapter;
import com.agarwal.vinod.govindkigali.api.UpcomingService;
import com.agarwal.vinod.govindkigali.models.Upcoming;
import com.agarwal.vinod.govindkigali.utils.PrefManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingFragment extends Fragment {

    boolean userSpinnerSelected = false;
    ActionBar actionBar;
    Toolbar toolbar;
    Spinner toolbarSpinner;
    RecyclerView rvUpcoming;
    LinearLayoutManagerWithSmoothScroller linearLayoutManager;
    boolean animateSpinner = true;
    public static ArrayList<Upcoming> feededUpcomings = null;

    public UpcomingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //setHasOptionsMenu(true);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        initiateToolbar();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);



        final ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);

        rvUpcoming = view.findViewById(R.id.rvUpcoming);
        final UpcomingAdapter adapter = new UpcomingAdapter(getContext(),null);
        linearLayoutManager = new LinearLayoutManagerWithSmoothScroller(getContext());
        rvUpcoming.setLayoutManager(linearLayoutManager);
        rvUpcoming.setAdapter(adapter);
        if(feededUpcomings == null){
            UpcomingService.getUpcomingApi().getUpcomings().enqueue(new Callback<ArrayList<Upcoming>>() {
                @Override
                public void onResponse(Call<ArrayList<Upcoming>> call, Response<ArrayList<Upcoming>> response) {
                    for (int i = 0; i < response.body().size(); i++) {
                        Log.d("Yooooooooo", "onResponse: " + response.body().get(i).getmVenue());
                    }
                    feededUpcomings = response.body();
                    adapter.update(feededUpcomings);
                    //rvUpcoming.smoothScrollToPosition(adapter.getNextEventPos());
                    rvUpcoming.post(new Runnable() {
                        @Override
                        public void run() {
                            rvUpcoming.smoothScrollToPosition(adapter.getNextEventPos());
                        }
                    });
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<ArrayList<Upcoming>> call, Throwable t) {
                    Log.d("Yooo", "onFailure: ");
                    t.printStackTrace();
                }
            });
        } else {
            adapter.update(feededUpcomings);
            rvUpcoming.post(new Runnable() {
                @Override
                public void run() {
                    rvUpcoming.smoothScrollToPosition(adapter.getNextEventPos());
                }
            });
            progressBar.setVisibility(View.GONE);
        }

        /*toolbarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(userSpinnerSelected) {
                    Toast.makeText(getContext(), "jkjkjkjkj", Toast.LENGTH_SHORT).show();
                    int idx = adapter.getStartIndexOfMonth(UpcomingSpinnerAdapter.months.get(position));
                    if (idx == -1) {
                        Toast.makeText(getContext(), "No Events for selected Month", Toast.LENGTH_SHORT).show();
                    } else {
                        rvUpcoming.smoothScrollToPosition(idx);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rvUpcoming.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    int idx = linearLayoutManager.findFirstVisibleItemPosition();
                    if ((toolbarSpinner != null) && (adapter.getItemViewType(idx) == UpcomingAdapter.TYPE_BANNER)) {

                        userSpinnerSelected = false;
                        toolbarSpinner.setSelection(

                                    UpcomingAdapter.monthsLong.indexOf(adapter.getUpcomings().get(idx).getmMonth()),
//                                    UpcomingAdapter.monthsLong.indexOf((adapter.onCreateViewHolder(null, UpcomingAdapter.TYPE_BANNER)).getMonthTitleatPos(idx)),
                                    true
                            );
                        userSpinnerSelected = true;
                        return;
                    }
                    /*if(toolbarSpinner.getSelectedItemPosition() ==
                            UpcomingSpinnerAdapter.months.indexOf(adapter.getUpcomings().get(idx).getmMonth()))
                    {
                        Log.d("TOOLBAR", "onScrollChange: ffffffffffffffff");
                        return;
                    }*/
                    if(toolbarSpinner != null){
//                        int newIdx = adapter.viewPosToArrayListPos(idx);
//                        userSpinnerSelected = false;
                        toolbarSpinner.setSelection(UpcomingAdapter.monthsLong.indexOf(adapter.getUpcomings().get(idx).getmMonth()),true);
//                        toolbarSpinner.setSelection(UpcomingSpinnerAdapter.months.indexOf(adapter.getUpcomings().get(newIdx).getmMonth()),true);
//                        userSpinnerSelected = true;
                        Log.d("TOOLBAR", "onScrollChange: gggggggggggggggggggg");
                    }
                    /*if(animateSpinner && toolbarSpinner != null) {
                        toolbarSpinner.setSelection(UpcomingSpinnerAdapter.months.indexOf(adapter.getUpcomings().get(idx).getmMonth()),true);
                        animateSpinner = false;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(3000);
                                    animateSpinner = true;
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }*/
                }
            });
        }
        /* else {
            rvUpcoming.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    int idx = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    if(toolbarSpinner != null)
                    toolbarSpinner.setSelection(UpcomingSpinnerAdapter.months.indexOf(adapter.getUpcomings().get(idx).getmMonth()));
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });
        }
        */
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    void initiateToolbar(){
        if(actionBar == null){
            actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        }
        if(toolbar == null){
            toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        }

        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        setupSpinner();
    }

    void setupSpinner(){
        if (toolbarSpinner == null){
            toolbarSpinner = toolbar.findViewById(R.id.spinner_toolbar);
        }
        if(toolbarSpinner !=null) {
            toolbarSpinner.setVisibility(View.VISIBLE);
            toolbarSpinner.setAdapter(new UpcomingSpinnerAdapter(getContext(), rvUpcoming));

            toolbarSpinner.setEnabled(false);
            toolbarSpinner.setClickable(false);
            if(new PrefManager(getContext()).isNightModeEnabled2())
                toolbarSpinner.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary_night_mode));
            else
                toolbarSpinner.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initiateToolbar();
    }

    @Override
    public void onStop() {
        super.onStop();
        actionBar.setDisplayShowTitleEnabled(true);
        if(toolbarSpinner != null)
        toolbarSpinner.setVisibility(View.GONE);
    }
}
