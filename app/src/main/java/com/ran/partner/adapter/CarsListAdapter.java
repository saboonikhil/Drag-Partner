package com.ran.partner.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ran.partner.R;

public class CarsListAdapter extends RecyclerView.Adapter<CarsListAdapter.CarsListViewHolder> {

    private Context context;

    public CarsListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CarsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_cars_list, parent, false);
        return new CarsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarsListViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 16;
    }

    class CarsListViewHolder extends RecyclerView.ViewHolder {

        CarsListViewHolder(View itemView) {
            super(itemView);
        }
    }
}