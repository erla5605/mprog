package com.mprog.spelainochuppljud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.io.IOException;

/*  App that starts a audio recording and when the recording is stops,
    the app can playback the recorded audio to the user.*/
public class AudioActivity extends AppCompatActivity {

    // Starts AudioFragment.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.audio_container);

        if (fragment == null) {
            fragment = new AudioFragment();
            fm.beginTransaction().add(R.id.audio_container, fragment).commit();
        }
    }

}




