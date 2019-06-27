package com.drag.partner;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.drag.partner.adapter.TripsAdapter;
import com.drag.partner.model.Cab;
import com.drag.partner.model.Partner;
import com.drag.partner.network.APIUtils;
import com.drag.partner.network.EndPointInterface;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class TripsFragment extends android.support.v4.app.Fragment {

    private static final String TAG = TripsFragment.class.getSimpleName();
    private Activity parentActivity;
    private View rootView;
    private SharedPreferences pref;
    private Partner partner;
    private String token;
    private Cab[] trips;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ImageView emptyView;
    private TripsAdapter tripsAdapter;

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

        pref = parentActivity.getSharedPreferences("AppPref", MODE_PRIVATE);
        token = pref.getString("token", "");
        String json = pref.getString("dbObj", "");
        partner = new Gson().fromJson(json, Partner.class);

        trips = generateTripsData(partner.getCabs());
        generateArrayData(trips);
        if (trips.length == 0)
            progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTripsData();
    }

    private void updateTripsData() {
        EndPointInterface service = APIUtils.getAPIService(parentActivity);
        service.partnerDetail(partner.get_id(), partner.getEmail(), token).enqueue(new Callback<Partner>() {
            @Override
            public void onResponse(@NonNull Call<Partner> call, @NonNull Response<Partner> response) {
                if (response.body() != null) {
                    progressBar.setVisibility(View.GONE);
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("dbObj", new Gson().toJson(response.body()));
                    edit.apply();
                    trips = generateTripsData(response.body().getCabs());
                    tripsAdapter.refreshData(trips);
                    if (trips.length == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Partner> call, @NonNull Throwable t) {
                Log.e(TAG + " On Failure", t.getMessage());
                progressBar.setVisibility(View.GONE);
                if (tripsAdapter.getItemCount() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                    Snackbar.make(rootView, "Something went wrong. Please try again later!", Snackbar.LENGTH_LONG).show();
                } else
                    Toast.makeText(parentActivity, "Couldn't refresh trips", Toast.LENGTH_LONG).show();
            }
        });
    }

    private Cab[] generateTripsData(Cab[] cabs) {
        ArrayList<Cab> myList = new ArrayList<>(Arrays.asList(cabs));
        for (Cab cab : cabs) {
            if (cab.getTripId() == null) {
                myList.remove(cab);
            }
        }
        Cab[] trips = new Cab[myList.size()];
        return myList.toArray(trips);
    }

    private void generateArrayData(Cab[] cabs) {
        tripsAdapter = new TripsAdapter(cabs);
        recyclerView.setAdapter(tripsAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
    }

    private void initViews() {
        recyclerView = rootView.findViewById(R.id.trips_recycler_view);
        emptyView = rootView.findViewById(R.id.trips_empty_view);
        progressBar = rootView.findViewById(R.id.trips_progress_bar);
    }
}