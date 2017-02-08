package com.example.jerome.naoremotecontrol.CORE.LISTENERS;

import android.widget.ArrayAdapter;

/**
 * Created by jerome on 04/02/17.
 */

public class Leds {
    private static ArrayAdapter<String> ledList;
    private static ChangeListener listener;

    public static ArrayAdapter<String> getLedList(){
        return ledList ;
    }

    public static void setLedList(ArrayAdapter<String> list) {
        ledList = list ;
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
