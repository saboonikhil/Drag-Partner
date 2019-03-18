package com.ran.partner.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ran.partner.R;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.TripsCardViewHolder> {

    @NonNull
    @Override
    public TripsCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_trips, parent, false);
        return new TripsCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripsCardViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 8;
    }

    class TripsCardViewHolder extends RecyclerView.ViewHolder {
        private TextView idView, pickupView, dropView, startTimeView, fareView;

        TripsCardViewHolder(View itemView) {
            super(itemView);
            idView = itemView.findViewById(R.id.trips_id);
            pickupView = itemView.findViewById(R.id.trips_pickup);
            dropView = itemView.findViewById(R.id.trips_drop);
            startTimeView = itemView.findViewById(R.id.trips_start_time);
            fareView = itemView.findViewById(R.id.trips_fare);
        }
    }
}