package com.mprog.spelauppyoutube_filmer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


// App that plays different national anthems in youtube when pressing a button.
public class MainActivity extends AppCompatActivity {

    private static final String pathToSwedishNationalAnthem = "https://www.youtube.com/watch?v=eaIUE4u4JWc";
    private static final String pathToFrenchNationalAnthem = "https://www.youtube.com/watch?v=4K1q9Ntcr5g";
    private static final String pathToDanishNationalAnthem = "https://www.youtube.com/watch?v=oSwV-DJivQU";
    private static final String pathToItalianNationalAnthem = "https://www.youtube.com/watch?v=04ckV9QueXc";


    private Button swedishButton;
    private Button frenchButton;
    private Button danishButton;
    private Button italianButton;

    // Sets up the different buttons.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swedishButton = findViewById(R.id.sweden_button);
        swedishButton.setOnClickListener(new View.OnClickListener() {
            // Calls the video intent with the right path to the youtube video.
            @Override
            public void onClick(View v) {
                startVideoIntent(pathToSwedishNationalAnthem);
            }
        });

        frenchButton = findViewById(R.id.second_button);
        frenchButton.setOnClickListener(new View.OnClickListener() {
            // Calls the video intent with the right path to the youtube video.
            @Override
            public void onClick(View v) {
                startVideoIntent(pathToFrenchNationalAnthem);
            }
        });

        danishButton = findViewById(R.id.third_button);
        danishButton.setOnClickListener(new View.OnClickListener() {
            // Calls the video intent with the right path to the youtube video.
            @Override
            public void onClick(View v) {
                startVideoIntent(pathToDanishNationalAnthem);
            }
        });

        italianButton = findViewById(R.id.fourth_button);
        italianButton.setOnClickListener(new View.OnClickListener() {
            // Calls the video intent with the right path to the youtube video.
            @Override
            public void onClick(View v) {
                startVideoIntent(pathToItalianNationalAnthem);
            }
        });
    }

    /*  Takes the path to the video and creates a URI from it that will start the youtube app.
        Then starts the activity with an intent with the uri.*/
    private void startVideoIntent(String path) {
        Uri uri = Uri.parse(path);

        uri = Uri.parse("vnd.youtube:" + uri.getQueryParameter("v"));

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
