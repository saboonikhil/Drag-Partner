package com.drag.partner;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drag.partner.adapter.RequestsAdapter;
import com.drag.partner.model.Partner;
import com.drag.partner.model.Request;
import com.drag.partner.network.APIUtils;
import com.drag.partner.network.EndPointInterface;
import com.drag.partner.util.TripsScrollListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class RequestsFragment extends Fragment {

    private static final String TAG = RequestsFragment.class.getSimpleName();
    private Activity parentActivity;
    private View rootView;
    private Partner partner;
    private String token;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ImageView emptyView;
    private RequestsAdapter requestsAdapter;
    private LinearLayoutManager layoutManager;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentActivity = getActivity();
        rootView = inflater.inflate(R.layout.fragment_requests, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentActivity.setTitle("My Requests");
        initViews();

        SharedPreferences pref = parentActivity.getSharedPreferences("AppPref", MODE_PRIVATE);
        token = pref.getString("token", "");
        String json = pref.getString("dbObj", "");
        partner = new Gson().fromJson(json, Partner.class);

        progressBar.setVisibility(View.VISIBLE);
        requestsAdapter = new RequestsAdapter();
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setAdapter(requestsAdapter);
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
                    requestsAdapter.removeLoadingFooter();
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
        callRequestListAPI().enqueue(new Callback<Request[]>() {
            @Override
            public void onResponse(@NonNull Call<Request[]> call, @NonNull Response<Request[]> response) {
                if (response.body() != null) {
                    progressBar.setVisibility(View.GONE);
                    requestsAdapter.getRequests().clear();
                    requestsAdapter.notifyDataSetChanged();
                    requestsAdapter.addAll(response.body());

                    if (response.body().length == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                        isLastPage = true;
                    } else {
                        emptyView.setVisibility(View.GONE);
                        if (response.body().length % 6 == 0) {
                            isLastPage = false;
                            requestsAdapter.addLoadingFooter();
                        } else {
                            isLastPage = true;
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Request[]> call, @NonNull Throwable t) {
                Log.e(TAG + " On Failure", t.getMessage());
                progressBar.setVisibility(View.GONE);
                if (requestsAdapter.getItemCount() == 0)
                    emptyView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void loadNextPage() {
        callRequestListAPI().enqueue(new Callback<Request[]>() {
            @Override
            public void onResponse(@NonNull Call<Request[]> call, @NonNull Response<Request[]> response) {
                if (response.body() != null) {
                    isLoading = false;
                    requestsAdapter.removeLoadingFooter();
                    requestsAdapter.addAll(response.body());
                    if (response.body().length > 0 && response.body().length % 6 == 0) {
                        requestsAdapter.addLoadingFooter();
                    } else {
                        isLastPage = true;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Request[]> call, @NonNull Throwable t) {
                Log.e(TAG + " On Failure", t.getMessage());
                isLoading = false;
                requestsAdapter.removeLoadingFooter();
            }
        });
    }

    private Call<Request[]> callRequestListAPI() {
        EndPointInterface service = APIUtils.getAPIService(parentActivity);
        return service.requestList(partner.getEmail(), token, currentPage);
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager connMgr = (ConnectivityManager) parentActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void initViews() {
        recyclerView = rootView.findViewById(R.id.requests_recycler_view);
        emptyView = rootView.findViewById(R.id.requests_empty_view);
        progressBar = rootView.findViewById(R.id.requests_progress_bar);
    }
}