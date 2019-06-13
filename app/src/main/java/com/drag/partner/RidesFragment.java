package com.drag.partner;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.drag.partner.adapter.RidesAdapter;
import com.drag.partner.model.Cab;
import com.drag.partner.model.Partner;
import com.drag.partner.network.APIUtils;
import com.drag.partner.network.EndPointInterface;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class RidesFragment extends Fragment implements RidesAdapter.ListItemClickListener {

    private static final String TAG = RidesFragment.class.getSimpleName();
    private Activity parentActivity;
    private View rootView;
    private Partner partner;
    private String token;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ImageView emptyView;
    private RidesAdapter ridesAdapter;
    private int refreshCount = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentActivity = getActivity();
        rootView = inflater.inflate(R.layout.fragment_rides, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentActivity.setTitle("Ride Requests");
        initViews();

        SharedPreferences pref = parentActivity.getSharedPreferences("AppPref", MODE_PRIVATE);
        token = pref.getString("token", "");
        String json = pref.getString("dbObj", "");
        partner = new Gson().fromJson(json, Partner.class);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullAndRefresh();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        pullAndRefresh();
    }

    private void pullAndRefresh() {
        refreshCount++;
        refreshLayout.setRefreshing(true);
        if (isConnectedToInternet())
            getRideList();
        else
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
    }

    private void getRideList() {
        EndPointInterface service = APIUtils.getAPIService();
        Call<List<Cab>> call = service.partnerRideList(partner.getEmail(), token);

        call.enqueue(new Callback<List<Cab>>() {
            @Override
            public void onResponse(@NonNull Call<List<Cab>> call, @NonNull Response<List<Cab>> response) {
                if (refreshCount == 1) {
                    generateDataList(response.body());
                } else {
                    ridesAdapter.refreshData(response.body());
                    if (ridesAdapter.getItemCount() == 0)
                        emptyView.setVisibility(View.VISIBLE);
                    else
                        emptyView.setVisibility(View.INVISIBLE);
                }
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<List<Cab>> call, @NonNull Throwable t) {
                Log.e(TAG + " On Failure", t.getMessage());
                if (refreshCount == 1)
                    Toast.makeText(getContext(), "Something went wrong. Please try again later!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getContext(), "Couldn't refresh cabs", Toast.LENGTH_LONG).show();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void generateDataList(List<Cab> cabList) {
        ridesAdapter = new RidesAdapter(getContext(), cabList, this);
        recyclerView.setAdapter(ridesAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        if (layoutManager.getItemCount() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            emptyView.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onListItemClick(Cab selectedCab) {

    }

    private boolean isConnectedToInternet() {
        ConnectivityManager connMgr = (ConnectivityManager) parentActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnected();
    }

    private void initViews() {
        refreshLayout = rootView.findViewById(R.id.rides_refresh_layout);
        recyclerView = rootView.findViewById(R.id.rides_recycler_view);
        emptyView = rootView.findViewById(R.id.rides_empty_view);
    }
}