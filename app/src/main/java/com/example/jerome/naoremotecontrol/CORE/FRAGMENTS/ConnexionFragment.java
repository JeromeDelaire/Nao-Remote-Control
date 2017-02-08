package com.example.jerome.naoremotecontrol.CORE.FRAGMENTS;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants;
import com.example.jerome.naoremotecontrol.CORE.NETWORK.Reception;
import com.example.jerome.naoremotecontrol.CORE.NETWORK.Server;
import com.example.jerome.naoremotecontrol.GLOBAL.FileOperator;
import com.example.jerome.naoremotecontrol.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import static com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants.DIRECTORY_NAME;
import static com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants.PORT;
import static com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants.SERVER_IP_FILE;

/**
 * Created by Jerome on 01/11/2016.
 * Fragment permettant de se connecter à un serveur et de le configurer
 */

public class ConnexionFragment extends Fragment implements View.OnClickListener {

    private EditText serverAdress, robotAdress ;
    private Button loginServer, loginServer2, loginRobot, loginRobot2, saveServerAdress, saveRobotAdress,
            removeServerAdress, removeRobotAdress, connexionStatus,  autmaticServerLogin;
    private Spinner listServerAdress, listRobotAdress ;
    private static Server server ;
    private TextView connexionState ;
    private View serverConfig ;
    private BufferedReader in ;
    private boolean majConnexion=false;
    private ProgressBar waitConnexion ;
    private View connexionView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.connexion_fragment, container, false);

    }

    // Maj de l'affichage de connexion lorsque le fragment est recrée
    @Override
    public void onResume(){
        super.onResume();
        if(Server.isConnect()){
            connexionStatus.setVisibility(View.VISIBLE);
            connexionState.setText(R.string.Connected);
            serverConfig.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        if(Server.getState()==0)server = new Server("0.0.0.0", PORT);

        // Widgets de connexion au serveur
        serverAdress = (EditText) view.findViewById(R.id.IPAdressServer);
        loginServer = (Button) view.findViewById(R.id.LoginServerButton);
        loginServer2 = (Button) view.findViewById(R.id.LoginServerButton2);
        saveServerAdress = (Button) view.findViewById(R.id.SaveServerAdressButton);
        removeServerAdress = (Button) view.findViewById(R.id.RemoveServerAdressButton);
        listServerAdress = (Spinner) view.findViewById(R.id.SpinnerServerAdress);
        autmaticServerLogin = (Button) view.findViewById(R.id.AutomaticConnexionButton);
        waitConnexion = (ProgressBar) view.findViewById(R.id.WaitConnexionSpinner);

        connexionStatus = (Button) view.findViewById(R.id.ConnexionStatusButton);
        connexionState = (TextView) view.findViewById(R.id.ConnexionStatusTextView);
        serverConfig = view.findViewById(R.id.ServerConfigLayout);

        // Widgets de connexion au robot
        robotAdress = (EditText) view.findViewById(R.id.RobotIPAdress);
        loginRobot = (Button) view.findViewById(R.id.LoginRobotButton);
        loginRobot2 = (Button) view.findViewById(R.id.LoginRobotButton2);
        saveRobotAdress = (Button) view.findViewById(R.id.SaveRobotAdressButton);
        removeRobotAdress = (Button) view.findViewById(R.id.RemoveRobotAdressButton);
        listRobotAdress = (Spinner) view.findViewById(R.id.SpinnerRobotAdress);

        // Ajout des IP sauvegardés dans le spinner
        initSpinner();

        loginServer.setOnClickListener(this);
        loginServer2.setOnClickListener(this);
        saveServerAdress.setOnClickListener(this);
        removeServerAdress.setOnClickListener(this);
        connexionStatus.setOnClickListener(this);
        autmaticServerLogin.setOnClickListener(this);

        loginRobot.setOnClickListener(this);
        loginRobot2.setOnClickListener(this);
        saveRobotAdress.setOnClickListener(this);
        removeRobotAdress.setOnClickListener(this);

        server.setListener(new Server.ChangeListener() {
            @Override
            public void onChange() {
                //       if(Server.getState()==1){
                majConnexion = true ;
                if(getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (Server.isConnect()) {
                                connexionStatus.setVisibility(View.VISIBLE);
                                connexionState.setText(R.string.Connected);
                                serverConfig.setVisibility(View.VISIBLE);
                                waitConnexion.setVisibility(View.GONE);
                                autmaticServerLogin.setVisibility(View.VISIBLE);

                                try {
                                    in = new BufferedReader(new InputStreamReader(Server.getSocket().getInputStream()));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                Thread thread_reception = new Thread(new Reception(getActivity(), in));
                                thread_reception.start();
                            } else {
                                connexionStatus.setVisibility(View.GONE);
                                connexionState.setText(R.string.Disconected);
                                serverConfig.setVisibility(View.GONE);
                                if(connexionView == loginServer || connexionView == loginServer2)
                                    Toast.makeText(getContext(), R.string.ErrorConnectServer, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

    }


    @Override
    public void onClick(View view) {

        connexionView = view ;

        // Si on clique sur le bouton de connexion automatique au serveur
        if(view == autmaticServerLogin){
            if(!Server.isConnect()) {
                try {

                    // Prend l'adresse ip du téléphone et la sépare en 3
                    String ipPhone = getLocalAddress().toString();
                    ipPhone = ipPhone.replaceFirst("/", "");
                    final String ip1 = ipPhone.substring(0, ipPhone.indexOf('.'));
                    ipPhone = ipPhone.replaceFirst(ip1, "");
                    ipPhone = ipPhone.replaceFirst(".", "");
                    final String ip2 = ipPhone.substring(0, ipPhone.indexOf('.'));
                    ipPhone = ipPhone.replaceFirst(ip2, "");
                    ipPhone = ipPhone.replaceFirst(".", "");
                    final String ip3 = ipPhone.substring(0, ipPhone.indexOf('.'));

                    // Affiche un cercle de chargement
                    waitConnexion.setVisibility(View.VISIBLE);
                    autmaticServerLogin.setVisibility(View.GONE);

                    // tente de se connecter sur chaque adresse IP du sous-réseau
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            int ip = 1;
                            while (!Server.isConnect() && ip < 255) {
                                serverConnexion(ip1 + "." + ip2 + "." + ip3 + "." + ip, autmaticServerLogin);
                                ip++;
                            }
                            if (ip >= 255){
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        waitConnexion.setVisibility(View.GONE);
                                        autmaticServerLogin.setVisibility(View.VISIBLE);
                                        Toast.makeText(getContext(), R.string.ErrorConnectServer, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else
                Toast.makeText(getContext(), R.string.ErrorAlreadyConnected, Toast.LENGTH_SHORT).show();

        }

        // Si on clique sur le bouton de connexion au serveur 1
        if(view == loginServer){
            // Si l'utilisateur a entrée une IP valide
            if(ipCorrect(serverAdress.getText().toString()) && !Server.isConnect())
            {
                serverConnexion(serverAdress.getText().toString(), loginServer);
            }else{
                if(Server.isConnect())
                    Toast.makeText(view.getContext(), R.string.ErrorAlreadyConnected, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(view.getContext(), R.string.ErrorIPNotValid, Toast.LENGTH_SHORT).show();
            }

        }

        // Si on Clique sur le bouton de connexion au serveur 2
        if(view == loginServer2)
        {
            //Si la liste n'est pas vide
            if(listServerAdress.getChildCount() != 0 && !Server.isConnect())
            {
                serverConnexion(listServerAdress.getSelectedItem().toString(), loginServer2);
            }else{
                if(Server.isConnect())
                    Toast.makeText(view.getContext(), R.string.ErrorAlreadyConnected, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(view.getContext(), R.string.ErrorNoItemSelected, Toast.LENGTH_SHORT).show();
            }

        }

        // Si on clique sur le bouton de sauvegarde de l'ip serveur
        if(view == saveServerAdress){
            // Si l'adresse Ip est valide
            if(ipCorrect(serverAdress.getText().toString()))
            {
                // Si l'IP n'est pas déjà sauvegardée
                if(!FileOperator.fileContain(DIRECTORY_NAME,SERVER_IP_FILE, serverAdress.getText().toString()))
                {
                    // On ajoute l'ip dans le mémoire du téléphone
                    if(!FileOperator.writeFile(DIRECTORY_NAME, SERVER_IP_FILE, serverAdress.getText().toString()))
                        Toast.makeText(view.getContext(), R.string.ErrorFile , Toast.LENGTH_SHORT).show();
                    else
                    {
                        Toast.makeText(view.getContext(), R.string.AdrressSavedSucces , Toast.LENGTH_SHORT).show();
                        serverAdress.setText("");
                    }

                    // Ajout des IP sauvegardés dans le spinner
                    initSpinner();
                }else
                    Toast.makeText(view.getContext(), R.string.ErrorIPExists, Toast.LENGTH_SHORT).show();

            }else
                Toast.makeText(view.getContext(), R.string.ErrorIPNotValid, Toast.LENGTH_SHORT).show();
        }

        // Si clique sur le bouton de suppresion d'adresses
        if(view == removeServerAdress)
        {
            // Si la liste n'est pas vide
            if(listServerAdress.getChildCount()!=0){
                if(FileOperator.removeLigne(DIRECTORY_NAME, SERVER_IP_FILE, listServerAdress.getSelectedItem().toString()))
                    initSpinner();
            }else{
                Toast.makeText(view.getContext(), R.string.ErrorEmptyList, Toast.LENGTH_SHORT).show();
            }
        }

        // Si on clique sur le bouton de deconnexion
        if(view == connexionStatus)
        {
            server.stopConnexion();
        }

        // Si on clique sur le bouton de connexion au robot 1
        if(view == loginRobot){
            // Si l'utilisateur a entrée une IP valide
            if(ipCorrect(robotAdress.getText().toString()))
            {
                Server.send(Constants.CONFIGURATION + Constants.ROBOT_IP + robotAdress.getText().toString());
            }else{
                Toast.makeText(view.getContext(), R.string.ErrorIPNotValid, Toast.LENGTH_SHORT).show();
            }

        }

        // Si on Clique sur le bouton de connexion au robot 2
        if(view == loginRobot2)
        {
            //Si la liste n'est pas vide
            if(listRobotAdress.getChildCount() != 0)
            {
                Server.send(Constants.CONFIGURATION + Constants.ROBOT_IP + listRobotAdress.getSelectedItem().toString());
            }else{
                Toast.makeText(view.getContext(), R.string.ErrorNoItemSelected, Toast.LENGTH_SHORT).show();
            }

        }

        // Si on clique sur le bouton de sauvegarde de l'ip du robot
        if(view == saveRobotAdress){
            // Si l'adresse Ip est valide
            if(ipCorrect(robotAdress.getText().toString()))
            {
                // Si l'IP n'est pas déjà sauvegardée
                if(!FileOperator.fileContain(DIRECTORY_NAME,Constants.ROBOT_IP_FILE, robotAdress.getText().toString()))
                {
                    // On ajoute l'ip dans le mémoire du téléphone
                    if(!FileOperator.writeFile(DIRECTORY_NAME, Constants.ROBOT_IP_FILE, robotAdress.getText().toString()))
                        Toast.makeText(view.getContext(), R.string.ErrorFile , Toast.LENGTH_SHORT).show();
                    else
                    {
                        Toast.makeText(view.getContext(), R.string.AdrressSavedSucces , Toast.LENGTH_SHORT).show();
                        robotAdress.setText("");
                    }

                    // Ajout des IP sauvegardés dans le spinner
                    initSpinner();
                }else
                    Toast.makeText(view.getContext(), R.string.ErrorIPExists, Toast.LENGTH_SHORT).show();

            }else
                Toast.makeText(view.getContext(), R.string.ErrorIPNotValid, Toast.LENGTH_SHORT).show();
        }

        // Si clique sur le bouton de suppresion d'adresses d'IP du robot
        if(view == removeRobotAdress)
        {
            // Si la liste n'est pas vide
            if(listRobotAdress.getChildCount()!=0){
                if(FileOperator.removeLigne(DIRECTORY_NAME, Constants.ROBOT_IP_FILE, listRobotAdress.getSelectedItem().toString()))
                    initSpinner();
            }else{
                Toast.makeText(view.getContext(), R.string.ErrorEmptyList, Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void serverConnexion(String ip, final View view){

        // On se connecte au serveur
        Server.setDstAddress(ip);
        Server.setDstPort(PORT);
        new Thread(server).start();
        while (!majConnexion);
        majConnexion=false;
    }

    // Ajout les IP sauvegardés du serveur et du robot dans les spinner
    public void initSpinner(){
        // IP serveur
        String[] listIPSaved = FileOperator.getLignes(DIRECTORY_NAME, SERVER_IP_FILE);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, listIPSaved);
        listServerAdress.setAdapter(adapter);

        // IP robot
        String[] listRobotIPSaved = FileOperator.getLignes(DIRECTORY_NAME, Constants.ROBOT_IP_FILE);
        ArrayAdapter<String> adapter_robot = new ArrayAdapter<>(getContext(), R.layout.spinner_item, listRobotIPSaved);
        listRobotAdress.setAdapter(adapter_robot);
    }

    public boolean ipCorrect(String ip){
        return ip.matches("([0-9]{1,3}\\.){3}[0-9]{1,3}");
    }

    private InetAddress getLocalAddress()throws IOException {

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        //return inetAddress.getHostAddress().toString();
                        return inetAddress;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("SALMAN", ex.toString());
        }
        return null;
    }
}


