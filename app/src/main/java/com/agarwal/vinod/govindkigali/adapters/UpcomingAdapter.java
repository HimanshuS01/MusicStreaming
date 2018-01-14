package com.agarwal.vinod.govindkigali.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.agarwal.vinod.govindkigali.R;
import com.agarwal.vinod.govindkigali.models.Upcoming;
import com.agarwal.vinod.govindkigali.utils.Util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Anirudh Gupta on 12/13/2017.
 */

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.UpcomingViewHolder> {

    public static ArrayList<String> weekDaysLong;
    public static ArrayList<String> weekDaysShort;
    public static ArrayList<String> monthsLong;
    public static ArrayList<String> monthsShort;
    public static final int TYPE_BANNER = 1;
    public static final int TYPE_ENTRY = 2;


    Context context;
    Spinner spinner;
    ArrayList<Upcoming> upcomings;
    ArrayList<Integer> upcomingType;
    //ArrayList<Pair<Integer, String> > startIndices;
    public UpcomingAdapter(Context context, ArrayList<Upcoming> upcomings) {
        this.context = context;
        if (upcomings != null) {
            this.upcomings = new ArrayList<>(upcomings);
        }
        else {
            this.upcomings = new ArrayList<>();
        }
        updateUpcomingTypes();
        if(weekDaysLong == null)
        weekDaysLong = new ArrayList<>(Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"));
        if(weekDaysShort == null)
        weekDaysShort = new ArrayList<>(Arrays.asList("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"));
        if(monthsLong == null)
        monthsLong = new ArrayList<>(Arrays.asList("January" , "February" , "March" , "April", "May",
                "June", "July", "August", "September", "October",
                "November", "December"));
        if(monthsShort == null)
        monthsShort = new ArrayList<>(Arrays.asList("Jan" , "Feb" , "March" , "April", "May",
                "June", "July", "Aug", "Sept", "Oct",
                "Nov", "Dec"));
    }

    public UpcomingAdapter(Context context, ArrayList<Upcoming> upcomings, Spinner spinner) {
        this(context,upcomings);
        this.spinner = spinner;
    }

    /*public int viewPosToArrayListPos(int viewPos){
        int count = 0;
        for (int i = 0;i < startIndices.size(); ++i) {
            if(startIndices.get(i).first.compareTo(viewPos) < 0){
                count++;
            }
        }
        return viewPos - count;
    }*/

    public void update(@NonNull ArrayList<Upcoming> upcomings){
        this.upcomings = new ArrayList<>(upcomings);
        updateUpcomingTypes();
        notifyDataSetChanged();

    }

    private void updateUpcomingTypes(){
        upcomingType = new ArrayList<>();
        if(upcomings.size() > 0) {
            upcomingType.add(TYPE_BANNER);
            upcomings.add(0, new Upcoming(upcomings.get(0).getmMonth(), upcomings.get(0).getmYear()));
            notifyDataSetChanged();
        }
        for (int i = 1;i < upcomings.size(); ++i){

            if( (!upcomings.get(i-1).getmMonth().equals(upcomings.get(i).getmMonth())) ||
                    (upcomings.get(i-1).getmYear() != upcomings.get(i).getmYear())) {
                upcomings.add(i, new Upcoming(upcomings.get(i).getmMonth(), upcomings.get(i).getmYear()));
                upcomingType.add(TYPE_BANNER);
                notifyDataSetChanged();
            } else {
                upcomingType.add(TYPE_ENTRY);
            }
        }



    }
    /*private void updateStartIndices(){
        startIndices = new ArrayList<>();
        if (upcomings.size() == 0)return;
        startIndices.add(new Pair<Integer, String>(0, upcomings.get(0).getmMonth()));
        notifyDataSetChanged();
        for (int i = 0;i < upcomings.size() - 1 ; ++i){
            if(!upcomings.get(i).getmMonth().equals(upcomings.get(i+1).getmMonth())){
                startIndices.add(
                        new Pair<>(i + 1 + startIndices.size(), upcomings.get(i+1).getmMonth())
                );
                notifyDataSetChanged();
            }
        }
    }*/

    @Override
    public int getItemViewType(int position) {

        return upcomingType.get(position);
        /*for (int i = 0;i<startIndices.size();++i){
            if(startIndices.get(i).first.equals(position))
                return TYPE_BANNER;
        }
        //if(startIndices.contains(position))return TYPE_BANNER;
        return TYPE_ENTRY;
        //return super.getItemViewType(position);*/
    }

    @Override
    public UpcomingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(viewType == TYPE_ENTRY){
            return new UpcomingViewHolder(inflater.inflate(R.layout.layout_upcoming,parent,false), viewType);
        } else if(viewType == TYPE_BANNER){
            Log.d("hhhhh", "onCreateViewHolder: Doooooooo");
            return new UpcomingViewHolder(inflater.inflate(R.layout.layout_upcoming_month_heading_banner,parent,false), viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(UpcomingViewHolder holder, int position) {
            holder.bindView(position);
    }



    @Override
    public int getItemCount() {
        return upcomings.size();
//        return upcomings.size() + startIndices.size();
    }

    public ArrayList<Upcoming> getUpcomings(){
        return upcomings;
    }

    public int getNextEventPos(){
        java.util.Date date= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        while (month <= 11) {
            String monthName = (new DateFormatSymbols().getMonths())[month];
            for (int i = 0;i < upcomings.size();i++) {
                if(upcomingType.get(i)==TYPE_BANNER) {
                    if(upcomings.get(i).getmMonth().equals(monthName)) return i;
                }
            }
            /*for (int i = 0;i<startIndices.size();++i){
                Pair<Integer, String> curPair = startIndices.get(i);
                if(curPair.second.equals(monthName)) return curPair.first;
            }*/
            month++;
        }
        return 0;
        /*if(upcomings.size()>0){
            Date currentDate = new Date();
            Log.d("CURDATE", "getNextEventPos: " + currentDate);
            Date answer = new Date(0);
            Upcoming ans = upcomings.get(0);
            for (Upcoming upcoming : upcomings) {
                if((answer.compareTo(upcoming.getDateObject()) < 0)&&(upcoming.getDateObject().compareTo(currentDate) <= 0)){
                    Log.d("LOOP", "getNextEventPos: " + answer);
                    answer = upcoming.getDateObject();
                    ans = upcoming;
                }
            }
            return upcomings.indexOf(ans);
        }
        return 0;*/

    }

    /*public int getStartIndexOfMonth(String month){
        for (int i = 0; i<startIndices.size();++i){
            Pair<Integer,String> curPair = startIndices.get(i);
            if (curPair.second.equals(month))return curPair.first;
        }
        return -1;
    }*/


    public class UpcomingViewHolder extends RecyclerView.ViewHolder {
        TextView tvMonth,tvDayNumber,tvWeekDay,tvVenue,tvTime;
        ImageView btnOptions;
        int viewType;

        TextView tvBannerHeading;


        public UpcomingViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            if (viewType == TYPE_ENTRY) {
                tvMonth = itemView.findViewById(R.id.tv_month);
                tvDayNumber = itemView.findViewById(R.id.tv_day_number);
                tvWeekDay = itemView.findViewById(R.id.tv_week_day);
                tvVenue = itemView.findViewById(R.id.tv_venue);
                tvTime = itemView.findViewById(R.id.tv_time);
                btnOptions = itemView.findViewById(R.id.btn_options);
            } else if(viewType == TYPE_BANNER) {
                tvBannerHeading = itemView.findViewById(R.id.tvMonthHead);
            }
        }

        public void bindView(final int position){
            if (viewType == TYPE_ENTRY) {
                //int position = viewPosToArrayListPos(pos);
                final String venue = upcomings.get(position).getmVenue();
                final String time = upcomings.get(position).getmTime();
                final int day = upcomings.get(position).getmDate();
                final String month = upcomings.get(position).getmMonth();
                final int year = upcomings.get(position).getmYear();
                //tvDate.setText(new StringBuilder().append(month).append(" ").append(day).append("\n").append(year).toString());
                tvDayNumber.setText(String.valueOf(upcomings.get(position).getmDate()));

                Log.d("DAY", "bindView: " + position + upcomings.get(position).getmDay());
                tvWeekDay.setText(
                        weekDaysShort.get(weekDaysLong.indexOf(upcomings.get(position).getmDay()))
                );
                tvMonth.setText(
                        monthsShort.get(monthsLong.indexOf(month))
                );
                tvVenue.setText(venue);
                tvTime.setText(time);
                btnOptions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu popup = new PopupMenu(context, view);
                        MenuInflater inflater = popup.getMenuInflater();
                        inflater.inflate(R.menu.upcoming_options, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.navigation_invite:
                                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                        sharingIntent.setType("text/plain");
                                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Invitation");
                                        sharingIntent.putExtra(Intent.EXTRA_TEXT,
                                                context.getString(R.string.invite_intent_text)
                                                        + "\n\nVenue: "
                                                        + venue
                                                        + "\n\nTime: "
                                                        + time
                                                        + "\n\nDate: "
                                                        + day + " " + month + " " + year);
                                        context.startActivity(Intent.createChooser(sharingIntent, context.getResources().getString(R.string.share_using)));
                                        break;


                                    case R.id.navigation_add_to_calender:
                                        Intent intent = new Intent(Intent.ACTION_EDIT);
                                        intent.setType("vnd.android.cursor.item/event");
                                        intent.putExtra(CalendarContract.Events.TITLE, context.getString(R.string.add_to_calender_title));
                                        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                                                Util.timeinMillis(day, month, year, time));
                                        //intent.putExtra(CalendarContract.Events.ALL_DAY, false);// periodicity
                                        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, venue);

                                        intent.putExtra(CalendarContract.Events.DESCRIPTION, "Venue: " + venue + "\nTime: " + time);
                                        context.startActivity(intent);
                                        break;


                                    case R.id.navigation_directions:
                                        try {
                                            String url = "https://www.google.com/maps/search/?api=1&query=" + URLEncoder.encode(venue, "utf-8");
                                            Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                            context.startActivity(intent2);
                                        } catch (UnsupportedEncodingException e) {
                                            Toast.makeText(context, R.string.no_directions, Toast.LENGTH_SHORT).show();
                                            e.printStackTrace();
                                        }
                                        break;
                                }
                                return true;
                            }
                        });
                        popup.show();
                    }
                });


            } else if (viewType == TYPE_BANNER) {

                tvBannerHeading.setText(upcomings.get(position).getmMonth() + " " + upcomings.get(position).getmYear());
            }
        }

        /*public String getMonthTitleatPos(int pos){
            Pair<Integer, String> curPair = null;
            for (int i = 0;i<startIndices.size();++i){
                curPair = startIndices.get(i);
                if(curPair.first.equals(pos)){
                    break;
                }
            }
            return curPair.second;
        }*/

        int getViewType(){
            return viewType;
        }
    }
}
