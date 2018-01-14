package com.agarwal.vinod.govindkigali.playerUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.agarwal.vinod.govindkigali.MainActivity;
import com.agarwal.vinod.govindkigali.playerUtils.PlayerCommunication;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by darsh on 24/12/17.
 */
public class ImageLoader extends AsyncTask<String, Void, Bitmap> {

    private PlayerCommunication playerCommunication;

    public ImageLoader(PlayerCommunication playerCommunication) {
        this.playerCommunication = playerCommunication;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Log.d(MainActivity.TAG, "doInBackground: working...");
        InputStream in = null;
        try {
            Log.i("URL", params[0]);
            URL url = new URL(params[0]);
            URLConnection urlConn = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.connect();

            in = httpConn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap image = BitmapFactory.decodeStream(in);
        return image;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        Log.d(MainActivity.TAG, "onPostExecute: setting bitmap");
        playerCommunication.uploadImage(bitmap);
    }
}
