package com.example.jerome.naoremotecontrol.CORE.NETWORK;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by Jerome on 01/11/2016.
 */

public class Reception extends AsyncTask<Void, Void, Void>{

    private BufferedReader in ;
    private String data ;

    Reception(BufferedReader in)
    {
        this.in = in ;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        while (true){
            try {
                data = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
