package com.ran.partner.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ran.partner.R;
import com.ran.partner.model.Cab;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CarsListAdapter extends RecyclerView.Adapter<CarsListAdapter.CarsListViewHolder> {

    private Cab[] cabs;

    public CarsListAdapter(Cab[] cabs) {
        this.cabs = cabs;
    }

    @NonNull
    @Override
    public CarsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_cars_list, parent, false);
        return new CarsListViewHolder(view);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(@NonNull CarsListViewHolder holder, int position) {
        holder.carNameView.setText(cabs[position].getCarName());
        holder.carNumberView.setText(cabs[position].getCarNumber());

        String pickup = cabs[position].getPickup();
        if (TextUtils.isEmpty(pickup))
            pickup = "All Pickup Locations";
        holder.pickupView.setText(pickup);

        String drop = cabs[position].getDrop();
        if (TextUtils.isEmpty(drop))
            drop = "All Drop Locations";
        holder.dropView.setText(drop);

        holder.seatsView.setText(cabs[position].getSeats());
        holder.fareView.setText(cabs[position].getFare());

        if (cabs[position].getStartTime() != null) {
            try {
                Calendar calendar = Calendar.getInstance();
                Date displayTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(cabs[position].getStartTime());
                calendar.setTime(displayTime);
                calendar.add(Calendar.HOUR, 5);
                calendar.add(Calendar.MINUTE, 30);
                String startTime = new SimpleDateFormat("hh:mm a").format(calendar.getTime());
                if (startTime.equals("12:00 PM"))
                    startTime = "Any";
                holder.startTimeView.setText(startTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return cabs.length;
    }

    class CarsListViewHolder extends RecyclerView.ViewHolder {
        private TextView carNameView, carNumberView, pickupView, dropView, seatsView, fareView, startTimeView;

        CarsListViewHolder(View itemView) {
            super(itemView);
            carNameView = itemView.findViewById(R.id.cars_car_name);
            carNumberView = itemView.findViewById(R.id.cars_car_number);
            pickupView = itemView.findViewById(R.id.cars_pickup);
            dropView = itemView.findViewById(R.id.cars_drop);
            startTimeView = itemView.findViewById(R.id.cars_time);
            fareView = itemView.findViewById(R.id.cars_fare);
            seatsView = itemView.findViewById(R.id.cars_seats);
        }
    }
}