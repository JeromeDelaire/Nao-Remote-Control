package com.example.jerome.naoremotecontrol.CORE.NETWORK;

import android.os.AsyncTask;
import android.widget.Toast;

import com.example.jerome.naoremotecontrol.GLOBAL.FileOperator;

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
            FileOperator.writeFile(DIRECTORY_NAME, "test.txt", "ok\n");
            try {
                data = in.readLine();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
