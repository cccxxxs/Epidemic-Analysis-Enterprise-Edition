package com.silentselene.enterpriseedition.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.silentselene.enterpriseedition.R;

public class GenerateCard {
    public static CardView GetCard(Context context, String title_text, String mac_text) {
        CardView cardView = (CardView) LayoutInflater.from(context).inflate(R.layout.card, null);
        LinearLayout linearLayout = (LinearLayout) (cardView.getChildAt(0));
        TextView title = (TextView) linearLayout.getChildAt(0);
        TextView mac = (TextView) linearLayout.getChildAt(1);
        title.setText(title_text);
        mac.setText(mac_text);
        return cardView;
    }
}
