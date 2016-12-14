package com.example.jerome.naoremotecontrol.CORE.LISTENERS;

import java.util.ArrayList;

/**
 * Created by jerome on 07/12/16.
 */

public class Langages {
    private static ArrayList<String> langages ;
    private static Langages.ChangeListener listener ;

    public static ArrayList<String> getLangages() {
        return langages;
    }

    public static void setLangages(ArrayList<String> lan) {
        langages = lan ;
        if (listener != null) listener.onChange();
    }

    public Langages.ChangeListener getListener() {
        return listener;
    }

    public void setListener(Langages.ChangeListener listener) {
        this.listener = listener;
    }

    public interface ChangeListener {
        void onChange();
    }
}
