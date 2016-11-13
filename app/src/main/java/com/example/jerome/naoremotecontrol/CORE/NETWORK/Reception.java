package com.example.jerome.naoremotecontrol.CORE.NETWORK;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.jerome.naoremotecontrol.CORE.FRAGMENTS.SpeechFragment;
import com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants;
import com.example.jerome.naoremotecontrol.CORE.LISTENERS.Volume;
import com.example.jerome.naoremotecontrol.GLOBAL.FileOperator;
import com.example.jerome.naoremotecontrol.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import static com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants.DIRECTORY_NAME;

/**
 * Created by Jerome on 01/11/2016.
 */

public class Reception implements Runnable{

    private BufferedReader in ;
    private String data ;

    public Reception(BufferedReader in)
    {
        this.in = in ;
    }

    @Override
    public void run() {

        while (true){

            if(Server.getState() == 1){
                try {
                    data = in.readLine();

                    if(data != null){

                        switch (data.substring(0,4)){

                            // Si on reçoit la valeur du volume
                            case Constants.VOLUME :
                                data = data.replaceAll(Constants.VOLUME, "");
                                int vol = Integer.parseInt(data);
                                Volume.setVolume(vol);
                                break;
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }
}
