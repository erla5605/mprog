package com.mprog.anvandavibrationer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/*An app that allows the user to to start a vibration based on the number of repetitions,
vibration time and pause time that the user has specified.*/
public class VibrationActivity extends AppCompatActivity {

    private EditText repetitionsEditText, vibrationEditText, pauseEditText;
    private Button vibrateButton;

    private Vibrator vibrator;

    // Sets the edit texts and the button.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibration);

        repetitionsEditText = findViewById(R.id.repetitions_edit_text);
        vibrationEditText = findViewById(R.id.vibration_edit_text);
        pauseEditText = findViewById(R.id.pause_edit_text);

        vibrateButton = findViewById(R.id.vibrate_butron);
        vibrateButton.setOnClickListener(new View.OnClickListener() {
            // Handles the input form user and starts the vibration.
            @Override
            public void onClick(View v) {
                int repetitions = parseNumberString(repetitionsEditText.getText().toString());
                repetitions = (repetitions == 0) ? 1 : repetitions; // To stop vibrating indefinitely.

                long[] pattern = createPattern();

                vibrate(repetitions, pattern);
            }
        });

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    // Creates pattern for the vibration. * 1000 to adjust from seconds to milliseconds.
    private long[] createPattern() {
        long vibrationTime = (long) parseNumberString(vibrationEditText.getText().toString()) * 1000;
        long pauseTime = (long) parseNumberString(pauseEditText.getText().toString()) * 1000;

        return new long[]{vibrationTime, pauseTime};
    }

    // Starts the vibration passed on the number of repetitions and pattern provided by the user.
    private void vibrate(int repetitions, long[] pattern) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, repetitions));
        } else {
            vibrator.vibrate(pattern, repetitions);
        }
    }

    // Parses the number in the edit text to int. Returns 1 in case of non number
    private int parseNumberString(String str) {
        int i;
        try {
            i = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            i = 1;
        }
        return i;
    }
}
