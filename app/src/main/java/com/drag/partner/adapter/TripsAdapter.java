package com.drag.partner.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.drag.partner.R;
import com.drag.partner.TripDetailsActivity;
import com.drag.partner.model.Cab;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TripsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    private List<Cab> trips;
    private Context mContext;

    public TripsAdapter() {
        trips = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == ITEM) {
            View viewItem = inflater.inflate(R.layout.layout_trips, parent, false);
            return new TripsCardViewHolder(viewItem);
        }
        View viewLoading = inflater.inflate(R.layout.layout_loading, parent, false);
        return new LoadingViewHolder(viewLoading);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        switch (getItemViewType(position)) {
            case ITEM:
                TripsCardViewHolder holder = (TripsCardViewHolder) viewHolder;
                try {
                    Calendar calendar = Calendar.getInstance();
                    Date displayTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(trips.get(position).getStartTime());
                    calendar.setTime(displayTime);
                    calendar.add(Calendar.HOUR, 5);
                    calendar.add(Calendar.MINUTE, 30);
                    holder.startTimeView.setText(new SimpleDateFormat("EEE, MMM d, hh:mm a").format(calendar.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                holder.idView.setText(trips.get(position).getRiders()[0].getTripId());
                holder.pickupView.setText(trips.get(position).getRiders()[0].getPickup());
                holder.dropView.setText(trips.get(position).getRiders()[0].getDrop());

                float fare = Float.parseFloat(trips.get(position).getFare());
                float commission = Float.parseFloat(trips.get(position).getRiders()[0].getLuggageCount());
                String displayAmount = "₹ " + String.format(java.util.Locale.US, "%.2f", (fare - (commission * 0.01 * fare)));
                holder.amountView.setText(displayAmount);
                break;

            case LOADING:
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) viewHolder;
                loadingViewHolder.mProgressBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    public List<Cab> getTrips() {
        return trips;
    }

    @Override
    public int getItemCount() {
        return trips == null ? 0 : trips.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == trips.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    private void add(Cab c) {
        trips.add(c);
        notifyItemInserted(trips.size() - 1);
    }

    public void addAll(Cab[] trips) {
        for (Cab cab : trips) {
            add(cab);
        }
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Cab());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = trips.size() - 1;
        Cab result = trips.get(position);

        if (result != null) {
            trips.remove(position);
            notifyItemRemoved(position);
        }
    }

    protected class TripsCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView idView, pickupView, dropView, startTimeView, amountView;

        TripsCardViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            idView = itemView.findViewById(R.id.trips_id);
            pickupView = itemView.findViewById(R.id.trips_pickup);
            dropView = itemView.findViewById(R.id.trips_drop);
            startTimeView = itemView.findViewById(R.id.trips_start_time);
            amountView = itemView.findViewById(R.id.trips_amount);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int itemPosition = getLayoutPosition();
            Intent intent = new Intent(mContext, TripDetailsActivity.class);
            intent.putExtra("position", itemPosition + "");
            intent.putExtra("trip_details", trips.toArray(new Cab[0]));
            mContext.startActivity(intent);
        }
    }

    protected class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;

        LoadingViewHolder(View itemView) {
            super(itemView);
            mProgressBar = itemView.findViewById(R.id.trips_loading_progress_bar);
        }
    }
}