package com.example.jerome.naoremotecontrol.CORE.FRAGMENTS;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants;
import com.example.jerome.naoremotecontrol.CORE.LISTENERS.Posture;
import com.example.jerome.naoremotecontrol.CORE.NETWORK.Server;
import com.example.jerome.naoremotecontrol.R;

import java.util.ArrayList;

/**
 * Created by jerome on 30/11/16.
 */

public class MoveFragment extends Fragment{

    private Spinner posturesList, behaviorList ;
    private Button takePosture, setBehavior ;
    private String[] listPos ;
    private static ArrayList<String> availableBehavior = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.move_fragment, container, false);
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            if(availableBehavior!=null){
                ArrayAdapter<String> behaviorAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, availableBehavior);
                behaviorList.setAdapter(behaviorAdapter);
            }
        }
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {

        posturesList = (Spinner) view.findViewById(R.id.PostureSpinner);
        takePosture = (Button) view.findViewById(R.id.TakePosture);
        behaviorList = (Spinner)  view.findViewById(R.id.BehaviorSpinner);
        setBehavior = (Button) view.findViewById(R.id.SetBehaviorButton);

        setBehavior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Server.send(Constants.BEHAVIOR + behaviorList.getSelectedItem());
            }
        });

        takePosture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Server.send(Constants.MOVE + posturesList.getSelectedItem());
            }
        });

        if(Posture.getPostures()!=null)
            initSpinner();
    }

    public void initSpinner(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, Posture.getPostures());
        posturesList.setAdapter(adapter);
    }

    public static void setAvailableBehavior(ArrayList<String> behavior){
        availableBehavior = behavior ;
    }

}
