package com.agarwal.vinod.govindkigali.playerUtils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.agarwal.vinod.govindkigali.MainActivity;
import com.agarwal.vinod.govindkigali.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by darsh on 24/12/17.
 */
public class DownloadMusic extends AsyncTask<String, Integer, Void> {

    private Context context;
    private NotificationCompat.Builder notificationBuilder;
    public DownloadMusic(Context context){
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... strings) {
        Log.d(MainActivity.TAG, "doInBackground: Downloading + playing music");
        try {
            RandomAccessFile file = new RandomAccessFile(strings[0], "rw");

            //TODO: MAKE Activity run even after user kills the app

            String CHANNEL_ID = "download govin ki gali";
            Integer id = 50891350;
            NotificationManager mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
            notificationBuilder.setContentText("Download in progress")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setAutoCancel(true);

            URL url = new URL(strings[1]);
            URLConnection conexion = url.openConnection();
            conexion.connect();
            long lenghtOfFile = conexion.getContentLength();
            Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
            InputStream input = new BufferedInputStream(url.openStream());
            byte data[] = new byte[1024];
            long total = 0, count = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                notificationBuilder.setProgress(100, (int)((total*100)/lenghtOfFile), false);
                Log.d(MainActivity.TAG, "doInBackground: " + (int)((total*100)/lenghtOfFile));

                // Displays the progress bar for the first time.
                mNotifyManager.notify(id, notificationBuilder.build());
                file.write(data);
                Log.d("ANDRO_ASYNC", "doInBackground: " + total);
            }
            file.close();
            mNotifyManager.cancel(id);
            Log.d(MainActivity.TAG, "doInBackground: Download complete");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
