package com.agarwal.vinod.govindkigali.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Anirudh Gupta on 12/13/2017.
 */

public class Upcoming {

    static ArrayList<String> monthsLong = new ArrayList<>(Arrays.asList("January" , "February" , "March" , "April", "May",
            "June", "July", "August", "September", "October",
            "November", "December"));;
    static ArrayList<String> monthsShort = new ArrayList<>(Arrays.asList("Jan" , "Feb" , "March" , "April", "May",
            "June", "July", "Aug", "Sept", "Oct",
            "Nov", "Dec"));;

    @SerializedName("Date")
    @Expose
    int mDate;

    @SerializedName("Day")
    @Expose
    String mDay;

    @SerializedName("Month")
    @Expose
    String mMonth;

    @SerializedName("Time")
    @Expose
    String mTime;

    @SerializedName("Venue")
    @Expose
    String mVenue;

    @SerializedName("Year")
    @Expose
    int mYear;


    public Upcoming(String mMonth, int mYear) {
        this.mDate = 0;
        this.mDay = "";
        this.mMonth = mMonth;
        this.mTime = "";
        this.mVenue = "";
        this.mYear = mYear;
        /*monthsLong = new ArrayList<>(Arrays.asList("January" , "February" , "March" , "April", "May",
                "June", "July", "August", "September", "October",
                "November", "December"));
        monthsShort = new ArrayList<>(Arrays.asList("Jan" , "Feb" , "March" , "April", "May",
                "June", "July", "Aug", "Sept", "Oct",
                "Nov", "Dec"));*/
    }

    public Upcoming(int mDate, String mDay, String mMonth, String mTime, String mVenue, int mYear) {
        this.mDate = mDate;
        this.mDay = mDay;
        this.mMonth = mMonth;
        this.mTime = mTime;
        this.mVenue = mVenue;
        this.mYear = mYear;
        /*monthsLong = new ArrayList<>(Arrays.asList("January" , "February" , "March" , "April", "May",
                "June", "July", "August", "September", "October",
                "November", "December"));
        monthsShort = new ArrayList<>(Arrays.asList("Jan" , "Feb" , "March" , "April", "May",
                "June", "July", "Aug", "Sept", "Oct",
                "Nov", "Dec"));*/
    }

    public void setmDate(int mDate) {
        this.mDate = mDate;
    }

    public void setmDay(String mDay) {
        this.mDay = mDay;
    }

    public void setmMonth(String mMonth) {
        this.mMonth = mMonth;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public void setmVenue(String mVenue) {
        this.mVenue = mVenue;
    }

    public void setmYear(int mYear) {
        this.mYear = mYear;
    }

    public int getmDate() {
        return mDate;
    }

    public String getmDay() {
        return mDay;
    }

    public String getmMonth() {
        return mMonth;
    }

    public String getmTime() {
        return mTime;
    }

    public String getmVenue() {
        return mVenue;
    }

    public int getmYear() {
        return mYear;
    }

    public Date getDateObject(){
        return new Date(mYear - 1900,monthsLong.indexOf(mMonth),mDate);
    }
}
