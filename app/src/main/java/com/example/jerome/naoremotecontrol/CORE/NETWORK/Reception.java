package com.example.jerome.naoremotecontrol.CORE.NETWORK;

import com.example.jerome.naoremotecontrol.CORE.FRAGMENTS.MoveFragment;
import com.example.jerome.naoremotecontrol.CORE.FRAGMENTS.SpeechFragment;
import com.example.jerome.naoremotecontrol.CORE.FRAGMENTS.StatusFragment;
import com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants;
import com.example.jerome.naoremotecontrol.CORE.LISTENERS.Battery;
import com.example.jerome.naoremotecontrol.CORE.LISTENERS.Volume;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Jerome on 01/11/2016.
 */

public class Reception implements Runnable{

    private BufferedReader in ;

    public Reception(BufferedReader in)
    {
        this.in = in ;
    }

    @Override
    public void run() {

        while(true){

            // Si on est connecté au serveur
            if(Server.getState() == 1){
                try {
                    String data = in.readLine();

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

                               ArrayList<String> postures = new ArrayList<>();

                                data = data.replaceAll(Constants.POST_LIST, "");
                                data = data.substring(1, data.length()-1);
                                data = data.replaceAll(", ", "/");
                                data = data + "/" ;

                                int numberPost = 0 ;

                                for(int i = 0; i< data.length(); i++){
                                    if(data.charAt(i)=='/')numberPost++;
                                }

                                for(int i=0 ; i<numberPost ; i++){
                                    postures.add(data.substring(0, data.indexOf('/'))) ;
                                    data = data.replaceAll(postures.get(i), "");
                                    data = data.replaceFirst("/", "");
                                }

                                MoveFragment.setAvailablePostures(postures);
                                break;

                            // Si on recoit la liste des langages disponibles
                            case Constants.LANGAGE :

                                ArrayList<String> langages = new ArrayList<>();

                                data = data.replaceAll(Constants.LANGAGE, "");
                                data = data.substring(1, data.length()-1);
                                data = data.replaceAll(", ", "/");
                                data = data + "/" ;

                                int numberLangages = 0 ;

                                for(int i = 0; i< data.length(); i++){
                                    if(data.charAt(i)=='/')numberLangages++;
                                }

                                for(int i=0 ; i<numberLangages ; i++){
                                    langages.add(data.substring(0, data.indexOf('/'))) ;
                                    data = data.replaceAll(langages.get(i), "");
                                    data = data.replaceFirst("/", "");
                                }

                                SpeechFragment.setAvailableLangages(langages);

                                break;

                            // Si on recoit la liste des voies disponibles
                            case Constants.VOICES :

                                ArrayList<String> voices = new ArrayList<>();

                                data = data.replaceAll(Constants.VOICES, "");
                                data = data.substring(1, data.length()-1);
                                data = data.replaceAll(", ", "/");
                                data = data + "/" ;

                                int numberLVoices = 0 ;

                                for(int i = 0; i< data.length(); i++){
                                    if(data.charAt(i)=='/')numberLVoices++;
                                }

                                for(int i=0 ; i<numberLVoices ; i++){
                                    voices.add(data.substring(0, data.indexOf('/'))) ;
                                    data = data.replaceAll(voices.get(i), "");
                                    data = data.replaceFirst("/", "");
                                }

                                SpeechFragment.setAvailableVoices(voices);

                                break;

                            // Si on recoit la liste des comportements disponibles
                            case Constants.BEHAVIOR :

                                ArrayList<String> behavior = new ArrayList<>();

                                data = data.replaceAll(Constants.BEHAVIOR, "");
                                data = data.substring(1, data.length()-1);
                                data = data.replaceAll(", ", "#");
                                data = data + "#" ;

                                int numberBehavior = 0 ;

                                for(int i = 0; i< data.length(); i++){
                                    if(data.charAt(i)=='#')numberBehavior++;
                                }

                                for(int i=0 ; i<numberBehavior ; i++){
                                    behavior.add(data.substring(0, data.indexOf('#'))) ;
                                    data = data.replaceFirst(behavior.get(i), "");
                                    data = data.replaceFirst("#", "");
                                }

                                MoveFragment.setAvailableBehavior(behavior);

                                break;

                            // Si on reçoit le nom du robot
                            case Constants.NAO_NAME :
                                data = data.replaceAll(Constants.NAO_NAME, "");
                                StatusFragment.setName(data);
                                break;

                            // Si on reçoit le niveau de batterie
                            case Constants.BATTERY :
                                data = data.replaceAll(Constants.BATTERY, "");
                                Battery.setBattery(data);
                                break;

                            // Si le serveur viens de se connecter au robot
                            case Constants.ROBOT_CONNECTED :
                                // Demande d'informations sur le robot au serveur
                                Server.send(Constants.GET + Constants.LANGAGES);
                                Server.send(Constants.GET + Constants.VOICES);
                                Server.send(Constants.GET + Constants.VOLUME);
                                Server.send(Constants.GET + Constants.POST_LIST);
                                Server.send(Constants.GET + Constants.BEHAVIOR);
                                Server.send(Constants.GET + Constants.NAO_NAME);
                                Server.send(Constants.GET + Constants.BATTERY);
                                Server.send(Constants.GET + Constants.LED_LIST);
                                break;

                            case Constants.LED_LIST :

                                ArrayList<String> leds = new ArrayList<>();

                                data = data.replaceAll(Constants.LED_LIST, "");
                                data = data.substring(1, data.length()-1);
                                data = data.replaceAll(", ", "/");
                                data = data + "/" ;

                                int numberLeds = 0 ;

                                for(int i = 0; i< data.length(); i++){
                                    if(data.charAt(i)=='/')numberLeds++;
                                }

                                for(int i=0 ; i<numberLeds ; i++){
                                    leds.add(data.substring(0, data.indexOf('/'))) ;
                                    data = data.replaceFirst(leds.get(i), "");
                                    data = data.replaceFirst("/", "");
                                }

                                StatusFragment.setAvailableLedGroups(leds);

                                break ;

                          /*  case Constants.RAW_DATA :
                                data = data.replaceAll(Constants.RAW_DATA, "");

                                int len = data.length();
                                byte[] raw = new byte[len / 2];
                                for (int i = 0; i < len; i += 2) {
                                    raw[i / 2] = (byte) ((Character.digit(data.charAt(i), 16) << 4)
                                            + Character.digit(data.charAt(i+1), 16));
                                }

                                MoveFragment.testImage = raw ;
                                break ;*/
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }
}
