package com.example.jerome.naoremotecontrol.CORE.FRAGMENTS;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import static com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants.DIRECTORY_NAME;
import static com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants.PORT;
import static com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants.SERVER_IP_FILE;

/**
 * Created by Jerome on 01/11/2016.
 * Fragment permettant de se connecter à un serveur et de le configurer
 */

public class ConnexionFragment extends Fragment implements View.OnClickListener {

    private EditText serverAdress ;
    private Button loginServer, loginServer2, saveServerAdress, removeServerAdress, connexionStatus ;
    private Spinner listServerAdress ;
    private Server server ;
    private TextView connexionState ;
    private View serverConfig ;
    private BufferedReader in ;
    private Thread thread_reception ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        server = new Server("0.0.0.0", PORT, getView());
        return inflater.inflate(R.layout.connexion_fragment, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {

        serverAdress = (EditText) view.findViewById(R.id.IPAdressServer);
        loginServer = (Button) view.findViewById(R.id.LoginServerButton);
        loginServer2 = (Button) view.findViewById(R.id.LoginServerButton2);
        saveServerAdress = (Button) view.findViewById(R.id.SaveServerAdressButton);
        removeServerAdress = (Button) view.findViewById(R.id.RemoveServerAdressButton);
        listServerAdress = (Spinner) view.findViewById(R.id.SpinnerServerAdress);
        connexionStatus = (Button) view.findViewById(R.id.ConnexionStatusButton);
        connexionState = (TextView) view.findViewById(R.id.ConnexionStatusTextView);
        serverConfig = (View) view.findViewById(R.id.ServerConfigLayout);

        // Ajout des IP sauvegardés dans le spinner
        initSpinner();

        loginServer.setOnClickListener(this);
        loginServer2.setOnClickListener(this);
        saveServerAdress.setOnClickListener(this);
        removeServerAdress.setOnClickListener(this);
        connexionStatus.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        // Si on clique sur le bouton de connexion 1
        if(view == loginServer){
            // Si l'utilisateur a entrée une IP valide
            if(ipCorrect(serverAdress.getText().toString()) && server.getState() != 1)
            {
                serverConnexion(serverAdress.getText().toString());
            }else{
                if(server.getState() == 1)
                    Toast.makeText(view.getContext(), R.string.ErrorAlreadyConnected, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(view.getContext(), R.string.ErrorIPNotValid, Toast.LENGTH_SHORT).show();
            }

        }

        // Si on Clique sur le bouton de connexion 2
        if(view == loginServer2)
        {
            //Si la liste n'est pas vide
            if(listServerAdress.getChildCount() != 0 && server.getState() != 1)
            {
                serverConnexion(listServerAdress.getSelectedItem().toString());
            }else{
                if(server.getState() == 1)
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
            if(FileOperator.removeLigne(DIRECTORY_NAME, SERVER_IP_FILE, listServerAdress.getSelectedItem().toString()))
                initSpinner();
        }

        // Si on clique sur le bouton de deconnexion
        if(view == connexionStatus)
        {
           // thread_reception.stop();
            server.stopConnexion();
            connexionStatus.setVisibility(View.GONE);
            connexionState.setText(R.string.Disconected);
            serverConfig.setVisibility(View.GONE);
        }
    }

    public void serverConnexion(String ip){

        // On se connecte au serveur
        server = new Server(ip, PORT, getView());
        server.execute();
        //Si la connection a réussie on active le bouton de déconnexion
        while(server.getState()==0);

        if(server.getState()==1){
            connexionStatus.setVisibility(View.VISIBLE);
            connexionState.setText(R.string.Connected);
            serverConfig.setVisibility(View.VISIBLE);
            try {
                in = new BufferedReader(new InputStreamReader(server.getSocket().getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            thread_reception = new Thread(new Reception(in));
            thread_reception.start();

            Server.send(Constants.GET + Constants.VOLUME);
        }
        else
        {
            Toast.makeText(getContext(), R.string.ErrorConnectServer, Toast.LENGTH_SHORT).show();
            connexionState.setText(R.string.Disconected);
            serverConfig.setVisibility(View.GONE);
        }

    }

    public void initSpinner(){
        String[] listIPSaved = FileOperator.getLignes(DIRECTORY_NAME, SERVER_IP_FILE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, listIPSaved);
        listServerAdress.setAdapter(adapter);
    }

    public boolean ipCorrect(String ip){
        return ip.matches("([0-9]{1,3}\\.){3}[0-9]{1,3}");
    }
}
