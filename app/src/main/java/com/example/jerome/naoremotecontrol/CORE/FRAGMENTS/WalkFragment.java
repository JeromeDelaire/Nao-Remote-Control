package com.example.jerome.naoremotecontrol.CORE.FRAGMENTS;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jerome.naoremotecontrol.CORE.NETWORK.Reception;
import com.example.jerome.naoremotecontrol.R;

import java.nio.IntBuffer;

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
        int   width  = 128 ;
        int   height = 128 ;

        // You are using RGBA that's why Config is ARGB.8888
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // vector is your int[] of ARGB
        bitmap.copyPixelsFromBuffer(IntBuffer.wrap(Reception.pixels));

        ImageView tv1;
        tv1= (ImageView) view.findViewById(R.id.image);
        tv1.setImageBitmap(bitmap);
    }
}
