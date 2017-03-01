package com.example.jerome.naoremotecontrol.CORE.LISTENERS;

/**
 * Created by jerome on 01/03/17.
 */

public class RobotConnexion {
    private static boolean connected ;
    private static ChangeListener listener;

    public static boolean isConnected() {
        return connected;
    }

    public static void setConnected(boolean state) {
        connected = state ;
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
