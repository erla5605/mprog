package com.mprog.taochvisakort;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;


/*  App that lets the user take a photo with the devices camera app,
    and then displays the photo that was taken.*/
public class PhotoActivity extends AppCompatActivity {

    // Starts the PhotoFragment.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.photo_container);

        if(fragment == null) {
            fragment = new PhotoFragment();
            fm.beginTransaction().add(R.id.photo_container, fragment).commit();
        }
    }
}
