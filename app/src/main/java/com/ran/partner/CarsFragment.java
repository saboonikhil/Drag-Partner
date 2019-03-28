package com.ran.partner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ran.partner.adapter.CarsAdapter;
import com.ran.partner.util.HorizontalCalendar.HorizontalCalendar;
import com.ran.partner.util.HorizontalCalendar.util.HorizontalCalendarListener;

import java.util.Calendar;

public class CarsFragment extends Fragment {

    private Activity parentActivity;
    private View rootView;
    private HorizontalCalendar horizontalCalendar;
    private ViewPager viewPager;
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
        initViews();

        Calendar startDate = Calendar.getInstance();
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

        CarsAdapter adapter = new CarsAdapter(getChildFragmentManager(), startDate);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                horizontalCalendar.centerCalendarToPosition(i + 2);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                CarsAdapter adapter = new CarsAdapter(getChildFragmentManager(), date);
                viewPager.setAdapter(adapter);
                viewPager.setCurrentItem(position - 2);
            }
        });

        addCarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(parentActivity, AddCarActivity.class));
            }
        });
    }

    private void initViews() {
        viewPager = rootView.findViewById(R.id.cars_view_pager);
        addCarView = rootView.findViewById(R.id.cars_add_car);
    }
}