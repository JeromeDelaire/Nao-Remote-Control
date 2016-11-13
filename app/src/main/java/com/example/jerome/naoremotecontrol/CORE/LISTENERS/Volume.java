package com.example.jerome.naoremotecontrol.CORE.LISTENERS;

/**
 * Created by jerome on 12/11/16.
 */

public class Volume {
    private static int volume = 0;
    private static ChangeListener listener;

    public static int getVolume() {
        return volume;
    }

    public static void setVolume(int vol) {
        volume = vol ;
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
