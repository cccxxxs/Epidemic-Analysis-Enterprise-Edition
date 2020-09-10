package com.silentselene.enterpriseedition.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.silentselene.enterpriseedition.R;
import com.silentselene.enterpriseedition.data.DBAdapter;
import com.silentselene.enterpriseedition.data.WiFiRecord;
import com.silentselene.enterpriseedition.login.LoginPage;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tech.linjiang.suitlines.SuitLines;
import tech.linjiang.suitlines.Unit;

import static com.silentselene.enterpriseedition.ui.home.GenerateCharts.getDangerList;

public class HomeFragment extends Fragment {
    private DBAdapter dbAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        if (!LoginPage.isLogin(root.getContext())) {
            return root;
        }

        dbAdapter = new DBAdapter(root.getContext());
        dbAdapter.open();
        drawAll(root);
        drawSample(root);

        WiFiRecord[] wiFiRecords = dbAdapter.queryAllData();
        int flag = 0;
        StringBuilder macs = new StringBuilder("");
        if (wiFiRecords == null) {
            return root;
        }
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

    public void drawSample(final View root) {
        CheckBox checkBox = (CheckBox) (root.findViewById(R.id.sampleBox));
        final LinearLayout linearLayout = (LinearLayout) root.findViewById(R.id.sampleLayout);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            SuitLines presuitLines;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    final SuitLines suitLines = new SuitLines(root.getContext());
                    presuitLines = suitLines;
                    LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(-1, 200);
                    linearParams.height = 200;
                    linearParams.setMargins(24, 8, 24, 8);
                    suitLines.setLayoutParams(linearParams);
                    linearLayout.addView(suitLines, 1);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Calendar c = Calendar.getInstance();
                            int now_hour = c.get(Calendar.HOUR_OF_DAY);
                            long time = c.getTimeInMillis();
                            time = time / (1000 * 60 * 60);
                            time = time - 7 * 24;
                            int hour;

                            SuitLines.LineBuilder builder = new SuitLines.LineBuilder();
                            for (int j = 0; j < 7; j++) {
                                List<Unit> lines = new ArrayList<>();
                                for (int i = 0; i < 24; i++) {
                                    time = time + 1;
                                    c.setTimeInMillis(time * (1000 * 60 * 60));
                                    hour = c.get(Calendar.HOUR_OF_DAY);
                                    lines.add(new Unit(new SecureRandom().nextInt(128), (hour > now_hour ? "Y+" :
                                            "T+") + hour));
                                }
                                int[] blue = {0xffd0e7ef, 0xffa5d4e2, 0xff6fc1c9, 0xff419eb7, 0xff3c919f, 0xff327c87, 0xff26616c};
                                builder.add(lines, blue[j]);
                            }
                            try {
                                builder.build(suitLines, true);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    if (presuitLines != null) {
                        linearLayout.removeViewAt(1);
                    }
                }
            }
        });
    }
}