package com.example.jerome.naoremotecontrol.CORE.FRAGMENTS;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants;
import com.example.jerome.naoremotecontrol.CORE.LISTENERS.Battery;
import com.example.jerome.naoremotecontrol.CORE.LISTENERS.Volume;
import com.example.jerome.naoremotecontrol.CORE.NETWORK.Server;
import com.example.jerome.naoremotecontrol.LIBS.ColorPicker.ColorPickerDialog;
import com.example.jerome.naoremotecontrol.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Jerome on 03/11/2016.
 */

public class StatusFragment extends Fragment implements View.OnClickListener {

    private Button rightEye, leftEye ;
    private static String name = "";
    private TextView nameDisplay, batteryLevel ;
    private Timer timer ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.status_fragment, container, false);
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            nameDisplay.setText(getResources().getText(R.string.Name) + " " + name);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        rightEye = (Button) view.findViewById(R.id.RightEyeButton);
        leftEye = (Button) view.findViewById(R.id.LeftEyeButton);
        nameDisplay = (TextView) view.findViewById(R.id.nameEditText);
        batteryLevel = (TextView) view.findViewById(R.id.BatteryLevelTextView) ;

        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(Server.getState()==1)Server.send(Constants.GET + Constants.BATTERY);
            }
        }, 0, 30*1000);

        rightEye.setOnClickListener(this);
        leftEye.setOnClickListener(this);

        Battery bat = new Battery();
        bat.setListener(new Battery.ChangeListener() {
            @Override
            public void onChange() {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        batteryLevel.setText(Battery.getBattery());
                    }
                });
            }
        });
    }

    private void showColorPickerDialog() {

        int initialColor = Color.WHITE;

        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(getContext(), initialColor, new ColorPickerDialog.OnColorSelectedListener() {

            @Override
            public void onColorSelected(int color) {
                showToast(color);
            }

        });
        colorPickerDialog.show();

    }

    private void showToast(int color) {
        String rgbString = "R: " + Color.red(color) + " B: " + Color.blue(color) + " G: " + Color.green(color);
        Toast.makeText(getContext(), rgbString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        int initialColor = Color.WHITE;

        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(getContext(), initialColor, new ColorPickerDialog.OnColorSelectedListener() {

            @Override
            public void onColorSelected(int color) {
                //
                String hex = String.format("0x00%02x%02x%02x", Color.red(color), Color.green(color), Color.blue(color));
                if(Server.getState()==1)Server.send(Constants.SETTING + Constants.LED + Constants.ALL_LEDS + hex);
            }

        });
        colorPickerDialog.show();
    }

    public static void setName(String nao_name){
        name = nao_name ;
    }
}
