package com.example.jerome.naoremotecontrol.GLOBAL;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.jerome.naoremotecontrol.R;

/**
 * Created by jerome on 21/10/16.
 */

public class Fonctions {

    public static int convertDp(Context context, int dp){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    // Crée un séparateur
    public static ImageView divider(Context context){
        ImageView divider = new ImageView(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5);
        divider.setLayoutParams(lp);
        divider.setBackgroundResource(R.color.Primary);
        return divider ;
    }
}
