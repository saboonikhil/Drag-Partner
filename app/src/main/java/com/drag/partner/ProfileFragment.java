package com.drag.partner;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.drag.partner.model.Partner;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    private Activity parentActivity;
    private View rootView;
    private TextView nameView, emailView, contactView, alternateContactView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentActivity = getActivity();
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentActivity.setTitle("My Profile");
        initViews();

        SharedPreferences pref = this.parentActivity.getSharedPreferences("AppPref", MODE_PRIVATE);
        String json = pref.getString("dbObj", "");
        Partner partner = new Gson().fromJson(json, Partner.class);

        nameView.setText(partner.getName());
        emailView.setText(partner.getEmail());
        contactView.setText(partner.getContact());
        alternateContactView.setText(partner.getAlternateContact());
    }

    private void initViews() {
        nameView = rootView.findViewById(R.id.profile_name);
        emailView = rootView.findViewById(R.id.profile_email);
        contactView = rootView.findViewById(R.id.profile_mobile_number);
        alternateContactView = rootView.findViewById(R.id.profile_alternate_number);
    }
}