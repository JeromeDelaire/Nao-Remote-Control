package com.example.jerome.naoremotecontrol.CORE;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.jerome.naoremotecontrol.CORE.FRAGMENTS.ConnexionFragment;
import com.example.jerome.naoremotecontrol.CORE.FRAGMENTS.MoveFragment;
import com.example.jerome.naoremotecontrol.CORE.FRAGMENTS.SpeechFragment;
import com.example.jerome.naoremotecontrol.CORE.FRAGMENTS.StatusFragment;

/**
 * Created by Jerome on 01/11/2016.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    int numOfTabs ;

    //On fournit à l'adapter la liste des fragments à afficher
    public PagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs ;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ConnexionFragment tab1 = new ConnexionFragment();
                return tab1;
            case 1:
                StatusFragment tab2 = new StatusFragment();
                return tab2;
            case 2:
                SpeechFragment tab3 = new SpeechFragment();
                return tab3;
            case 3 :
                MoveFragment tab4 = new MoveFragment();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
