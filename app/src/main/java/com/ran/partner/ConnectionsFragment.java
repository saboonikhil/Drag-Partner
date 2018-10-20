package com.ran.partner;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ran.partner.util.ViewAnimation;

public class ConnectionsFragment extends Fragment {

    private Activity parentActivity;
    private View rootView;
    private View addDriverLayout, addSubDriverLayout, newBackgroundView;
    private FloatingActionButton addDriverView, addConnectionsView, addSubDriverView;
    private boolean rotate = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentActivity = getActivity();
        rootView = inflater.inflate(R.layout.fragment_connections, container, false);
        parentActivity.setTitle("Connections");
        initVariables();

        ViewAnimation.initShowOut(addDriverLayout);
        ViewAnimation.initShowOut(addSubDriverLayout);
        newBackgroundView.setVisibility(View.GONE);

        addConnectionsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(v);
            }
        });

        newBackgroundView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(addConnectionsView);
            }
        });

        addDriverView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Driver clicked", Toast.LENGTH_SHORT).show();
            }
        });

        addSubDriverView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Sub Driver clicked", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    private void toggleFabMode(View v) {
        rotate = ViewAnimation.rotateFab(v, !rotate);
        if (rotate) {
            ViewAnimation.showIn(addDriverLayout);
            ViewAnimation.showIn(addSubDriverLayout);
            newBackgroundView.setVisibility(View.VISIBLE);
        } else {
            ViewAnimation.showOut(addDriverLayout);
            ViewAnimation.showOut(addSubDriverLayout);
            newBackgroundView.setVisibility(View.GONE);
        }
    }

    private void initVariables() {
        addConnectionsView = rootView.findViewById(R.id.connections_add);
        newBackgroundView = rootView.findViewById(R.id.connections_new_background);
        addDriverLayout = rootView.findViewById(R.id.connections_add_driver_layout);
        addDriverView = rootView.findViewById(R.id.connections_add_driver);
        addSubDriverLayout = rootView.findViewById(R.id.connections_add_sub_driver_layout);
        addSubDriverView = rootView.findViewById(R.id.connections_add_sub_driver);
    }
}