package com.example.jerome.naoremotecontrol.CORE.LISTENERS;

import android.widget.ArrayAdapter;

/**
 * Created by jerome on 08/02/17.
 */

public class Langage {
    private static ArrayAdapter<String> langageList;
    private static ChangeListener listener;

    public static ArrayAdapter<String> getLangageList(){
        return langageList;
    }

    public static void setLangageList(ArrayAdapter<String> list) {
        langageList = list ;
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
