package com.mprog.sandochtaemotepost;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

// App that lets you send an email with attachments.
public class EmailActivity extends AppCompatActivity {

    private static final int REQUEST_FILE = 1;

    private EditText recipientEditText, subjectEditText, messageEditText;
    private TextView attachmentTextView;
    private ImageButton sendButton, attachmentButton;

    private ArrayList<Uri> attachments = new ArrayList<>(); // List to hold the uris to the attached files.


    // Sets up the edit texts, text views and the buttons.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        recipientEditText = findViewById(R.id.recipient);

        sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            // Creates and starts the intent to send the email. With recipient, subject, message and attachments.
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                intent.setType("message/rfc822");

                String[] recipients = recipientEditText.getText().toString().split(";");
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT, subjectEditText.getText().toString());
                intent.putExtra(Intent.EXTRA_TEXT, messageEditText.getText().toString());

                if (!attachments.isEmpty()) {
                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, attachments);
                }

                startActivity(intent);
            }
        });

        subjectEditText = findViewById(R.id.subject);
        messageEditText = findViewById(R.id.message);
        attachmentTextView = findViewById(R.id.attached_files);
        attachmentButton = findViewById(R.id.attachment_button);
        attachmentButton.setOnClickListener(new View.OnClickListener() {
            // Creates the intent to pick a file to attach.
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                startActivityForResult(intent, REQUEST_FILE);
            }
        });

    }

    // Handles the result from picking a file to attach in the email.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_FILE) {
                Uri uri = data.getData();
                setAttachmentText(uri);
                attachments.add(uri);
            }
        }
    }

    // Sets the the text to display the names of the attached files.
    private void setAttachmentText(Uri uri) {
        if (attachments.isEmpty()) {
            attachmentTextView.setText(uri.getLastPathSegment());
        } else {
            attachmentTextView.setText(attachmentTextView.getText().toString() + "; " + uri.getLastPathSegment());
        }
    }
}
