package com.example.jerome.naoremotecontrol.CORE.INTERFACES;

/**
 * Created by Jerome on 01/11/2016.
 */

public interface Constants{
    String DIRECTORY_NAME = "Nao Remote Control" ;
    String SERVER_IP_FILE = "IP.txt" ;
    String SPEECH_FILE = "Speech.txt" ;
    int PORT = 38771 ;


    /*
    * Communication avec le serveur
    * */
    // Configurations du serveur
    String CONFIGURATION = "CFG/";
        String STOP_CONNEXION = "STP/";

    // Faire parler le robot
    String SAY = "SAY/";
        String ANIMATION = "ANM/";
        String NO_ANIMATION = "NAM/" ;

    // Réglage du robot
    String SETTING = "SET/" ;
        String VOLUME = "VOL/" ;
        String LED = "LED/" ;
            String RIGHT_EYE = "REC/" ;
            String LEFT_EYE = "LEC/" ;
            String ALL_LEDS = "ALL/" ;
        String LANGAGE = "LAN/";


    // Demande d'informations
    String GET = "GET/";
        //String VOLUME = " VOL/" ;

}