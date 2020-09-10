package com.silentselene.enterpriseedition.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.silentselene.enterpriseedition.R;
import com.silentselene.enterpriseedition.data.DBAdapter;
import com.silentselene.enterpriseedition.data.WiFiRecord;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.silentselene.enterpriseedition.ui.home.GenerateCharts.getDangerList;

public class HomeFragment extends Fragment {
    private DBAdapter dbAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        dbAdapter = new DBAdapter(root.getContext());
        dbAdapter.open();
        drawAll(root);


        WiFiRecord[] wiFiRecords = dbAdapter.queryAllData();
        int flag = 0;
        StringBuilder macs = new StringBuilder("");
        for (WiFiRecord wiFiRecord : wiFiRecords) {
            if (flag != 0) {
                macs.append(",\"").append(wiFiRecord.getMAC()).append("\"");
            } else {
                flag = 1;
                macs.append("[\"").append(wiFiRecord.getMAC()).append("\"");
            }
        }
        macs.append("]");
        getDangerList(root.getContext(), macs.toString());


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