package com.mprog.anvandanotifikationer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

// App that lets a user pick and set a date.
public class NotificationActivity extends AppCompatActivity implements SetDateDialogFragment.SetDialogListener {

    private static final String SET_DIALOG = "set_dialog";
    private static final String CHANNEL_ID = "com.mprog.anvandanotifikationer";
    private static final String DATE_PATTERN = "YYYY-MM-dd";
    private static final String DATE_KEY = "date";

    private Button pickDateButton, setDateButton;

    private Date date;

    // OnCreate, sets up the buttons
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        if(savedInstanceState != null){
            date = (Date) savedInstanceState.getSerializable(DATE_KEY);
        }

        createNotificationChannel();

        pickDateButton = findViewById(R.id.pick_date_button);
        pickDateButton.setOnClickListener(new View.OnClickListener() {
            // Starts a DatePickerDialog to pick a date.
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
                new DatePickerDialog(NotificationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Toast.makeText(NotificationActivity.this, "Date picked", Toast.LENGTH_SHORT).show();
                        Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
                        date = calendar.getTime();
                        setPickDateButtonText();
                    }
                }, year, month, day).show();

                setDateButton.setEnabled(true);
            }
        });
        setPickDateButtonText();

        setDateButton = findViewById(R.id.set_date_button);
        setDateButton.setOnClickListener(new View.OnClickListener() {
            // Starts the SetDateDialog
            @Override
            public void onClick(View v) {
                new SetDateDialogFragment().show(getSupportFragmentManager(),SET_DIALOG);
            }
        });
        setDateButton.setEnabled(date != null);
    }

    // Sets the text for the PickDateButton
    private void setPickDateButtonText() {
        pickDateButton.setText(date != null ? new SimpleDateFormat(DATE_PATTERN).format(date) : getString(R.string.pick_date));
    }

    // Creates the notification channel for the notification.
    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = getString(R.string.channel_name);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
    }


    // Creates a notification that the user has set a date.
    private void createNotification() {
        String dateString = new SimpleDateFormat(DATE_PATTERN).format(date);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_date_set)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text) +" "+  dateString)
                .setVibrate(new long[] {1000, 1000})
                .setAutoCancel(true)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, notification);
    }

    // Saves the date.
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(DATE_KEY, date);
    }

    // Listener method for the set date dialog, creates a toast and a notification that the date has been set.
    @Override
    public void onYesButtonClicked() {
        Toast.makeText(this, getString(R.string.date_set_toast), Toast.LENGTH_SHORT ).show();

        createNotification();
    }

    // Listener method for the set date dialog, creates a toast that the date has not been set.
    @Override
    public void onNoButtonClicked() {
        Toast.makeText(this, R.string.date_not_set_toast, Toast.LENGTH_SHORT ).show();
    }
}
