package com.ran.partner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ran.partner.util.ScrollDatePicker.ScrollDatePicker;
import com.ran.partner.util.ScrollDatePicker.listener.DateListener;

import org.joda.time.DateTime;

public class CarsFragment extends Fragment implements DateListener {

    private Activity parentActivity;
    private ScrollDatePicker picker;
    private FloatingActionButton addCarView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentActivity = getActivity();
        return inflater.inflate(R.layout.fragment_cars, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentActivity.setTitle("My Cars");
        initVariables();

        picker.setListener(this)
                .setDays(30)
                .setOffset(7)
                .setDateSelectedColor(Color.DKGRAY)
                .setDateSelectedTextColor(Color.WHITE)
                .setMonthAndYearTextColor(Color.DKGRAY)
                .setTodayButtonTextColor(getResources().getColor(R.color.colorPrimary))
                .setTodayDateTextColor(getResources().getColor(R.color.colorPrimary))
                .setTodayDateBackgroundColor(Color.GRAY)
                .setUnselectedDayTextColor(Color.DKGRAY)
                .setDayOfWeekTextColor(Color.DKGRAY)
                .setUnselectedDayTextColor(getResources().getColor(R.color.primaryTextColor))
                .showTodayButton(false)
                .init();
        picker.setBackgroundColor(Color.LTGRAY);
        picker.setDate(new DateTime());

        addCarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parentActivity, AddCarActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDateSelected(DateTime dateSelected) {
        Log.i("ScrollDatePicker", "Show cars for " + dateSelected.toString());
    }

    private void initVariables() {
        addCarView = parentActivity.findViewById(R.id.add_car_button);
        picker = parentActivity.findViewById(R.id.cars_scroll_date_picker);
    }
}