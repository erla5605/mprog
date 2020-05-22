package com.example.sparatillfill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/*
 App that counts prime numbers and displays them to the user.
 Saves most recently found prime number in shared preferences.
*/
public class MainActivity extends AppCompatActivity {
    public static final long SMALLEST_PRIME_NR = 1;

    private PrimeNrFinder primeNrFinder;
    private TextView txtPrimeNr;
    private long primeNr;
    private CountDownTimer timer;

    // Get the primeNr from sharedPreferences creates timer.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        primeNr = getPreferences(Context.MODE_PRIVATE).getLong(getString(R.string.primeNr), SMALLEST_PRIME_NR);
        txtPrimeNr = (TextView) findViewById(R.id.primeNr);

        primeNrFinder = new PrimeNrFinder();

        // Timer to find a new primeNr every 0.5 seconds.
        timer = new CountDownTimer(20000, 500) {
            public void onTick(long millisUntilFinished) {
                primeNr = primeNrFinder.getNextPrime(primeNr);
                txtPrimeNr.setText(Long.toString(primeNr));
            }

            public void onFinish() {
                start();
            }
        }.start();
    }

    // Saves the last primeNr found to sharedPreferences.
    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
        SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
        editor.putLong(getString(R.string.primeNr), primeNr);
        editor.apply();
    }
}
