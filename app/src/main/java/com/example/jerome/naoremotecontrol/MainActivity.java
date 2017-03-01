package com.example.jerome.naoremotecontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants;
import com.example.jerome.naoremotecontrol.CORE.LISTENERS.RobotConnexion;
import com.example.jerome.naoremotecontrol.CORE.NETWORK.Server;
import com.example.jerome.naoremotecontrol.GLOBAL.FileOperator;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants.DIRECTORY_NAME;

public class MainActivity extends AppCompatActivity {

    private ImageView robotStatus, serverStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        robotStatus = (ImageView) findViewById(R.id.RobotStatus);
        serverStatus = (ImageView) findViewById(R.id.ServerStatus);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.Connexion).setIcon(R.drawable.login_tab));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.Status).setIcon(R.drawable.status_tab));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.Speech).setIcon(R.drawable.speech_tab));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.Move).setIcon(R.drawable.move_tab));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.ViewPager);
        final PagerAdapter adapter = new com.example.jerome.naoremotecontrol.CORE.PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        com.example.jerome.naoremotecontrol.CORE.LISTENERS.Server serverListen = new com.example.jerome.naoremotecontrol.CORE.LISTENERS.Server();
        serverListen.setListener(new com.example.jerome.naoremotecontrol.CORE.LISTENERS.Server.ChangeListener() {
            @Override
            public void onChange() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Button connexionStatus = (Button) findViewById(R.id.ConnexionStatusButton);
                        View serverConfig = findViewById(R.id.ServerConfigLayout);
                        ProgressBar waitConnexion = (ProgressBar) findViewById(R.id.WaitConnexionSpinner);
                        Button autmaticServerLogin = (Button) findViewById(R.id.AutomaticConnexionButton);

                        if(com.example.jerome.naoremotecontrol.CORE.LISTENERS.Server.isConnected()){


                            connexionStatus.setVisibility(View.VISIBLE);
                            serverConfig.setVisibility(View.VISIBLE);
                            waitConnexion.setVisibility(View.GONE);
                            autmaticServerLogin.setVisibility(View.VISIBLE);

                            serverStatus.setBackgroundResource(R.drawable.wifi_connected);

                        }

                        else{
                            connexionStatus.setVisibility(View.GONE);
                            serverConfig.setVisibility(View.GONE);

                            serverStatus.setBackgroundResource(R.drawable.wifi_disconnected);
                        }

                    }
                });
            }
        });

        final RobotConnexion robotListen = new RobotConnexion();
        robotListen.setListener(new RobotConnexion.ChangeListener() {
            @Override
            public void onChange() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(robotListen.isConnected())
                            robotStatus.setBackgroundResource(R.drawable.wifi_connected);
                        else
                            robotStatus.setBackgroundResource(R.drawable.wifi_disconnected);
                    }
                });
            }
        });

        // On crée un repertoire pour sauvegarder les données (s'il n'existe pas déjà)
        if(!FileOperator.createDirectory(DIRECTORY_NAME))
            Toast.makeText(this, R.string.ErrorDirectory, Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(Server.isConnect()) {
            // Pour éteindre le robot
            if (id == R.id.shutdown) {
                Server.send(Constants.SHUTDOWN);
            }
            // Pour redémarrer le robot
            if (id == R.id.reboot) {
                Server.send(Constants.REBOOT);
            }

        }
        // Affiche le menu aide
        if(id == R.id.help){

            Intent myIntent = new Intent(this.getApplicationContext(), HelpActivity.class);
            startActivityForResult(myIntent, 0);
        }


        return super.onOptionsItemSelected(item);
    }

}
