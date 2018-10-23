package com.ran.partner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ran.partner.util.ViewAnimation;

import static android.content.Context.MODE_PRIVATE;

public class ConnectionsFragment extends Fragment {

    private View rootView;
    private View addDriverLayout, addSubDriverLayout, newBackgroundView;
    private FloatingActionButton addDriverView, addConnectionsView, addSubDriverView;
    private boolean rotate = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Activity parentActivity = getActivity();
        rootView = inflater.inflate(R.layout.fragment_connections, container, false);
        assert parentActivity != null;
        parentActivity.setTitle("Connections");
        initVariables();

        SharedPreferences pref = parentActivity.getSharedPreferences("AppPref", MODE_PRIVATE);
        String role = pref.getString("role", "");
        customLayout(role);

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
                AddDriverFragment dialog = new AddDriverFragment();
                assert getFragmentManager() != null;
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, AddDriverFragment.TAG);
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

    @SuppressLint("RestrictedApi")
    private void customLayout(String role) {
        switch (role) {
            case "admin":
                break;
            case "org":
                addConnectionsView.setVisibility(View.GONE);
                break;
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