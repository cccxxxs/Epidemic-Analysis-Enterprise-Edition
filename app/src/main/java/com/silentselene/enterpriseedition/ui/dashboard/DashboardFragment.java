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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.silentselene.enterpriseedition.R;
import com.silentselene.enterpriseedition.data.DBAdapter;
import com.silentselene.enterpriseedition.data.WiFiRecord;

import java.util.regex.Pattern;

public class DashboardFragment extends Fragment {

    DBAdapter dbAdapter;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        dbAdapter = new DBAdapter(root.getContext());
        dbAdapter.open();
        drawAll(root);

        Button button = root.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText title = root.findViewById(R.id.alias);
                EditText mac = root.findViewById(R.id.macAddress);
                insertOne(root, mac.getText().toString(), title.getText().toString());
            }
        });

        return root;
    }

    public void drawAll(final View root) {
        WiFiRecord[] wiFiRecords = dbAdapter.queryAllData();
        if (wiFiRecords == null) return;
        for (WiFiRecord wiFiRecord : wiFiRecords) {
            LinearLayout linearLayout = root.findViewById(R.id.cardList);
            linearLayout.addView
                    (GenerateCard.GetCard(root.getContext(),
                            wiFiRecord.getALIAS(), wiFiRecord.getMAC()), 0);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
            layoutParams.setMargins(24, 8, 24, 8);
            linearLayout.getChildAt(0).setLayoutParams(layoutParams);
        }
    }

    public void insertOne(final View root, String mac, String alias) {
        if (!Pattern.matches("^([A-Fa-f0-9]{2}[-,:]){5}[A-Fa-f0-9]{2}$", mac)) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), "Mac address is illegal", Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }
        dbAdapter.insert(new WiFiRecord(alias, mac));

        LinearLayout linearLayout = root.findViewById(R.id.cardList);
        linearLayout.addView
                (GenerateCard.GetCard(root.getContext(), alias, mac), 0);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.setMargins(24, 8, 24, 8);
        linearLayout.getChildAt(0).setLayoutParams(layoutParams);
    }
}