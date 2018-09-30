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

public class NowFragment extends Fragment {

    private Activity parentActivity;
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

        actionNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actionNowButton.getText().toString().equals("Ready To Pickup")) {
                    actionNowButton.setText(R.string.start_trip);
                } else if (actionNowButton.getText().toString().equals("Start Trip")) {
                    actionNowButton.setText(R.string.end_trip);
                }
            }
        });
    }

    private void initVariables() {
        actionNowButton = parentActivity.findViewById(R.id.action_now_button);
    }
}