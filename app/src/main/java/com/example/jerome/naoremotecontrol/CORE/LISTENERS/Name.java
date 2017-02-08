package com.example.jerome.naoremotecontrol.CORE.LISTENERS;

/**
 * Created by jerome on 04/02/17.
 */

public class Name {
    private static String name = "";
    private static ChangeListener listener;

    public static String getName() {
        return name;
    }

    public static void setName(String naoName) {
        name = naoName ;
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
