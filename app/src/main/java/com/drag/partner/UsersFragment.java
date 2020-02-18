package com.drag.partner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drag.partner.adapter.UsersAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

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