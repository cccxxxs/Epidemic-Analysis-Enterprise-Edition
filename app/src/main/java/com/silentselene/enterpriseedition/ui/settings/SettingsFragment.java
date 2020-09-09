package com.silentselene.enterpriseedition.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.silentselene.enterpriseedition.R;
import com.silentselene.enterpriseedition.login.LoginPage;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
//        settingsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        (root.findViewById(R.id.btn_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int SUB_ACTIVITY_LOGIN = 1;
                Intent login_page = new Intent(getActivity(), LoginPage.class);
                startActivityForResult(login_page, SUB_ACTIVITY_LOGIN);
            }
        });
        return root;
    }
}