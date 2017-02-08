package com.example.jerome.naoremotecontrol.CORE.LISTENERS;

import android.widget.ArrayAdapter;

/**
 * Created by jerome on 08/02/17.
 */

public class Behavior {
    private static ArrayAdapter<String> behaviorList;
    private static ChangeListener listener;

    public static ArrayAdapter<String> getBehaviorList(){
        return behaviorList ;
    }

    public static void setBehaviorList(ArrayAdapter<String> list) {
        behaviorList = list ;
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
