package com.agarwal.vinod.govindkigali.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Anirudh Gupta on 12/11/2017.
 */

public class Util {
    @NonNull
    public static Resources getLocalizedResources(Context context, Locale desiredLocale) {
        Configuration conf = context.getResources().getConfiguration();
        conf = new Configuration(conf);
        conf.setLocale(desiredLocale);
        Context localizedContext = context.createConfigurationContext(conf);
        return localizedContext.getResources();
    }

    public static long timeinMillis(int day, String month, int year, String time){
        long timeInMilliseconds = 0;
        String[] timeArr = time.split("P");
        if (timeArr.length == 1){
            timeArr = time.split("A");
            timeArr[1] = "AM";
        } else {
            timeArr[1] = "PM";
        }
        String formattedTime = day + "-" + month + "-" + year + "-" + timeArr[0] + "-" + timeArr[1];
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMMM-yyyy-h:mm-a");
        try {
            Date mDate = sdf.parse(formattedTime);
            timeInMilliseconds = mDate.getTime();
        } catch (ParseException e) {
            Log.d("Util", "timeinMillis: Invalid Date");
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }
    public static int convertPixelsToDp2(float sizeInDp, Context context) {
        float scale = context.getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (sizeInDp * scale + 0.5f);
        return dpAsPixels;
    }
}
