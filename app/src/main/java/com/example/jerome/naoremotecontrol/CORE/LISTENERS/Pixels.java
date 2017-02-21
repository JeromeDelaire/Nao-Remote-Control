package com.example.jerome.naoremotecontrol.CORE.LISTENERS;

import android.widget.ArrayAdapter;

/**
 * Created by Jerome on 20/02/2017.
 */

public class Pixels {
    private static int pixels[] = new int[128*128];
    private static ChangeListener listener;

    public static int [] getPixels(){
        return pixels;
    }

    public static void setPixels(int pixelsTab[]) {
        pixels = pixelsTab ;
        if (listener != null) listener.onChange();
    }

    public ChangeListener getListener() {
        return listener;
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    public interface ChangeListener {
        void onChange();
    }
}
