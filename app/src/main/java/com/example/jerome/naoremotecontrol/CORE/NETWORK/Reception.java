package com.example.jerome.naoremotecontrol.CORE.NETWORK;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jerome.naoremotecontrol.CORE.FRAGMENTS.MoveFragment;
import com.example.jerome.naoremotecontrol.CORE.FRAGMENTS.SpeechFragment;
import com.example.jerome.naoremotecontrol.CORE.FRAGMENTS.StatusFragment;
import com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants;
import com.example.jerome.naoremotecontrol.CORE.LISTENERS.Battery;
import com.example.jerome.naoremotecontrol.CORE.LISTENERS.Behavior;
import com.example.jerome.naoremotecontrol.CORE.LISTENERS.Langage;
import com.example.jerome.naoremotecontrol.CORE.LISTENERS.Leds;
import com.example.jerome.naoremotecontrol.CORE.LISTENERS.Name;
import com.example.jerome.naoremotecontrol.CORE.LISTENERS.Posture;
import com.example.jerome.naoremotecontrol.CORE.LISTENERS.Voice;
import com.example.jerome.naoremotecontrol.CORE.LISTENERS.Volume;
import com.example.jerome.naoremotecontrol.MainActivity;
import com.example.jerome.naoremotecontrol.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by Jerome on 01/11/2016.
 */

public class Reception implements Runnable{

    private BufferedReader in ;
    private Activity activity ;
    public static int [] pixels = new int[128*128];

    public Reception(Activity activity, BufferedReader in)
    {
        this.activity = activity ;
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

                                ArrayAdapter<String> posturesAdapter = new ArrayAdapter<>(activity, R.layout.spinner_item, postures);
                                Posture.setPosturesList(posturesAdapter);

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

                                ArrayAdapter langageAdapter = new ArrayAdapter(activity, R.layout.spinner_item, langages);
                                Langage.setLangageList(langageAdapter);

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

                                ArrayAdapter<String> voiceAdpater = new ArrayAdapter<String>(activity, R.layout.spinner_item, voices);
                                Voice.setVoiceList(voiceAdpater);
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

                                ArrayAdapter<String> behaviorAdapter = new ArrayAdapter<>(activity, R.layout.spinner_item, behavior);
                                Behavior.setBehaviorList(behaviorAdapter);

                                break;

                            // Si on reçoit le nom du robot
                            case Constants.NAO_NAME :
                                data = data.replaceAll(Constants.NAO_NAME, "");
                                Name.setName(data);
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

                            // Si on reçoit la liste des groupes de leds
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
                                //.setAvailableLedGroups(leds);
                                ArrayAdapter<String> ledsAdapter = new ArrayAdapter<>(activity, R.layout.spinner_item, leds);
                                Leds.setLedList(ledsAdapter);

                                break ;

                            case Constants.RAW_DATA :

                                data = data.replaceAll(Constants.RAW_DATA, "");

                                Byte [] rawData = new Byte[128*128];

                                data = data.replaceAll(" ", "");

                                int i=0 ;
                                while(data != ""){
                                    String str = data.substring(0, 8);
                                    data = data.replaceFirst(str, "");
                                    pixels[i] = Integer.parseInt(str, 2);
                                    i++;
                                }

                                data = "" ;
                                break ;
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }
}
