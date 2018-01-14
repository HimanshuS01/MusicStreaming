package com.agarwal.vinod.govindkigali.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Anirudh Gupta on 11/12/17.
 */

public class SongService {

    private SongService() {}

    private static API api = null;

    public static API getSongApi() {
        if (api == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.soundcloud.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            api = retrofit.create(API.class);
        }
        return api;
    }
}
