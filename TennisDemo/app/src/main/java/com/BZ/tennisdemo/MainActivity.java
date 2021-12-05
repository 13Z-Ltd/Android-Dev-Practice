package com.BZ.tennisdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainActivity extends AppCompatActivity {

    private SurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        //getSupportActionBar().hide();

        surfaceView = (SurfaceView)findViewById(R.id.game);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback((SurfaceHolder.Callback) surfaceView);
    }
}