package com.example.jerome.naoremotecontrol.CORE.NETWORK;

import com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jerome on 11/11/16.
 */

public class Request extends TimerTask{

    @Override
    public void run() {
        if (Server.getState() == 1) {
            Server.send(Constants.GET + Constants.VOLUME);
        }

    }

}
