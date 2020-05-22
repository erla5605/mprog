package com.mprog.taemot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {

    // Check if message has been received and calls on the ReceiveActivity to display the message..
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            if (messages.length > 0) {
                String message = messages[0].getDisplayMessageBody();
                String from = messages[0].getOriginatingAddress();

                Toast.makeText(context, "Sms Recieved from: " + from + "\n \"" + message + "\"", Toast.LENGTH_SHORT).show();
                ReceiveActivity instance = ReceiveActivity.getInstance();
                if (instance != null) {
                    instance.setText(from, message);
                }
            }
        }
    }
}
