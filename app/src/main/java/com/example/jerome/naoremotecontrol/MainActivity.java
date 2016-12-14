package com.example.jerome.naoremotecontrol;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.jerome.naoremotecontrol.CORE.FRAGMENTS.ConnexionFragment;
import com.example.jerome.naoremotecontrol.CORE.FRAGMENTS.MoveFragment;
import com.example.jerome.naoremotecontrol.CORE.FRAGMENTS.SpeechFragment;
import com.example.jerome.naoremotecontrol.CORE.FRAGMENTS.StatusFragment;
import com.example.jerome.naoremotecontrol.CORE.MyPagerAdapter;
import com.example.jerome.naoremotecontrol.CORE.NETWORK.Server;
import com.example.jerome.naoremotecontrol.GLOBAL.FileOperator;

import java.util.List;
import java.util.Vector;

import static com.example.jerome.naoremotecontrol.CORE.INTERFACES.Constants.DIRECTORY_NAME;

public class MainActivity extends FragmentActivity {

        private PagerAdapter mPagerAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            super.setContentView(R.layout.activity_main);

            // Création de la liste de Fragments que fera défiler le PagerAdapter
            List fragments = new Vector();

            // Ajout des Fragments dans la liste
            fragments.add(Fragment.instantiate(this,ConnexionFragment.class.getName()));
            fragments.add(Fragment.instantiate(this,StatusFragment.class.getName()));
            fragments.add(Fragment.instantiate(this,SpeechFragment.class.getName()));
            fragments.add(Fragment.instantiate(this, MoveFragment.class.getName()));

            // Création de l'adapter qui s'occupera de l'affichage de la liste de Fragments
            this.mPagerAdapter = new MyPagerAdapter(super.getSupportFragmentManager(), fragments);

            ViewPager pager = (ViewPager) super.findViewById(R.id.ViewPager);
            // Affectation de l'adapter au ViewPager
            pager.setAdapter(this.mPagerAdapter);

            // On crée un repertoire pour sauvegarder les données (s'il n'existe pas déjà)
            if(!FileOperator.createDirectory(DIRECTORY_NAME))
                Toast.makeText(this, R.string.ErrorDirectory, Toast.LENGTH_SHORT).show();

        }
}
