package com.agarwal.vinod.govindkigali.api;

import com.agarwal.vinod.govindkigali.models.Song;
import com.agarwal.vinod.govindkigali.models.Upcoming;
import com.agarwal.vinod.govindkigali.models.upcomings.Year;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;



public interface API {

//    @GET("users/82858766/tracks?client_id=iq13rThQx5jx9KWaOY8oGgg1PUm9vp3J")
//    Call<ArrayList<Song>> getTracks();

    @GET("users/17410596/tracks?client_id=iq13rThQx5jx9KWaOY8oGgg1PUm9vp3J")
    Call<ArrayList<Song>> getTracks();

    @GET("bins/7wsaj")
    Call<ArrayList<Upcoming>> getUpcomings();

    @GET("bins/uz8qr")
    Call<ArrayList<Upcoming>> getUpcomingsOldOrig();

    @GET("bins/11avcb")
    Call<ArrayList<Year>> getUpcomingYearsModulated();
}
