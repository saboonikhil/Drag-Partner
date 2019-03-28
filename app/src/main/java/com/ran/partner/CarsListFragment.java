package com.ran.partner;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ran.partner.adapter.CarsListAdapter;

public class CarsListFragment extends Fragment {

    private String date;
    private View rootView;
    private RecyclerView recyclerView;

    public static CarsListFragment newInstance(String date) {
        CarsListFragment carsListFragment = new CarsListFragment();
        Bundle args = new Bundle();
        args.putString("date", date);
        carsListFragment.setArguments(args);
        return carsListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            date = getArguments().getString("date");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cars_list, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();

        recyclerView.setAdapter(new CarsListAdapter(getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Toast.makeText(getContext(), date + " is selected!", Toast.LENGTH_SHORT).show();
    }

    private void initViews() {
        recyclerView = rootView.findViewById(R.id.users_recycler_view);
    }
}