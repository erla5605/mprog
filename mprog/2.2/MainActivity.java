package com.example.sqlitedb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*
 App that counts prime numbers and saves them
 and the time they were found to an SQLite database and displays them.
*/
public class MainActivity extends AppCompatActivity {

    private PrimeNrFinder primeNrFinder;
    private TextView txtPrimeNr;
    private Button btnAllPrimeNr;
    private long primeNr;
    private PrimeNrDatabase primeNrDatabase;
    private CountDownTimer timer;

    // OnCreate, sets up the text views, buttons and other attributes.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtPrimeNr = (TextView) findViewById(R.id.primeNr);
        btnAllPrimeNr = (Button) findViewById(R.id.btnAllPrimeNr);
        btnAllPrimeNr.setOnClickListener(new View.OnClickListener() {
            // Stats the activity to display the prime numbers found.
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PrimeNrListActivity.class);
                startActivity(intent);
            }
        });

        primeNrDatabase = PrimeNrDatabase.getInstance(getApplicationContext());
        primeNrFinder = new PrimeNrFinder();
        primeNr = primeNrDatabase.getHighestPrimeNr();

        // Timer to find a new primeNr every 0.5 seconds, and inserts it in the database.
        timer = new CountDownTimer(20000, 500) {
            public void onTick(long millisUntilFinished) {
                primeNr = primeNrFinder.getNextPrime(primeNr);
                txtPrimeNr.setText(Long.toString(primeNr));
                primeNrDatabase.insert(primeNr);
            }

            public void onFinish() {
                start();
            }
        }.start();
    }

    // Stops the timer from counting prime numbers.
    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    // Starts the timer counting prime numbers.
    @Override
    protected void onResume() {
        super.onResume();
        timer.start();
    }
}
