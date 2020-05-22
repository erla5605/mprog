package com.mprog.fatagpanslutningsinformation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

// App that checks if the device has an network connection or not.
public class ConnectionActivity extends AppCompatActivity {

    private ImageView connectionImageView;
    private TextView connectionTextView;

    private ConnectivityManager connectivityManager;

    // Sets up the ImageView, TextView and ConnectivityManager.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        connectionImageView = findViewById(R.id.connection_image_view);
        connectionTextView = findViewById(R.id.connection_text_view);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        refresh();
    }

    // Checks the network connection
    private void refresh() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            setImageAndText(R.string.connected, R.drawable.ic_action_check);
        } else {
            setImageAndText(R.string.not_connected, R.drawable.ic_action_cross);
        }
    }

    // Sets the ImageViews image and TextViews text.
    private void setImageAndText(int text, int image) {
        connectionTextView.setText(text);
        connectionImageView.setImageDrawable(getResources().getDrawable(image));
    }

    // Creates the menu in the toolbar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_connection, menu);
        return true;
    }

    // Handles if user presses the refresh button in the toolbar.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            refresh();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
