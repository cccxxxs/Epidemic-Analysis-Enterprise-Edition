package com.silentselene.enterpriseedition.ui.home;


import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.silentselene.enterpriseedition.R;

public class GenerateCard {
    public static CardView GetCard(Context context, String title_text, String mac_text) {
        CardView cardView = (CardView) LayoutInflater.from(context).inflate(R.layout.select_card, null);
        LinearLayout linearLayout1 = (LinearLayout) (cardView.getChildAt(0));
        LinearLayout linearLayout2 = (LinearLayout) (linearLayout1.getChildAt(1));
        TextView title = (TextView) linearLayout2.getChildAt(0);
        TextView mac = (TextView) linearLayout2.getChildAt(1);
        title.setText(title_text);
        mac.setText(mac_text);
        CheckBox checkBox = (CheckBox) (linearLayout1.getChildAt(0));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });
        return cardView;
    }
}