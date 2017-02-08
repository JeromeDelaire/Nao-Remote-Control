package com.example.jerome.naoremotecontrol;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants;
import com.example.jerome.naoremotecontrol.CORE.NETWORK.Server;
import com.example.jerome.naoremotecontrol.GLOBAL.FileOperator;

import static com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants.DIRECTORY_NAME;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.Connexion).setIcon(R.drawable.login_tab));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.Status).setIcon(R.drawable.status_tab));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.Speech).setIcon(R.drawable.speech_tab));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.Move).setIcon(R.drawable.move_tab));
        tabLayout.addTab(tabLayout.newTab().setText("Walk").setIcon(R.drawable.walk_tab));
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

       /* // Création de la liste de Fragments que fera défiler le PagerAdapter
        List<Fragment> fragments = new Vector<>();

        // Ajout des Fragments dans la liste
        fragments.add(Fragment.instantiate(this,ConnexionFragment.class.getName()));
        fragments.add(Fragment.instantiate(this,StatusFragment.class.getName()));
        fragments.add(Fragment.instantiate(this,SpeechFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, MoveFragment.class.getName()));

        // Création de l'adapter qui s'occupera de l'affichage de la liste de Fragments
        PagerAdapter mPagerAdapter = new MyPagerAdapter(super.getSupportFragmentManager(), fragments);

        ViewPager pager = (ViewPager) super.findViewById(R.id.ViewPager);
        // Affectation de l'adapter au ViewPager
        pager.setAdapter(mPagerAdapter);*/

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

        if(Server.isConnect()){
            // Pour éteindre le robot
            if(id == R.id.shutdown){
                Server.send(Constants.SHUTDOWN);
            }
            // Pour redémarrer le robot
            if(id == R.id.reboot){
                Server.send(Constants.REBOOT);
            }
            // Affiche le menu aide
            if(id == R.id.help){

            }
        }

        return super.onOptionsItemSelected(item);
    }

}
