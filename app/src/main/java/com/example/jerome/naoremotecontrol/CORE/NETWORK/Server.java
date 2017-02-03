package com.example.jerome.naoremotecontrol.CORE.NETWORK;

import com.example.jerome.naoremotecontrol.CORE.LISTENERS.Battery;
import com.example.jerome.naoremotecontrol.GLOBAL.FileOperator;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import static com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants.CONFIGURATION;
import static com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants.DIRECTORY_NAME;
import static com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants.STOP_CONNEXION;

/**
 * Created by Jerome on 01/11/2016.
 */

public class Server implements Runnable {

    private static String dstAddress;
    private static int dstPort;
    private static Socket socket = null;
    private static PrintWriter out;
    private static int state ;
    private static ChangeListener listener;
    private static boolean connect;

    public Server(String addr, int port) {
        dstAddress = addr;
        dstPort = port;
        state = 0 ;
        connect=false;
    }

    public static Socket getSocket() {
        return socket;
    }

    public static int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public static boolean isConnect() {
        return connect;
    }

    public void stopConnexion() {
        try {
            out.println(CONFIGURATION + STOP_CONNEXION);
            out.flush();
            socket.close();
            state = 0 ;
            connect=false;
            if (listener != null) listener.onChange();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void send(String message){
        message = message.replaceAll("[\r\n]+", " ");
        out.println(message);
        out.flush();
    }

    @Override
    public void run() {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(dstAddress, dstPort), 100);
            out = new PrintWriter(socket.getOutputStream());
            if(socket.isBound() && socket.isConnected()){
                state = 1 ;
                connect=true;
                if (listener != null) listener.onChange();
            }


        } catch (IOException e) {
            state = -1 ;
            if (listener != null) listener.onChange();
            e.printStackTrace();
        }
    }

    public ChangeListener getListener() {
        return listener;
    }

    public static String getDstAddress() {
        return dstAddress;
    }

    public static void setDstAddress(String dstAddress) {
        Server.dstAddress = dstAddress;
    }

    public static int getDstPort() {
        return dstPort;
    }

    public static void setDstPort(int dstPort) {
        Server.dstPort = dstPort;
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;

    }

    public interface ChangeListener {
        void onChange();
    }
}