package com.example.jerome.naoremotecontrol.CORE.FRAGMENTS;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants;
import com.example.jerome.naoremotecontrol.CORE.LISTENERS.Volume;
import com.example.jerome.naoremotecontrol.CORE.NETWORK.Server;
import com.example.jerome.naoremotecontrol.GLOBAL.FileOperator;
import com.example.jerome.naoremotecontrol.R;

import java.util.ArrayList;

import static com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants.DIRECTORY_NAME;
import static com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants.NO_ANIMATION;
import static com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants.SAY;
import static com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants.SPEECH_FILE;

/**
 * Created by Jerome on 01/11/2016.
 */

public class SpeechFragment extends Fragment implements View.OnClickListener{

    private static ArrayList<String> availableVoices = null, availableLangages = null;
    private EditText speech ;
    private Button saveSpeech, saySpeech, saySavedSpeech, deleteSpeech, removeSpeech ;
    private Server server ;
    private Spinner speechesList, langages, voicesList ;
    private SeekBar volumeBar, pitchShift;
    private CheckBox animatedSpeech ;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.speech_fragment, container, false);
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if(Server.isConnect())Server.send(Constants.GET + Constants.VOLUME);
        if (visible) {
            if(availableLangages!=null){
                ArrayAdapter<String> langagesAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, availableLangages);
                langages.setAdapter(langagesAdapter);
            }
            if(availableVoices != null){
                ArrayAdapter<String> voicesAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, availableVoices);
                voicesList.setAdapter(voicesAdapter);
            }
            if(Volume.getVolume()!=-1) {
                volumeBar.setProgress(Volume.getVolume());
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        speech = (EditText) view.findViewById(R.id.Speech);
        saySpeech = (Button) view.findViewById(R.id.SpeakButton);
        saySavedSpeech = (Button) view.findViewById(R.id.SpeakButton2);
        saveSpeech = (Button) view.findViewById(R.id.SaveSpeechButton);
        speechesList = (Spinner) view.findViewById(R.id.SpinnerSpeech);
        removeSpeech = (Button) view.findViewById(R.id.RemoveSpeech);
        volumeBar = (SeekBar) view.findViewById(R.id.VolumeSeekBar);
        pitchShift = (SeekBar) view.findViewById(R.id.PitchShiftSeekBar);
        animatedSpeech = (CheckBox) view.findViewById(R.id.AnimatedSpeech);
        langages = (Spinner) view.findViewById(R.id.LangageSpinner);
        voicesList = (Spinner) view.findViewById(R.id.VoiceSpinner);

        // Initialisation des discours sauvegardés
        initSpinner();

        // Ajout des listeners
        saySpeech.setOnClickListener(this);
        saveSpeech.setOnClickListener(this);
        saySavedSpeech.setOnClickListener(this);
        removeSpeech.setOnClickListener(this);

        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(Server.isConnect())Server.send(Constants.SETTING + Constants.VOLUME + String.valueOf(volumeBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        Volume vol = new Volume();
        vol.setListener(new Volume.ChangeListener() {
            @Override
            public void onChange() {
                volumeBar.setProgress(Volume.getVolume());
            }
        });

    }

    @Override
    public void onClick(View view) {

        float val = pitchShift.getProgress()+9 ;
        val /= 10 ;
        String shifting_value = Float.toString(val);

        //Si on clique sur le bouton parler 1
        if(view == saySpeech  && voicesList.getChildCount()!=0 &&
                langages.getChildCount()!=0 && speechesList.getChildCount()!=0){

            // Sélection de la voix
            Server.send(Constants.SETTING + Constants.SPEAKING_VOICE + voicesList.getSelectedItem());

            // Choix de la langue
            Server.send(Constants.SETTING + Constants.LANGAGE + langages.getSelectedItem().toString());

            // Réglages de la voix
            Server.send(Constants.SETTING + Constants.PITCH_SHIFTING + shifting_value);

            // Discours normal
            if(!animatedSpeech.isChecked())
                Server.send(SAY + NO_ANIMATION + speech.getText().toString());
            else
                // Discours animé
                Server.send(Constants.SAY + Constants.ANIMATION + speech.getText().toString());
        }

        //Si on clique sur le bouton parler 2
        if(view == saySavedSpeech && voicesList.getChildCount()!=0 &&
                langages.getChildCount()!=0 && speechesList.getChildCount()!=0){

            // Sélection de la voix
            Server.send(Constants.SETTING + Constants.SPEAKING_VOICE + voicesList.getSelectedItem());

            // Choix de la langue
            Server.send(Constants.SETTING + Constants.LANGAGE + langages.getSelectedItem().toString());

            // Réglages de la voix
            Server.send(Constants.SETTING + Constants.PITCH_SHIFTING + shifting_value);

            // Discours normal
            if(!animatedSpeech.isChecked())
                Server.send(SAY + NO_ANIMATION + speechesList.getSelectedItem().toString());
            //Discours animé
            else
                Server.send(Constants.SAY + Constants.ANIMATION + speechesList.getSelectedItem().toString());
        }

        // Si on clique sur le bouton de sauvegarde
        if(view == saveSpeech){
            String s_speech = speech.getText().toString();
            s_speech = s_speech.replaceAll("[\r\n]+", " ");
            // Si le discours n'est pas déjà sauvegardée && pas vide
            if(!FileOperator.fileContain(DIRECTORY_NAME, SPEECH_FILE , s_speech) && s_speech.length() != 0)
            {
                // On ajoute le discours dans le mémoire du téléphone
                if(!FileOperator.writeFile(DIRECTORY_NAME, SPEECH_FILE, s_speech))
                    Toast.makeText(view.getContext(), R.string.ErrorFile , Toast.LENGTH_SHORT).show();
                else
                {
                    Toast.makeText(view.getContext(), R.string.SpeechSaveSucces , Toast.LENGTH_SHORT).show();
                    speech.setText("");
                }

                // Ajout discours sauvegardés dans le spinner
                initSpinner();
            }else
                Toast.makeText(view.getContext(), R.string.ErrorIPExists, Toast.LENGTH_SHORT).show();
        }

        // Si on clique sur le bouton de suppresion de discours
        if(view == removeSpeech){
            if(FileOperator.removeLigne(DIRECTORY_NAME, SPEECH_FILE, speechesList.getSelectedItem().toString()))
                initSpinner();
        }
    }

    // Initialisation du spinner
    public void initSpinner(){
        String[] list = FileOperator.getLignes(DIRECTORY_NAME, SPEECH_FILE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, list);
        speechesList.setAdapter(adapter);
    }

    public static void setAvailableVoices(ArrayList<String> voices){
        availableVoices = voices ;
    }

    public static void setAvailableLangages(ArrayList<String> langages){
        availableLangages = langages ;
    }
}
