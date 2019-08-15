package com.drag.partner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.drag.partner.adapter.CarsAdapter;
import com.drag.partner.model.Cab;
import com.drag.partner.model.Partner;
import com.drag.partner.network.APIUtils;
import com.drag.partner.network.EndPointInterface;
import com.drag.partner.util.HorizontalCalendar.HorizontalCalendar;
import com.drag.partner.util.HorizontalCalendar.util.HorizontalCalendarListener;
import com.drag.partner.util.OnSwipeTouchListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class CarsFragment extends Fragment {

    private String TAG = "CarsFragment";
    private Activity parentActivity;
    private View rootView;
    private SharedPreferences pref;
    private Partner partner;
    private String token;
    private Cab[] trips;
    private HorizontalCalendar horizontalCalendar;
    private RecyclerView recyclerView;
    private CarsAdapter carsAdapter;
    private ImageView emptyView;
    private FloatingActionButton addCarView;
    private int count = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentActivity = getActivity();
        rootView = inflater.inflate(R.layout.fragment_cars, container, false);
        return rootView;
    }

    @SuppressLint({"SimpleDateFormat", "ClickableViewAccessibility"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentActivity.setTitle("My Cars");
        initViews();

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DAY_OF_MONTH, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, 30);

        horizontalCalendar = new HorizontalCalendar.Builder(rootView, R.id.cars_calendar_view)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .configure()
                .formatTopText("MMM")
                .formatMiddleText("dd")
                .formatBottomText("EEE")
                .textSize(14f, 24f, 14f)
                .showTopText(true)
                .showBottomText(true)
                .textColor(Color.LTGRAY, Color.WHITE)
                .end()
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                count = position;
                refreshTripsData();
            }
        });

        pref = parentActivity.getSharedPreferences("AppPref", MODE_PRIVATE);
        token = pref.getString("token", "");
        String json = pref.getString("dbObj", "");
        partner = new Gson().fromJson(json, Partner.class);

        trips = generateTripsData(partner.getCabs(), startDate);
        generateArrayData(trips);
        if (trips.length == 0)
            emptyView.setVisibility(View.VISIBLE);

        recyclerView.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            @Override
            public void onSwipeLeft() {
                if (count != 32)
                    count++;
                horizontalCalendar.centerCalendarToPosition(count);
            }

            @Override
            public void onSwipeRight() {
                if (count != 2)
                    count--;
                horizontalCalendar.centerCalendarToPosition(count);
            }
        });

        addCarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCarFragment dialog = new AddCarFragment();
                if (getFragmentManager() != null) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft, AddCarFragment.TAG);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTripsData();
    }

    private void updateTripsData() {
        EndPointInterface service = APIUtils.getAPIService(parentActivity);
        /*service.partnerDetail(partner.get_id(), partner.getEmail(), token).enqueue(new Callback<Partner>() {
            @Override
            public void onResponse(@NonNull Call<Partner> call, @NonNull Response<Partner> response) {
                if (response.body() != null) {
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("dbObj", new Gson().toJson(response.body()));
                    edit.apply();
                    String json = pref.getString("dbObj", "");
                    partner = new Gson().fromJson(json, Partner.class);
                    refreshTripsData();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Partner> call, @NonNull Throwable t) {
                Log.e(TAG + " On Failure", t.getMessage());
                Toast.makeText(parentActivity, "Couldn't refresh cars", Toast.LENGTH_LONG).show();
            }
        });*/
    }

    private void refreshTripsData() {
        trips = generateTripsData(partner.getCabs(), horizontalCalendar.getSelectedDate());
        carsAdapter.refreshData(trips);
        if (trips.length == 0)
            emptyView.setVisibility(View.VISIBLE);
        else
            emptyView.setVisibility(View.GONE);
    }

    @SuppressLint("SimpleDateFormat")
    private Cab[] generateTripsData(Cab[] cabs, Calendar date) {
        ArrayList<Cab> myList = new ArrayList<>(Arrays.asList(cabs));
        /*String selectedDate = new SimpleDateFormat("EEE, MMM dd, yyyy").format(date.getTime());
        for (Cab cab : cabs) {
            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(cab.getStartTime()));
                calendar.add(Calendar.HOUR, 5);
                calendar.add(Calendar.MINUTE, 30);
                String startDate = new SimpleDateFormat("EEE, MMM dd, yyyy").format(calendar.getTime());
                if (cab.getTripId() != null || !startDate.equals(selectedDate))
                    myList.remove(cab);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }*/
        Cab[] trips = new Cab[myList.size()];
        return myList.toArray(trips);
    }

    private void generateArrayData(Cab[] cabs) {
        carsAdapter = new CarsAdapter(cabs);
        recyclerView.setAdapter(carsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initViews() {
        recyclerView = rootView.findViewById(R.id.cars_recycler_view);
        emptyView = rootView.findViewById(R.id.cars_empty_view);
        addCarView = rootView.findViewById(R.id.cars_add_car);
    }
}