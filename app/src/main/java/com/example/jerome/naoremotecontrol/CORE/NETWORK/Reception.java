package com.example.jerome.naoremotecontrol.CORE.NETWORK;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.jerome.naoremotecontrol.CORE.FRAGMENTS.MoveFragment;
import com.example.jerome.naoremotecontrol.CORE.FRAGMENTS.SpeechFragment;
import com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants;
import com.example.jerome.naoremotecontrol.CORE.LISTENERS.Langages;
import com.example.jerome.naoremotecontrol.CORE.LISTENERS.Posture;
import com.example.jerome.naoremotecontrol.CORE.LISTENERS.Volume;
import com.example.jerome.naoremotecontrol.GLOBAL.FileOperator;
import com.example.jerome.naoremotecontrol.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants.DIRECTORY_NAME;
import static com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants.VOLUME;

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

            // Si on est connecté au serveur
            if(Server.getState() == 1){
                try {
                    data = in.readLine();

                    // Si on a recu des données
                    if(data != null){

                        switch (data.substring(0,4)){

                            // Si on reçoit la valeur du volume
                            case Constants.VOLUME :
                                data = data.replaceAll(Constants.VOLUME, "");
                                int vol = Integer.parseInt(data);
                                Volume.setVolume(vol);
                                break;

                            // Si on reçoit la liste des postures disponibles
                            case Constants.POST_LIST :

                               ArrayList<String> postures = new ArrayList<String>();

                                data = data.replaceAll(Constants.POST_LIST, "");
                                data = data.substring(1,data.length()-1);
                                data = data.replaceAll(", ", "/");
                                data = data + "/" ;

                                int numberPost = 0 ;

                                for(int i=0 ; i<data.length(); i++){
                                    if(data.charAt(i)=='/')numberPost++;
                                }

                                for(int i=0 ; i<numberPost ; i++){
                                    postures.add(data.substring(0, data.indexOf('/'))) ;
                                    data = data.replaceAll(postures.get(i).toString(), "");
                                    data = data.replaceFirst("/", "");
                                }

                               Posture.setPostures(postures);
                                break;

                            // Si on recoit la liste des langages disponibles
                            case Constants.LANGAGE :

                                ArrayList<String> langages = new ArrayList<String>();

                                data = data.replaceAll(Constants.LANGAGE, "");
                                data = data.substring(1,data.length()-1);
                                data = data.replaceAll(", ", "/");
                                data = data + "/" ;

                                int numberLangages = 0 ;

                                for(int i=0 ; i<data.length(); i++){
                                    if(data.charAt(i)=='/')numberLangages++;
                                }

                                for(int i=0 ; i<numberLangages ; i++){
                                    langages.add(data.substring(0, data.indexOf('/'))) ;
                                    data = data.replaceAll(langages.get(i).toString(), "");
                                    data = data.replaceFirst("/", "");
                                }

                                Langages.setLangages(langages);

                                break;

                            // Si on recoit la liste des voies disponibles
                            case Constants.VOICES :

                                ArrayList<String> voices = new ArrayList<String>();

                                data = data.replaceAll(Constants.VOICES, "");
                                data = data.substring(1,data.length()-1);
                                data = data.replaceAll(", ", "/");
                                data = data + "/" ;

                                int numberLVoices = 0 ;

                                for(int i=0 ; i<data.length(); i++){
                                    if(data.charAt(i)=='/')numberLVoices++;
                                }

                                for(int i=0 ; i<numberLVoices ; i++){
                                    voices.add(data.substring(0, data.indexOf('/'))) ;
                                    data = data.replaceAll(voices.get(i).toString(), "");
                                    data = data.replaceFirst("/", "");
                                }

                                SpeechFragment.setAvailableVoices(voices);

                                break;

                            // Si on recoit la liste des comportements disponibles
                            case Constants.BEHAVIOR :

                                ArrayList<String> behavior = new ArrayList<String>();

                                data = data.replaceAll(Constants.BEHAVIOR, "");
                                data = data.substring(1,data.length()-1);
                                data = data.replaceAll(", ", "#");
                                data = data + "#" ;

                                int numberBehavior = 0 ;

                                for(int i=0 ; i<data.length(); i++){
                                    if(data.charAt(i)=='#')numberBehavior++;
                                }

                                for(int i=0 ; i<numberBehavior ; i++){
                                    behavior.add(data.substring(0, data.indexOf('#'))) ;
                                    data = data.replaceFirst(behavior.get(i).toString(), "");
                                    data = data.replaceFirst("#", "");
                                }

                                MoveFragment.setAvailableBehavior(behavior);

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
