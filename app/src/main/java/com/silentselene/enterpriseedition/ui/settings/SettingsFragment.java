package com.silentselene.enterpriseedition.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.card.MaterialCardView;
import com.silentselene.enterpriseedition.R;
import com.silentselene.enterpriseedition.login.LoginPage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SettingsFragment extends Fragment {

    LinearLayout info_layout;
    MaterialCardView btn_logout;
    MaterialCardView btn_login;

    TextView shop_name;
    TextView usr_tel;
    TextView shop_loc;

    String usr_info = "";

    private SettingsViewModel settingsViewModel;

    private final String FILE_NAME = "config.ini";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, final Bundle savedInstanceState) {
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        info_layout = root.findViewById(R.id.info_layout);
        btn_logout = root.findViewById(R.id.btn_logout);
        btn_login = root.findViewById(R.id.btn_login);

        shop_name = root.findViewById(R.id.shop_name);
        usr_tel = root.findViewById(R.id.usr_tel);
        shop_loc = root.findViewById(R.id.shop_loc);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info_layout.setVisibility(getView().GONE);
                btn_login.setVisibility(getView().VISIBLE);

                try {
                    savePreferenceFiles("");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent loginPage = new Intent(getActivity(), LoginPage.class);
                loginPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loginPage);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginPage = new Intent(getActivity(), LoginPage.class);
                loginPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loginPage);
            }
        });

        if (LoginPage.isLogin(root.getContext())) {
            info_layout.setVisibility(getView().VISIBLE);
            btn_login.setVisibility(getView().GONE);

            try {
                loadPreferencesFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.d("USR_INFO", usr_info);

            String[] info = usr_info.split("\n");
            Log.d("USR_INFO", info[0]);
            Log.d("USR_INFO", info[1]);
            Log.d("USR_INFO", info[2]);

            shop_name.setText(info[1]);
            usr_tel.setText(info[0]);
            shop_loc.setText(info[2]);
        }

        return root;
    }

    private void savePreferenceFiles(String info) throws IOException {
        FileOutputStream fos = getActivity().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
        fos.write(info.getBytes());
        fos.flush();
        fos.close();
    }

    private void loadPreferencesFile() throws IOException {
        try {
            FileInputStream fis = getActivity().openFileInput(FILE_NAME);
            if (fis.available() == 0) {
                return;
            }
            byte[] readBytes = new byte[fis.available()];
            while (fis.read(readBytes) != -1) {
            }
            usr_info = new String(readBytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}