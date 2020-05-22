package com.mprog.anvandacopyandpaste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

// App that allows the user to enter text and then copy and paste it.
public class CopyPasteActivity extends AppCompatActivity {

    private EditText copyEditText;
    private TextView pasteTextView;
    private Button copyButton, pasteButton;

    private ClipboardManager clipboardManager;

    // OnCreate, sets up the edit text, text view and buttons as well as the clip board manager.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_paste);

        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        copyEditText = findViewById(R.id.copy_edit_text);
        pasteTextView = findViewById(R.id.paste_text_view);

        copyButton = findViewById(R.id.copy_button);
        copyButton.setOnClickListener(new View.OnClickListener() {
            // Copies the text in the edit text field.
            @Override
            public void onClick(View v) {
                ClipData clip = ClipData.newPlainText("simple text",copyEditText.getText().toString());
                clipboardManager.setPrimaryClip(clip);
                enablePasteButton();
            }
        });

        pasteButton = findViewById(R.id.paste_button);
        pasteButton.setOnClickListener(new View.OnClickListener() {
            // Pastes the text in the clip board manager to the text view.
            @Override
            public void onClick(View v) {
                ClipData.Item pasteItem = clipboardManager.getPrimaryClip().getItemAt(0);
                if(pasteItem != null){
                    pasteTextView.setText(pasteItem.getText());
                }
            }
        });
        enablePasteButton();
    }

    // Enables the paste button.
    private void enablePasteButton() {
        pasteButton.setEnabled(clipboardManager.hasPrimaryClip() &&
                clipboardManager.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN));
    }
}
