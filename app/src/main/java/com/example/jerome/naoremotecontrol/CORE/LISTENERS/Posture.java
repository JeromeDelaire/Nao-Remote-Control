package com.example.jerome.naoremotecontrol.CORE.LISTENERS;

import android.widget.ArrayAdapter;

/**
 * Created by jerome on 08/02/17.
 */

public class Posture {
    private static ArrayAdapter<String> posturesList;
    private static ChangeListener listener;

    public static ArrayAdapter<String> getPosturesList(){
        return posturesList ;
    }

    public static void setPosturesList(ArrayAdapter<String> list) {
        posturesList = list ;
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
