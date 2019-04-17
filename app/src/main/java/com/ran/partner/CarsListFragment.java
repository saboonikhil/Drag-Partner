package com.ran.partner;

import android.app.Activity;
import android.content.SharedPreferences;
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

import com.google.gson.Gson;
import com.ran.partner.adapter.CarsListAdapter;
import com.ran.partner.model.Cab;
import com.ran.partner.model.Partner;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;

public class CarsListFragment extends Fragment {

    private String date;
    private Activity parentActivity;
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
        parentActivity = getActivity();
        rootView = inflater.inflate(R.layout.fragment_cars_list, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();

        SharedPreferences pref = parentActivity.getSharedPreferences("AppPref", MODE_PRIVATE);
        String json = pref.getString("dbObj", "");
        Partner partner = new Gson().fromJson(json, Partner.class);

        Cab[] trips = generateTripsData(partner.getCabs());
        generateArrayData(trips);

        Toast.makeText(getContext(), date + " is selected!", Toast.LENGTH_SHORT).show();
    }

    private Cab[] generateTripsData(Cab[] cabs) {
        ArrayList<Cab> myList = new ArrayList<>(Arrays.asList(cabs));
        for (Cab cab : cabs) {
            if (cab.isBooked()) {
                myList.remove(cab);
            }
        }
        Cab[] trips = new Cab[myList.size()];
        return myList.toArray(trips);
    }

    private void generateArrayData(Cab[] cabs) {
        CarsListAdapter carsListAdapter = new CarsListAdapter(cabs);
        recyclerView.setAdapter(carsListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initViews() {
        recyclerView = rootView.findViewById(R.id.users_recycler_view);
    }
}