package com.example.jerome.naoremotecontrol.CORE.NETWORK;

import android.os.AsyncTask;

import java.io.PrintWriter;

/**
 * Created by Jerome on 01/11/2016.
 */

public class Transmission implements Runnable{

    private PrintWriter out ;

    public Transmission(PrintWriter out){
        this.out = out ;
    }

    @Override
    public void run() {

    }
}
