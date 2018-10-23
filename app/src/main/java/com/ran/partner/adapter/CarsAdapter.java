package com.ran.partner.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ran.partner.R;

public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.CarsViewHolder> {
    private TextView carNameView, carNumberView, pickupView, dropView, seatsView, fareView, timeView;

    @NonNull
    @Override
    public CarsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_cars, parent, false);
        return new CarsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarsAdapter.CarsViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    class CarsViewHolder extends RecyclerView.ViewHolder {
        CarsViewHolder(View itemView) {
            super(itemView);
            carNameView = itemView.findViewById(R.id.cars_car_name);
            carNumberView = itemView.findViewById(R.id.cars_car_number);
            pickupView = itemView.findViewById(R.id.cars_pickup);
            dropView = itemView.findViewById(R.id.cars_drop);
            seatsView = itemView.findViewById(R.id.cars_seats);
            fareView = itemView.findViewById(R.id.cars_fare);
            timeView = itemView.findViewById(R.id.cars_time);
        }
    }
}

