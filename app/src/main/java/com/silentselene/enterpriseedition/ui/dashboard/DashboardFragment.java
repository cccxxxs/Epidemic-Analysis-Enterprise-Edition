package com.silentselene.enterpriseedition.ui.dashboard;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.silentselene.enterpriseedition.MainActivity;
import com.silentselene.enterpriseedition.R;

import java.util.Objects;
import java.util.regex.Pattern;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        Button button = root.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout linearLayout = root.findViewById(R.id.cardList);
                EditText title = root.findViewById(R.id.alias);
                EditText mac = root.findViewById(R.id.macAddress);
                if (!Pattern.matches("^([A-Fa-f0-9]{2}[-,:]){5}[A-Fa-f0-9]{2}$", mac.getText())) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Mac address is illegal", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                linearLayout.addView
                        (GenerateCard.GetCard(container.getContext(),
                                title.getText().toString(), mac.getText().toString()), 0);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
                layoutParams.setMargins(24, 8, 24, 8);
                linearLayout.getChildAt(0).setLayoutParams(layoutParams);
            }
        });

        return root;
    }
}