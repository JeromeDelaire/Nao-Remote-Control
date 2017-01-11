package com.example.jerome.naoremotecontrol.CORE.LISTENERS;

/**
 * Created by jerome on 11/01/17.
 */

public class Battery {
    private static String battery = "";
    private static ChangeListener listener;

    public static String getBattery(){
        return battery ;
    }

    public static void setBattery(String bat) {
        battery = bat ;
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
