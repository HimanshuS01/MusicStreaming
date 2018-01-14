package com.agarwal.vinod.govindkigali.models.upcomings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by anirudh on 31/12/17.
 */

public class Month {
    @SerializedName("Month")
    @Expose
    int monthId;
    @SerializedName("events")
    @Expose
    ArrayList<Event> events;

    public int getMonthId() {
        return monthId;
    }

    public void setMonthId(int monthId) {
        this.monthId = monthId;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
