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
import com.drag.partner.model.Request;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RequestsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    private List<Request> requests;
    private Context mContext;

    public RequestsAdapter() {
        requests = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == ITEM) {
            View viewItem = inflater.inflate(R.layout.layout_requests, parent, false);
            return new RequestsCardViewHolder(viewItem);
        }
        View viewLoading = inflater.inflate(R.layout.layout_loading, parent, false);
        return new LoadingViewHolder(viewLoading);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        switch (getItemViewType(position)) {
            case ITEM:
                RequestsCardViewHolder holder = (RequestsCardViewHolder) viewHolder;
                try {
                    Calendar calendar = Calendar.getInstance();
                    Date displayTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(requests.get(position).getStartTime());
                    calendar.setTime(displayTime);
                    calendar.add(Calendar.HOUR, 5);
                    calendar.add(Calendar.MINUTE, 30);
                    holder.startTimeView.setText(new SimpleDateFormat("EEE, MMM d, hh:mm a").format(calendar.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                holder.idView.setText(requests.get(position).getId());
                holder.pickupView.setText(requests.get(position).getPickup());
                holder.dropView.setText(requests.get(position).getDrop());
                break;

            case LOADING:
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) viewHolder;
                loadingViewHolder.mProgressBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    public List<Request> getRequests() {
        return requests;
    }

    @Override
    public int getItemCount() {
        return requests == null ? 0 : requests.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == requests.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    private void add(Request r) {
        requests.add(r);
        notifyItemInserted(requests.size() - 1);
    }

    public void addAll(Request[] requests) {
        for (Request req : requests) {
            add(req);
        }
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Request());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = requests.size() - 1;
        Request result = requests.get(position);

        if (result != null) {
            requests.remove(position);
            notifyItemRemoved(position);
        }
    }

    protected class RequestsCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView idView, pickupView, dropView, startTimeView;

        RequestsCardViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            idView = itemView.findViewById(R.id.requests_id);
            pickupView = itemView.findViewById(R.id.requests_pickup);
            dropView = itemView.findViewById(R.id.requests_drop);
            startTimeView = itemView.findViewById(R.id.requests_start_time);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int itemPosition = getLayoutPosition();
            Intent intent = new Intent(mContext, TripDetailsActivity.class);
            intent.putExtra("position", itemPosition + "");
            intent.putExtra("trip_details", requests.toArray(new Request[0]));
            mContext.startActivity(intent);
        }
    }

    protected class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;

        LoadingViewHolder(View itemView) {
            super(itemView);
            mProgressBar = itemView.findViewById(R.id.loading_progress_bar);
        }
    }
}