package com.agarwal.vinod.govindkigali.models.upcomings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by anirudh on 31/12/17.
 */

public class Year {
    @SerializedName("Year")
    @Expose
    int year;

    @SerializedName("Details")
    @Expose
    ArrayList<Month> monthList;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public ArrayList<Month> getMonthList() {
        return monthList;
    }

    public void setMonthList(ArrayList<Month> monthList) {
        this.monthList = monthList;
    }


}
