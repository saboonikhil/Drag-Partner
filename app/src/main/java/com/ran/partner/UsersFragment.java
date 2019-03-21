package com.ran.partner;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ran.partner.adapter.UsersAdapter;

public class UsersFragment extends Fragment implements UsersAdapter.ListItemClickListener {

    private View rootView;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_users, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();

        recyclerView.setAdapter(new UsersAdapter(getContext(), this));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onListItemClick() {
        BottomSheetDialogFragment userDetails = new UserDetailsFragment();
        userDetails.show(getChildFragmentManager(), "Bottom Sheet Dialog Fragment");
    }

    private void initViews() {
        recyclerView = rootView.findViewById(R.id.users_recycler_view);
    }
}