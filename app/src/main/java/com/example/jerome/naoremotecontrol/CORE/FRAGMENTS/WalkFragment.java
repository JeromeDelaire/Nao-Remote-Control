package com.example.jerome.naoremotecontrol.CORE.FRAGMENTS;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jerome.naoremotecontrol.R;

/**
 * Created by jerome on 04/02/17.
 */

public class WalkFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.walk_fragment, container, false);
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);

    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {

    }
}
