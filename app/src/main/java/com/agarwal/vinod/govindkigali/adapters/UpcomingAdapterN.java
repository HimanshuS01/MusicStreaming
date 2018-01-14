package com.agarwal.vinod.govindkigali.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agarwal.vinod.govindkigali.R;

/**
 * Created by anirudh on 31/12/17.
 */

public class UpcomingAdapterN extends RecyclerView.Adapter<UpcomingAdapterN.ExtHolder>{

    Context context;

    public UpcomingAdapterN(Context context) {
        this.context = context;
    }

    @Override
    public ExtHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_ext_upcoming,parent,false);
        ExtHolder viewHolder = new ExtHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ExtHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ExtHolder extends RecyclerView.ViewHolder{

        public ExtHolder(View itemView) {
            super(itemView);
        }
    }
}
