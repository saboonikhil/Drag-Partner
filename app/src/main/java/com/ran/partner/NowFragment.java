package com.ran.partner;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NowFragment extends Fragment {

    private Activity parentActivity;
    private Calendar now;
    private LinearLayout timeLayout;
    private TextView startTimeView, startDateView, endTimeView, endDateView;
    private Button actionNowButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentActivity = getActivity();
        return inflater.inflate(R.layout.fragment_now, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentActivity.setTitle("RAN Partner");
        initVariables();

        timeLayout.setVisibility(View.GONE);
        actionNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (actionNowButton.getText().toString()) {
                    case "Ready To Pickup":
                        Toast.makeText(getContext(), "Message sent to the rider.", Toast.LENGTH_LONG).show();
                        actionNowButton.setText(R.string.start_trip);
                        break;
                    case "Start Trip":
                        timeLayout.setVisibility(View.VISIBLE);
                        now = Calendar.getInstance();
                        startTimeView.setText(new SimpleDateFormat("HH:mm", Locale.US).format(now.getTime()));
                        startDateView.setText(new SimpleDateFormat("MMM d", Locale.US).format(now.getTime()));
                        actionNowButton.setText(R.string.end_trip);
                        break;
                    case "End Trip":
                        now = Calendar.getInstance();
                        endTimeView.setText(new SimpleDateFormat("HH:mm", Locale.US).format(now.getTime()));
                        endDateView.setText(new SimpleDateFormat("MMM d", Locale.US).format(now.getTime()));
                        break;
                }
            }
        });
    }

    private void initVariables() {
        timeLayout = parentActivity.findViewById(R.id.now_time_layout);
        startTimeView = parentActivity.findViewById(R.id.now_start_time);
        startDateView = parentActivity.findViewById(R.id.now_start_date);
        endTimeView = parentActivity.findViewById(R.id.now_end_time);
        endDateView = parentActivity.findViewById(R.id.now_end_date);
        actionNowButton = parentActivity.findViewById(R.id.action_now_button);
    }
}