package com.ran.partner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ran.partner.adapter.CarsAdapter;
import com.ran.partner.util.HorizontalCalendar.HorizontalCalendar;
import com.ran.partner.util.HorizontalCalendar.util.HorizontalCalendarListener;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class CarsFragment extends Fragment {

    private Activity parentActivity;
    private View rootView;
    private FloatingActionButton addCarView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentActivity = getActivity();
        rootView = inflater.inflate(R.layout.fragment_cars, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentActivity.setTitle("My Cars");
        initVariables();

        SharedPreferences pref = parentActivity.getSharedPreferences("AppPref", MODE_PRIVATE);
        String role = pref.getString("role", "");
        customLayout(role);

        Calendar startDate = Calendar.getInstance();
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
                Toast.makeText(getContext(), DateFormat.format("EEE, MMM d, yyyy", date) + " is selected!", Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.cars_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new CarsAdapter());

        addCarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(parentActivity, AddCarActivity.class));
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void customLayout(String role) {
        switch (role) {
            case "admin":
                break;
            case "org":
                addCarView.setVisibility(View.GONE);
                break;
        }
    }

    private void initVariables() {
        addCarView = parentActivity.findViewById(R.id.cars_add_car);
    }
}