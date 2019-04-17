package com.ran.partner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ran.partner.adapter.CarsAdapter;
import com.ran.partner.model.Partner;
import com.ran.partner.util.HorizontalCalendar.HorizontalCalendar;
import com.ran.partner.util.HorizontalCalendar.util.HorizontalCalendarListener;

import java.util.Calendar;

public class CarsFragment extends Fragment {

    private static final String TAG = "CarsFragment";
    private Activity parentActivity;
    private View rootView;
    private Partner partner;
    private RecyclerView recyclerView;
    private CarsAdapter carsAdapter;
    private FloatingActionButton addCarView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentActivity = getActivity();
        rootView = inflater.inflate(R.layout.fragment_cars, container, false);
        return rootView;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentActivity.setTitle("My Cars");
        initViews();

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DAY_OF_MONTH, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, 30);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(rootView, R.id.cars_calendar_view)
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

    private void initViews() {
        recyclerView = rootView.findViewById(R.id.cars_recycler_view);
        addCarView = rootView.findViewById(R.id.cars_add_car);
    }
}