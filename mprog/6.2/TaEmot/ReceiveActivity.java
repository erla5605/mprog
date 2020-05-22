package com.mprog.taemot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.widget.TextView;
import android.widget.Toast;

// App that receives sms messages and displays them to the user.
public class ReceiveActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST = 200;
    private static final String FROM_KEY = "from";
    private static final String MESSAGE_KEY = "message";

    private static ReceiveActivity instance;

    private static final String[] PERMISSION = new String[] {
      Manifest.permission.RECEIVE_SMS
    };

    private TextView fromTextView, messageTextView;

    // Returns the instance of the ReceiveActivity for the receiver.
    public static ReceiveActivity getInstance(){
        return instance;
    }

    // OnCreate, sets up the text views.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

        fromTextView = findViewById(R.id.from_text_view);
        messageTextView = findViewById(R.id.message_text_view);

        if(savedInstanceState != null) {
            fromTextView.setText(savedInstanceState.getString(FROM_KEY));
            messageTextView.setText(savedInstanceState.getString(MESSAGE_KEY));
        }

        checkPermission();
        instance = this;
    }

    // Checks permissions.
    private void checkPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(PERMISSION, PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(this, PERMISSION, PERMISSION_REQUEST);
            }
        }
    }

    // Sets the text of the received message.
    public void setText(String from, String message){
        fromTextView.setText(from);
        messageTextView.setText(message);
    }


    // Saves the message and who it was from.
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(FROM_KEY, fromTextView.getText().toString());
        outState.putString(MESSAGE_KEY, messageTextView.getText().toString());
    }

    // Handles the permission request.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQUEST && grantResults[0] != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "NEED PERMISSION", Toast.LENGTH_SHORT).show();
        }
    }
}
