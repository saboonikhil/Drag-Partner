package com.ran.partner.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ran.partner.R;

public class TripsCardAdapter extends RecyclerView.Adapter<TripsCardAdapter.MyViewHolder> {
    private TextView date_time;
    private TextView carName;
    private TextView carNumber;
    private TextView pickUp;
    private TextView drop;
    private ImageView cabPicture;
    private ImageView dropIcon;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_trips, parent, false);

        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(TripsCardAdapter.MyViewHolder holder, int position) {
        date_time.setText("Sun , 24 May, 10:45 PM");
        carName.setText("Toyota Etios");
        carNumber.setText("WB 01 EQ 6789");
        pickUp.setText("RP Hall, IIT Kharagpur");
        drop.setText("NSC Bose International Airport");
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
            cabPicture = itemView.findViewById(R.id.trips_car_image);
            date_time = itemView.findViewById(R.id.trips_date_time);
            carName = itemView.findViewById(R.id.trips_car_name);
            carNumber = itemView.findViewById(R.id.trips_car_number);
            ImageView pickUpIcon = itemView.findViewById(R.id.pickup_icon);
            pickUp = itemView.findViewById(R.id.trips_pickup_location);
            dropIcon = itemView.findViewById(R.id.drop_icon);
            drop = itemView.findViewById(R.id.trips_drop_location);
            Context mContext;
            mContext = itemView.getContext();
        }
    }
}

