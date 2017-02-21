package com.example.jerome.naoremotecontrol.CORE.LISTENERS;

/**
 * Created by Jerome on 20/02/2017.
 */

public class Server {
    private static boolean connected ;
    private static ChangeListener listener;

    public static boolean isConnected() {
        return connected;
    }

    public static void setConnected(boolean state) {
        connected = state ;
        com.example.jerome.naoremotecontrol.CORE.NETWORK.Server.setConnect(state);
      /*  if(state){
            com.example.jerome.naoremotecontrol.CORE.NETWORK.Server.setState(1);

        }else
            com.example.jerome.naoremotecontrol.CORE.NETWORK.Server.setState(0);*/

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
