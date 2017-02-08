package com.example.jerome.naoremotecontrol.CORE.LISTENERS;

import android.widget.ArrayAdapter;

/**
 * Created by jerome on 08/02/17.
 */

public class Voice {
    private static ArrayAdapter<String> voiceList;
    private static ChangeListener listener;

    public static ArrayAdapter<String> getVoiceList(){
        return voiceList ;
    }

    public static void setVoiceList(ArrayAdapter<String> list) {
        voiceList = list ;
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
