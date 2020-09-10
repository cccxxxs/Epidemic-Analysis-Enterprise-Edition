package com.silentselene.enterpriseedition.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.card.MaterialCardView;
import com.silentselene.enterpriseedition.R;
import com.silentselene.enterpriseedition.login.LoginPage;

import java.io.FileOutputStream;
import java.io.IOException;

public class SettingsFragment extends Fragment {

    LinearLayout info_layout;
    MaterialCardView btn_logout;


    private SettingsViewModel settingsViewModel;

    private final String FILE_NAME = "config.ini";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        info_layout = root.findViewById(R.id.info_layout);
        btn_logout = root.findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_logout.setVisibility(getView().GONE);

                try {
                    savePreferenceFiles("");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent login_page = new Intent(getActivity(), LoginPage.class);
                startActivity(login_page);
            }
        });

        if(LoginPage.isLogin(root.getContext())){
            info_layout.setVisibility(getView().VISIBLE);
        }

        return root;
    }

    private void savePreferenceFiles(String info) throws IOException {
        FileOutputStream fos = getActivity().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
        fos.write(info.getBytes());
        fos.flush();
        fos.close();
    }
}