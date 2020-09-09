package com.silentselene.enterpriseedition.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.silentselene.enterpriseedition.R;
import com.silentselene.enterpriseedition.data.DBAdapter;
import com.silentselene.enterpriseedition.data.WiFiRecord;

public class HomeFragment extends Fragment {
    private DBAdapter dbAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        dbAdapter = new DBAdapter(root.getContext());
        dbAdapter.open();
        drawAll(root);

        return root;
    }

    public void drawAll(final View root) {
        WiFiRecord[] wiFiRecords = dbAdapter.queryAllData();
        if (wiFiRecords == null) return;
        for (WiFiRecord wiFiRecord : wiFiRecords) {
            LinearLayout linearLayout = root.findViewById(R.id.selectCards);
            linearLayout.addView
                    (GenerateCard.GetCard(root.getContext(),
                            wiFiRecord.getALIAS(), wiFiRecord.getMAC()), 0);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
            layoutParams.setMargins(24, 8, 24, 8);
            linearLayout.getChildAt(0).setLayoutParams(layoutParams);
        }
    }
}