package com.example.jerome.naoremotecontrol.CORE.NETWORK;

import android.os.AsyncTask;
import android.view.View;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import static com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants.CONFIGURATION;
import static com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants.STOP_CONNEXION;

/**
 * Created by Jerome on 01/11/2016.
 */

public class Server extends AsyncTask<Void, Void, Void> {

    private static String dstAddress;
    private static int dstPort;
    private static Socket socket = null;
    private static PrintWriter out;
    private static int state ;

    public Server(String addr, int port, View v) {
        dstAddress = addr;
        dstPort = port;
        state = 0 ;
    }

    public Socket getSocket() {
        return socket;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override

    protected Void doInBackground(Void... voids) {

        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(dstAddress, dstPort), 2000);
            out = new PrintWriter(socket.getOutputStream());
            if(socket.isBound() && socket.isConnected())
            state = 1 ;

        } catch (IOException e) {
            state = -1 ;
            e.printStackTrace();
        }
        return null;
    }

    public void stopConnexion() {
        try {
            out.println(CONFIGURATION + STOP_CONNEXION);
            out.flush();
            socket.close();
            state = 0 ;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void send(String message){
        out.println(message);
        out.flush();
    }
}