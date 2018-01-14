package com.agarwal.vinod.govindkigali.playerUtils;


import android.graphics.Bitmap;

import com.agarwal.vinod.govindkigali.models.Song;

import java.util.ArrayList;


/**
 * Created by Anirudh Gupta on 12/11/2017.
 */

public interface PlayerCommunication {
    void uploadImage(Bitmap bitmap);
    void playSong(ArrayList<Song> playlist, Integer value);
}
