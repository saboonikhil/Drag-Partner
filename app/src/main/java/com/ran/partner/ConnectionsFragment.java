package com.ran.partner;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ran.partner.adapter.ConnectionsAdapter;
import com.ran.partner.util.ViewAnimation;

public class ConnectionsFragment extends Fragment {

    private Activity parentActivity;
    private View rootView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private View addPartnerLayout, addPlaceLayout, newBackgroundView;
    private FloatingActionButton addPartnerView, addConnectionsView, addPlaceView;
    private boolean rotate = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentActivity = getActivity();
        rootView = inflater.inflate(R.layout.fragment_connections, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        parentActivity.setTitle("Connections");
        initViews();

        ConnectionsAdapter adapter = new ConnectionsAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        ViewAnimation.initShowOut(addPartnerLayout);
        ViewAnimation.initShowOut(addPlaceLayout);
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

        addPartnerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPartnerFragment dialog = new AddPartnerFragment();
                assert getFragmentManager() != null;
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, AddPartnerFragment.TAG);
            }
        });

        addPlaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Place clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleFabMode(View v) {
        rotate = ViewAnimation.rotateFab(v, !rotate);
        if (rotate) {
            ViewAnimation.showIn(addPartnerLayout);
            ViewAnimation.showIn(addPlaceLayout);
            newBackgroundView.setVisibility(View.VISIBLE);
        } else {
            ViewAnimation.showOut(addPartnerLayout);
            ViewAnimation.showOut(addPlaceLayout);
            newBackgroundView.setVisibility(View.GONE);
        }
    }

    private void initViews() {
        addConnectionsView = rootView.findViewById(R.id.connections_add);
        newBackgroundView = rootView.findViewById(R.id.connections_new_background);
        addPartnerLayout = rootView.findViewById(R.id.connections_add_partner_layout);
        addPartnerView = rootView.findViewById(R.id.connections_add_partner);
        addPlaceLayout = rootView.findViewById(R.id.connections_add_place_layout);
        addPlaceView = rootView.findViewById(R.id.connections_add_place);
        tabLayout = rootView.findViewById(R.id.connections_tab_layout);
        viewPager = rootView.findViewById(R.id.connections_view_pager);
    }
}