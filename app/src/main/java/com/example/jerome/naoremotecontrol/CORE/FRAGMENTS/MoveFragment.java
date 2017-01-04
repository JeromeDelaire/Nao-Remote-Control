package com.example.jerome.naoremotecontrol.CORE.FRAGMENTS;

import android.content.Context;
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants;
import com.example.jerome.naoremotecontrol.CORE.NETWORK.Server;
import com.example.jerome.naoremotecontrol.R;

import java.util.ArrayList;

/**
 * Created by jerome on 30/11/16.
 */

public class MoveFragment extends Fragment implements SensorEventListener{

    private Spinner posturesList, behaviorList ;
    private Button takePosture, setBehavior, walk ;
    private String[] listPos ;
    private static ArrayList<String> availableBehavior = null, availablePostures = null;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private boolean walkPressed = false;
    private int lastX = 0, lastY= 0, lastZ = 0 ;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.move_fragment, container, false);
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            // Spinner comportements
            if(availableBehavior!=null){
                ArrayAdapter<String> behaviorAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, availableBehavior);
                behaviorList.setAdapter(behaviorAdapter);
            }
            //Spinner postures
            if(availablePostures!=null){
                ArrayAdapter<String> posturesAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, availablePostures);
                posturesList.setAdapter(posturesAdapter);
            }
        }
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {

        // Accéléromètre
        senSensorManager = (SensorManager) view.getContext().getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

        posturesList = (Spinner) view.findViewById(R.id.PostureSpinner);
        takePosture = (Button) view.findViewById(R.id.TakePosture);
        behaviorList = (Spinner)  view.findViewById(R.id.BehaviorSpinner);
        setBehavior = (Button) view.findViewById(R.id.SetBehaviorButton);
        walk = (Button) view.findViewById(R.id.WalkButton);

        walk.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        walkPressed = true;
                        return true;
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        walkPressed = false ;
                        Server.send(Constants.STOP_WALK);
                        return true;
                }
                return false;
            }
        });

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
    }

    public static void setAvailableBehavior(ArrayList<String> behavior){
        availableBehavior = behavior ;
    }

    public static void setAvailablePostures(ArrayList<String> postures){
        availablePostures = postures ;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        // Si l'acceléromètre a bougé
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER && walkPressed) {
            boolean send = false ;
            String direction = "";
            int x = (int) sensorEvent.values[0];
            int y = (int) sensorEvent.values[1];
            int z = (int) sensorEvent.values[2];
            if (x < -4 && lastX >= 0){
                lastX = x ;
                direction += Constants.TURN_LEFT ;
                send = true ;
            }
            else if(x > 4 && lastX <= 0) {
                direction += Constants.TURN_RIGHT;
                lastX = x;
                send = true ;
            }else if(x > -4 && x < -4){
                direction += "NON/" ;
                send = true;
                lastX = x;
            }
            if(y > 4 && lastY <= 0){
                direction += Constants.TOWARD ;
                lastY = y ;
                send = true ;
                direction += "NON/" ;
            }else if(y < -4 && lastY >= 0){
                direction += Constants.BACKWARD;
                lastY = y ;
                send = true ;
            }else if(y > -4 && y < -4){
                send = true;
                lastY = y ;
            }
            if(send)Server.send(Constants.WALK + direction);
            send = false;

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
