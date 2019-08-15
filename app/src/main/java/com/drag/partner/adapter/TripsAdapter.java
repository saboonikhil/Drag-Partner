package com.drag.partner.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.drag.partner.R;
import com.drag.partner.TripDetailsActivity;
import com.drag.partner.model.Cab;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.TripsCardViewHolder> {

    private Cab[] trips;
    private Context mContext;

    public TripsAdapter(Cab[] trips) {
        this.trips = trips;
    }

    /*public void refreshData(Cab[] dataSet) {
        trips = dataSet;
        notifyDataSetChanged();
    }*/

    @NonNull
    @Override
    public TripsCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_trips, parent, false);
        return new TripsCardViewHolder(view);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(@NonNull TripsCardViewHolder holder, int position) {
        try {
            Calendar calendar = Calendar.getInstance();
            String startTime = trips[position].getStartTime();
            if (startTime != null) {
                Date displayTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(startTime);
                calendar.setTime(displayTime);
                calendar.add(Calendar.HOUR, 5);
                calendar.add(Calendar.MINUTE, 30);
                holder.startTimeView.setText(new SimpleDateFormat("EEE, MMM d, hh:mm a").format(calendar.getTime()));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.idView.setText(trips[position].getRiders()[0].getTripId());
        holder.pickupView.setText(trips[position].getRiders()[0].getPickup());
        holder.dropView.setText(trips[position].getRiders()[0].getDrop());

        float fare = Integer.parseInt(trips[position].getRiders()[0].getFare());
        float commission = Float.parseFloat(trips[position].getRiders()[0].getLuggageCount());
        String displayFare = "₹ " + String.format(java.util.Locale.US, "%.2f", (fare - (commission * 0.01 * fare)));
        holder.fareView.setText(displayFare);
    }

    @Override
    public int getItemCount() {
        return trips.length;
    }

    class TripsCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView idView, pickupView, dropView, startTimeView, fareView;

        TripsCardViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            idView = itemView.findViewById(R.id.trips_id);
            pickupView = itemView.findViewById(R.id.trips_pickup);
            dropView = itemView.findViewById(R.id.trips_drop);
            startTimeView = itemView.findViewById(R.id.trips_start_time);
            fareView = itemView.findViewById(R.id.trips_fare);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int itemPosition = getLayoutPosition();
            Intent intent = new Intent(mContext, TripDetailsActivity.class);
            intent.putExtra("position", itemPosition + "");
            intent.putExtra("trip_details", trips);
            mContext.startActivity(intent);
        }
    }
}