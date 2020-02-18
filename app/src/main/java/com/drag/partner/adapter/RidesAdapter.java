package com.drag.partner.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drag.partner.R;
import com.drag.partner.model.Cab;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RidesAdapter extends RecyclerView.Adapter<RidesAdapter.RidesCardViewHolder> {

    private Context context;
    private List<Cab> rideList;
    private ListItemClickListener mOnClickListener;
    private int itemCount = 0;

    public RidesAdapter(Context context, List<Cab> rideList, ListItemClickListener listener) {
        this.context = context;
        this.rideList = rideList;
        mOnClickListener = listener;
    }

    public void refreshData(List<Cab> dataSet) {
        rideList = dataSet;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RidesCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rides, parent, false);
        return new RidesCardViewHolder(view);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(@NonNull RidesCardViewHolder holder, int position) {
        try {
            Calendar calendar = Calendar.getInstance();
            String startTime = rideList.get(position).getStartTime();
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

       /* holder.idView.setText(rideList.get(position).getTripId());
        holder.pickupView.setText(rideList.get(position).getPickup());
        holder.dropView.setText(rideList.get(position).getDrop());

        if (rideList.get(position).getTripId().indexOf('@') == -1) {
            switch (rideList.get(position).getSeats()) {
                case "0":
                    holder.seatsView.setText("4");
                    break;
                case "1":
                    holder.seatsView.setText("3");
                    break;
                case "2":
                    holder.seatsView.setText("2");
                    break;
                case "3":
                    holder.seatsView.setText("1");
                    break;
            }
        } else {
            holder.seatsView.setText(rideList.get(position).getSeats());
        }*/
    }

    @Override
    public int getItemCount() {
        if (rideList != null)
            itemCount = rideList.size();
        else
            Toast.makeText(context, "Can't connect to Drag servers", Toast.LENGTH_LONG).show();
        return itemCount;
    }

    public interface ListItemClickListener {
        void onListItemClick(Cab selectedCab);
    }

    class RidesCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView idView, pickupView, dropView, startTimeView, seatsView;

        RidesCardViewHolder(View itemView) {
            super(itemView);
            idView = itemView.findViewById(R.id.rides_id);
            pickupView = itemView.findViewById(R.id.rides_pickup);
            dropView = itemView.findViewById(R.id.rides_drop);
            startTimeView = itemView.findViewById(R.id.rides_start_time);
            seatsView = itemView.findViewById(R.id.rides_seats);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(rideList.get(clickedPosition));
        }
    }
}