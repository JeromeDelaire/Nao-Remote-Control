package com.example.jerome.naoremotecontrol.CORE.FRAGMENTS;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants;
import com.example.jerome.naoremotecontrol.CORE.LISTENERS.Volume;
import com.example.jerome.naoremotecontrol.CORE.NETWORK.Server;
import com.example.jerome.naoremotecontrol.GLOBAL.FileOperator;
import com.example.jerome.naoremotecontrol.R;

import java.util.Observable;
import java.util.Observer;

import static com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants.ANIMATION;
import static com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants.DIRECTORY_NAME;
import static com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants.NO_ANIMATION;
import static com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants.SAY;
import static com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants.SPEECH_FILE;

/**
 * Created by Jerome on 01/11/2016.
 */

public class SpeechFragment extends Fragment implements View.OnClickListener, Observer{

    private EditText speech ;
    private Button saveSpeech, saySpeech, saySavedSpeech, deleteSpeech, removeSpeech ;
    private Server server ;
    private Spinner speechesList, langages ;
    private SeekBar volumeBar ;
    private CheckBox animatedSpeech ;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.speech_fragment, container, false);
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
        animatedSpeech = (CheckBox) view.findViewById(R.id.AnimatedSpeech);
        langages = (Spinner) view.findViewById(R.id.LangageSpinner);

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
                Server.send(Constants.SETTING + Constants.VOLUME + String.valueOf(volumeBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
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

        //Si on clique sur le bouton parler 1
        if(view == saySpeech){
            Server.send(Constants.SETTING + Constants.LANGAGE + langages.getSelectedItem().toString());

            if(!animatedSpeech.isChecked())
                Server.send(SAY + NO_ANIMATION + speech.getText().toString());
            else
                Server.send(Constants.SAY + Constants.ANIMATION + speech.getText().toString());
        }

        //Si on clique sur le bouton parler 2
        if(view == saySavedSpeech){

            Server.send(Constants.SETTING + Constants.LANGAGE + langages.getSelectedItem().toString());

            if(!animatedSpeech.isChecked())
                Server.send(SAY + NO_ANIMATION + speechesList.getSelectedItem().toString());
            else
                Server.send(Constants.SAY + Constants.ANIMATION + speechesList.getSelectedItem().toString());
        }

        // Si on clique sur le bouton de sauvegarde
        if(view == saveSpeech){
            String s_speech = speech.getText().toString();
            s_speech = s_speech.replaceAll("[\r\n]+", " ");
            // Si le discours n'est pas déjà sauvegardée
            if(!FileOperator.fileContain(DIRECTORY_NAME, SPEECH_FILE , s_speech))
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

        String[] list_lang = {"French", "English"};
        ArrayAdapter<String> adapter_lang = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, list_lang);
        langages.setAdapter(adapter_lang);
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
