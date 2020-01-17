package com.drag.partner;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.drag.partner.adapter.TripsAdapter;
import com.drag.partner.model.Cab;
import com.drag.partner.model.Partner;
import com.drag.partner.network.APIUtils;
import com.drag.partner.network.EndPointInterface;
import com.drag.partner.util.TripsScrollListener;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class TripsFragment extends Fragment {

    private static final String TAG = TripsFragment.class.getSimpleName();
    private Activity parentActivity;
    private View rootView;
    private Partner partner;
    private String token;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ImageView emptyView;
    private TripsAdapter tripsAdapter;
    private LinearLayoutManager layoutManager;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentActivity = getActivity();
        rootView = inflater.inflate(R.layout.fragment_trips, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentActivity.setTitle("My Trips");
        initViews();

        SharedPreferences pref = parentActivity.getSharedPreferences("AppPref", MODE_PRIVATE);
        token = pref.getString("token", "");
        String json = pref.getString("dbObj", "");
        partner = new Gson().fromJson(json, Partner.class);

        progressBar.setVisibility(View.VISIBLE);
        tripsAdapter = new TripsAdapter();
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setAdapter(tripsAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new TripsScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 6;

                if (isConnectedToInternet())
                    loadNextPage();
                else {
                    Snackbar.make(rootView, "No Internet Connection", Snackbar.LENGTH_LONG).show();
                    tripsAdapter.removeLoadingFooter();
                }
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isConnectedToInternet()) loadFirstPage();
        else Snackbar.make(rootView, "No Internet Connection", Snackbar.LENGTH_LONG).show();
    }

    private void loadFirstPage() {
        currentPage = 0;
        callPartnerTripsAPI().enqueue(new Callback<Cab[]>() {
            @Override
            public void onResponse(@NonNull Call<Cab[]> call, @NonNull Response<Cab[]> response) {
                if (response.body() != null) {
                    progressBar.setVisibility(View.GONE);
                    tripsAdapter.getTrips().clear();
                    tripsAdapter.notifyDataSetChanged();
                    tripsAdapter.addAll(response.body());

                    if (response.body().length == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                        isLastPage = true;
                    } else {
                        emptyView.setVisibility(View.GONE);
                        if (response.body().length % 6 == 0) {
                            isLastPage = false;
                            tripsAdapter.addLoadingFooter();
                        } else {
                            isLastPage = true;
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Cab[]> call, @NonNull Throwable t) {
                Log.e(TAG + " On Failure", t.getMessage());
                progressBar.setVisibility(View.GONE);
                if (tripsAdapter.getItemCount() == 0)
                    emptyView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void loadNextPage() {
        callPartnerTripsAPI().enqueue(new Callback<Cab[]>() {
            @Override
            public void onResponse(@NonNull Call<Cab[]> call, @NonNull Response<Cab[]> response) {
                if (response.body() != null) {
                    isLoading = false;
                    tripsAdapter.removeLoadingFooter();
                    tripsAdapter.addAll(response.body());
                    if (response.body().length > 0 && response.body().length % 6 == 0) {
                        tripsAdapter.addLoadingFooter();
                    } else {
                        isLastPage = true;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Cab[]> call, @NonNull Throwable t) {
                Log.e(TAG + " On Failure", t.getMessage());
                isLoading = false;
                tripsAdapter.removeLoadingFooter();
            }
        });
    }

    private Call<Cab[]> callPartnerTripsAPI() {
        EndPointInterface service = APIUtils.getAPIService(parentActivity);
        return service.partnerTrips(partner.get_id(), partner.getEmail(), token, currentPage);
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager connMgr = (ConnectivityManager) parentActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void initViews() {
        recyclerView = rootView.findViewById(R.id.trips_recycler_view);
        emptyView = rootView.findViewById(R.id.trips_empty_view);
        progressBar = rootView.findViewById(R.id.trips_progress_bar);
    }
}