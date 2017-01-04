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
        //Réglage de la fréquence de lavoix
        String PITCH_SHIFTING = "PTS/" ;
        // Choix de la voix
        String SPEAKING_VOICE = "SPV/" ;

    // Demande d'informations
    String GET = "GET/";
        //String VOLUME = " VOL/" ;
        String POST_LIST = "POS/" ;
        String VOICES = "VOI/" ;
        String LANGAGES = "ALN/" ;
        //String BEHAVIOUR
        String NAO_NAME = "NAO/" ;

    //Réglage de comportement
    String BEHAVIOR = "BHV/" ;

    // Faire bouger le robot
    String MOVE = "MOV/";

    // Faire marcher le robot
    String WALK = "WLK/" ;
        String TURN_LEFT = "WTL/";
        String TURN_RIGHT = "WTR/";
        String TOWARD = "WTW/";
        String BACKWARD = "WBW/";
    String STOP_WALK = "STW/";

}