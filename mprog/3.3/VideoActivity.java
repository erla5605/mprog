package com.mprog.spelainochvisafilm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

// App to start the camera and record a video. Then play the video in the app.
public class VideoActivity extends AppCompatActivity {

    // Starts the VideoFragment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.video_container);

        if(fragment == null){
            fragment = new VideoFragment();
            fm.beginTransaction().add(R.id.video_container, fragment).commit();
        }
    }
}
