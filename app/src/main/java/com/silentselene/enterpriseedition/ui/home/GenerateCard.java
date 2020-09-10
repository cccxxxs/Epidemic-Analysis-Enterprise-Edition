package com.silentselene.enterpriseedition.ui.home;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.silentselene.enterpriseedition.R;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import tech.linjiang.suitlines.SuitLines;
import tech.linjiang.suitlines.Unit;

public class GenerateCard {
    public static CardView GetCard(final Context context, String title_text, final String mac_text) {
        final CardView cardView = (CardView) LayoutInflater.from(context).inflate(R.layout.select_card, null);
        LinearLayout linearLayout1 = (LinearLayout) (cardView.getChildAt(0));
        LinearLayout linearLayout2 = (LinearLayout) (linearLayout1.getChildAt(1));
        TextView title = (TextView) linearLayout2.getChildAt(0);
        final TextView mac = (TextView) linearLayout2.getChildAt(1);
        title.setText(title_text);
        mac.setText(mac_text);
        CheckBox checkBox = (CheckBox) (linearLayout1.getChildAt(0));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            SuitLines presuitLines=null;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (presuitLines != null) {
                        presuitLines.setVisibility(View.VISIBLE);
                        return ;
                    }
                    final SuitLines suitLines = new SuitLines(context);
                    presuitLines = suitLines;
                    LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(-1, 200);
                    linearParams.height = 200;
                    linearParams.setMargins(24, 8, 24, 8);
                    suitLines.setLayoutParams(linearParams);
                    int index = ((ViewGroup) cardView.getParent()).indexOfChild(cardView);
                    LinearLayout father = ((LinearLayout) cardView.getParent());
                    father.addView(suitLines, index + 1);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            GenerateCharts.drawCharts(suitLines, mac_text);
                        }
                    }).start();
                } else {
                    if (presuitLines != null) {
                        presuitLines.setVisibility(View.GONE);
                    }
                }
            }
        });
        return cardView;
    }
}