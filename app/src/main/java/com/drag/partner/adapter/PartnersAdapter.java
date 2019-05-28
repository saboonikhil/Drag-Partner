package com.drag.partner.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drag.partner.R;

public class PartnersAdapter extends RecyclerView.Adapter<PartnersAdapter.PartnersViewHolder> {

    private Context context;
    private ListItemClickListener mOnClickListener;

    public PartnersAdapter(Context context, ListItemClickListener listener) {
        this.context = context;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public PartnersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_partners, parent, false);
        return new PartnersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PartnersViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 16;
    }

    public interface ListItemClickListener {
        void onListItemClick();
    }

    class PartnersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        PartnersViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnClickListener.onListItemClick();
        }
    }
}