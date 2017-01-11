package com.example.jerome.naoremotecontrol.CORE.FRAGMENTS;

import android.content.Context;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
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
                        walk.setBackgroundResource(R.drawable.accel_pressed);
                        return true;
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        walkPressed = false ;
                        if(Server.getState()==1)Server.send(Constants.STOP_WALK);
                        walk.setBackgroundResource(R.drawable.accel);
                        return true;
                }
                return false;
            }
        });

        setBehavior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Server.getState()==1)Server.send(Constants.BEHAVIOR + behaviorList.getSelectedItem());
            }
        });

        takePosture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Server.getState()==1)Server.send(Constants.MOVE + posturesList.getSelectedItem());
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
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER && walkPressed && Server.getState()==1)  {
            boolean send = false ;
            String direction = "";
            int x = (int) sensorEvent.values[0];
            int y = (int) sensorEvent.values[1];
            int z = (int) sensorEvent.values[2];
            if (x < -2 && lastX >= 0){
                lastX = x ;
                direction += Constants.TURN_RIGHT ;
                send = true ;
            }
            else if(x > 2 && lastX <= 0) {
                direction += Constants.TURN_LEFT;
                lastX = x;
                send = true ;
            }else if(x > -2 && x < 2 && (lastX < -2 || lastX > 2)){
                direction += "NON/" ;
                send = true;
                lastX = x;
            }
            else if(y > 2 && lastY <= 0){
                direction += Constants.BACKWARD ;
                lastY = y ;
                send = true ;
            }else if(y < -2 && lastY >= 0){
                direction += Constants.TOWARD;
                lastY = y ;
                send = true ;
            }else if(y > -2 && y < 2 && (lastY < -2 || lastY > 2)){
                send = true;
                lastY = y ;
                direction += "NON/" ;
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
