package com.ran.partner;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.ran.partner.adapter.TripsAdapter;

public class TripsFragment extends android.support.v4.app.Fragment {

    private Activity parentActivity;
    private View rootView;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ImageView emptyView;
    private RelativeLayout rootLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentActivity = getActivity();
        rootView = inflater.inflate(R.layout.fragment_trips, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentActivity.setTitle("My Trips");
        initViews();

        RecyclerView recyclerView = view.findViewById(R.id.trips_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new TripsAdapter());
    }

    private void initViews() {
        rootLayout = rootView.findViewById(R.id.fragment_trips_layout);
        recyclerView = rootView.findViewById(R.id.trips_recycler_view);
        emptyView = rootView.findViewById(R.id.trips_empty_view);
        progressBar = rootView.findViewById(R.id.trips_progress_bar);
    }
}