package com.example.jerome.naoremotecontrol.CORE.FRAGMENTS;

import android.content.Context;
import android.graphics.Bitmap;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants;
import com.example.jerome.naoremotecontrol.CORE.NETWORK.Server;
import com.example.jerome.naoremotecontrol.R;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by jerome on 30/11/16.
 */

public class MoveFragment extends Fragment implements OnClickListener {

    private Spinner posturesList, behaviorList ;
    private Button takePosture, setBehavior, forward, backward, left, right, stop, relax;
    private String[] listPos ;
    private static ArrayList<String> availableBehavior = null, availablePostures = null;
    public static byte[] testImage  ;

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

        posturesList = (Spinner) view.findViewById(R.id.PostureSpinner);
        takePosture = (Button) view.findViewById(R.id.TakePosture);
        behaviorList = (Spinner)  view.findViewById(R.id.BehaviorSpinner);
        setBehavior = (Button) view.findViewById(R.id.SetBehaviorButton);
        forward = (Button) view.findViewById(R.id.TowardButton);
        backward = (Button) view.findViewById(R.id.BackwardButton);
        left = (Button) view.findViewById(R.id.LeftButton);
        right = (Button) view.findViewById(R.id.RightButton);
        stop = (Button) view.findViewById(R.id.StopButton);
        relax = (Button) view.findViewById(R.id.StopMotorsButton);

        /*ImageView imageView = (ImageView) view.findViewById(R.id.ImageTest);

        Bitmap bm = Bitmap.createBitmap(320, 240, Bitmap.Config.ARGB_8888);
        bm.copyPixelsFromBuffer(ByteBuffer.wrap(testImage));

        imageView.setImageBitmap(bm);*/


        forward.setOnClickListener(this);
        backward.setOnClickListener(this);
        left.setOnClickListener(this);
        right.setOnClickListener(this);
        stop.setOnClickListener(this);
        relax.setOnClickListener(this);

        setBehavior.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Server.isConnect())Server.send(Constants.BEHAVIOR + behaviorList.getSelectedItem());
            }
        });

        takePosture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Server.isConnect())Server.send(Constants.MOVE + posturesList.getSelectedItem());
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
    public void onClick(View view) {
        if(Server.isConnect()){
            if(view == forward)Server.send(Constants.WALK + Constants.TOWARD);
            if(view == backward)Server.send(Constants.WALK + Constants.BACKWARD);
            if(view == left)Server.send(Constants.WALK + Constants.TURN_LEFT);
            if(view == right)Server.send(Constants.WALK + Constants.TURN_RIGHT);
            if(view == stop)Server.send(Constants.WALK + Constants.STOP_WALK);
            if(view == relax)Server.send(Constants.MOVE + Constants.RELAX);
        }
    }
}
