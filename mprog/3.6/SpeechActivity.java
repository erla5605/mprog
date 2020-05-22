package com.mprog.talltilltexttexttilltal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/*  App that lets the user set the text of a text view with speech
    and also reads the text with text to speech.
    Same app for both assignment 3.5 and 3.6   */
public class SpeechActivity extends AppCompatActivity {

    private static final int SPEECH_TO_TEXT_RESULT = 1;
    private static final int TEXT_TO_SPEECH_RESULT = 2;

    private static final String KEY_TEXT = "text";
    private static final String KEY_SPEECH = "button";

    private TextView speechTextTextView;

    private Button speechToTextButton, textToSpeechButton;
    private boolean textToSpeechButtonEnabled = false;

    private TextToSpeech textToSpeech;

    // Sets the buttons and the text view.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);

        speechTextTextView = findViewById(R.id.text_speech_text_view);

        if (savedInstanceState != null) {
            speechTextTextView.setText(savedInstanceState.getString(KEY_TEXT));
            textToSpeechButtonEnabled = savedInstanceState.getBoolean(KEY_SPEECH);
        }

        speechToTextButton = findViewById(R.id.speech_to_text);
        speechToTextButton.setOnClickListener(new View.OnClickListener() {
            // Start the intent for speech to text.
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech to text");

                startActivityForResult(intent, SPEECH_TO_TEXT_RESULT);

                textToSpeechButtonEnabled = true;
                textToSpeechButton.setEnabled(textToSpeechButtonEnabled);
            }
        });

        textToSpeechButton = findViewById(R.id.text_to_speech);
        textToSpeechButton.setOnClickListener(new View.OnClickListener() {
            // Start the intent for text to speech.
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
                startActivityForResult(intent, TEXT_TO_SPEECH_RESULT);
            }
        });

        textToSpeechButton.setEnabled(textToSpeechButtonEnabled);

    }

    // Handles result from the text to speech and the speech to text.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEECH_TO_TEXT_RESULT && resultCode == RESULT_OK) {
            setTextSpeechView(data);
        } else if (requestCode == TEXT_TO_SPEECH_RESULT) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                startTextToSpeech();
            } else {
                Intent intent = new Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(intent);
            }
        }

    }

    //Starts text to speech of the text in the text view.
    private void startTextToSpeech() {
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        textToSpeech.speak(speechTextTextView.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
                    } else {
                        textToSpeech.speak(speechTextTextView.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            }
        });
    }

    // Sets the text view with the text from the SpeechToText.
    private void setTextSpeechView(@Nullable Intent data) {
        ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

        speechTextTextView.setText(matches.get(0));
    }

    // Saves the text in the text view and if textToSpeechButton is enabled.
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_TEXT, speechTextTextView.getText().toString());
        outState.putBoolean(KEY_SPEECH, textToSpeechButtonEnabled);
    }
}
