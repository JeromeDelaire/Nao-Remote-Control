package com.example.jerome.naoremotecontrol.CORE.FRAGMENTS;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants;
import com.example.jerome.naoremotecontrol.CORE.LISTENERS.Battery;
import com.example.jerome.naoremotecontrol.CORE.NETWORK.Server;
import com.example.jerome.naoremotecontrol.LIBS.ColorPicker.ColorPickerDialog;
import com.example.jerome.naoremotecontrol.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Jerome Delaire on 03/11/2016.
 */

public class StatusFragment extends Fragment {

    private static String name = "";
    private TextView nameDisplay, batteryLevel ;
    private ImageView batteryIcon ;
    private Context context ;
    private PopupWindow changeNameWindow ;
    private LinearLayout linearLayout ;
    private View customView, clicked ;
    private Button resetAll, setColor ;
    private static ArrayList<String> availableLedGroups = null ;
    private Spinner ledList ;

    public static void setAvailableLedGroups(ArrayList<String> availableLedGroups) {
        StatusFragment.availableLedGroups = availableLedGroups;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.status_fragment, container, false);
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            if(name!="")nameDisplay.setText(getResources().getText(R.string.Name) + " " + name);
            if(availableLedGroups!=null){
                ArrayAdapter<String> ledsAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, availableLedGroups);
                ledList.setAdapter(ledsAdapter);
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {

        nameDisplay = (TextView) view.findViewById(R.id.nameEditText);
        batteryLevel = (TextView) view.findViewById(R.id.BatteryLevelTextView) ;
        batteryIcon = (ImageView) view.findViewById(R.id.BatteryIcon);
        Button changeName = (Button) view.findViewById(R.id.ChangeNameButton);
        context = getContext();
        linearLayout = (LinearLayout) view.findViewById(R.id.StatusLayout);
        Button reboot = (Button) view.findViewById(R.id.RebootButton);
        resetAll = (Button) view.findViewById(R.id.ResetAllButton);
        setColor = (Button) view.findViewById(R.id.SetColorButton);
        ledList = (Spinner) view.findViewById(R.id.LEDListSpinner);
        final Switch autonomous = (Switch) view.findViewById(R.id.AutonomousSwitch);

        autonomous.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(Server.isConnect()){
                    if(autonomous.isChecked()){
                        Server.send(Constants.SETTING + Constants.AUTONOMOUS + Constants.SOLITARY);
                    }else{
                        Server.send(Constants.SETTING + Constants.AUTONOMOUS + Constants.DISABLED);
                    }
                }

            }
        });

        resetAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Server.isConnect())Server.send(Constants.SETTING + Constants.LED + Constants.RESET);
            }
        });

        setColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ledList.getChildCount()!=0){
                    int initialColor = Color.WHITE;
                    ColorPickerDialog colorPickerDialog = new ColorPickerDialog(getContext(), initialColor, new ColorPickerDialog.OnColorSelectedListener() {

                        @Override
                        public void onColorSelected(int color) {
                            if(Server.isConnect()){
                                String hex = String.format("0x00%02x%02x%02x", Color.red(color), Color.green(color), Color.blue(color));
                                Server.send(Constants.SETTING + Constants.LED + ledList.getSelectedItem().toString() + "/" + hex);
                            }
                        }

                    });
                    colorPickerDialog.show();

                }
            }
        });

        reboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Server.send(Constants.REBOOT);
            }
        });

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(Server.isConnect())Server.send(Constants.GET + Constants.BATTERY);
            }
        }, 0, 30*1000);

        // Si on clique sur le bouton pour changer de nom on ouvre une nouvelle fenêtre
        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                customView = inflater.inflate(R.layout.change_name_layout,null);

                changeNameWindow = new PopupWindow(
                        customView,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );

                changeNameWindow.setFocusable(true);
                changeNameWindow.update();

                if(Build.VERSION.SDK_INT>=21){
                    changeNameWindow.setElevation(5.0f);
                }

                Button ok = (Button) customView.findViewById(R.id.ValidNameButton);
                Button cancel = (Button) customView.findViewById(R.id.CancelNameButton);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText name = (EditText) customView.findViewById(R.id.NewNameEditText);
                        if(name.getText().length()>0 && Server.isConnect())Server.send(Constants.SETTING + Constants.NAME + name.getText());
                        changeNameWindow.dismiss();
                        linearLayout.setEnabled(true);
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changeNameWindow.dismiss();
                        linearLayout.setEnabled(true);
                    }
                });

                changeNameWindow.showAtLocation(linearLayout, Gravity.CENTER,0,0);
            }
        });

        Battery bat = new Battery();
        bat.setListener(new Battery.ChangeListener() {
            @Override
            public void onChange() {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        String batText = Battery.getBattery() + "%" ;
                        batteryLevel.setText(batText);
                        int bat = Integer.parseInt(Battery.getBattery());
                        if(bat > 0 && bat < 20)batteryIcon.setBackgroundResource(R.drawable.battery_20);
                        else if(bat >=20 && bat < 40)batteryIcon.setBackgroundResource(R.drawable.battery_40);
                        else if (bat >=40 && bat < 60)batteryIcon.setBackgroundResource(R.drawable.battery_60);
                        else if(bat >= 60 && bat < 80)batteryIcon.setBackgroundResource(R.drawable.battery_80);
                        else if(bat >= 80 && bat <= 100)batteryIcon.setBackgroundResource(R.drawable.battery_full);
                    }
                });
            }
        });


    }

    public static void setName(String nao_name){
        name = nao_name ;
    }
}
