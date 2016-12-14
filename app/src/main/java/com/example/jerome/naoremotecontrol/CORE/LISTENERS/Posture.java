package com.example.jerome.naoremotecontrol.CORE.LISTENERS;

import java.util.ArrayList;

/**
 * Created by jerome on 30/11/16.
 */

public class Posture {
    private static ArrayList<String > postures ;
    private static ChangeListener listener ;

    public static ArrayList<String> getPostures() {
        return postures;
    }

    public static void setPostures(ArrayList<String> pos) {
        postures = pos ;
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
