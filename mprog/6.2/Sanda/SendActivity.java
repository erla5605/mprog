package com.mprog.sanda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

// App that sends sms messages.
public class SendActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST = 200;

    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_PHONE_STATE
    };

    private EditText toEditText, messageEditText;
    private Button sendButton;

    // OnCreate, sets up the edit texts and the send button.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        toEditText = findViewById(R.id.to_edit_text);
        messageEditText = findViewById(R.id.message_edit_text);

        sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            // Sends the sms.
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(SendActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    sendSMS();
                } else {
                    requestPermission();
                }
            }
        });
    }

    // Sends the to the recipient with the message that has been entered.
    private void sendSMS() {
        String to = toEditText.getText().toString();
        String message = messageEditText.getText().toString();

        SmsManager manager = SmsManager.getDefault();

        ArrayList<String> messageParts = manager.divideMessage(message);
        if (messageParts.size() > 1) {
            manager.sendMultipartTextMessage(to, null, messageParts, null, null);
        } else {
            manager.sendTextMessage(to, null, message, null, null);
        }
        Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();
        toEditText.getText().clear();
        messageEditText.getText().clear();

    }

    // Request permission to send sms.
    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(PERMISSIONS, PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST);
            }
        }
    }

    // Handles the request for permission to send sms.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST && checkGrantResult(grantResults)) {
            sendSMS();
        } else {
            Toast.makeText(this, "Need permission to send SMS", Toast.LENGTH_SHORT).show();
        }
    }

    // Checks the result of the permission request.
    private boolean checkGrantResult(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
