package com.mprog.anvandagps_data;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.Date;

// App that displays the gps data to the user.
public class GPSActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST = 200;
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final String TIME_PATTERN = "YYYY-MM-dd, kk:mm:ss";

    private FusedLocationProviderClient fusedLocClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    private TextView gpsTextView;

    // OnCreate, sets up the text view, location attributes.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        gpsTextView = findViewById(R.id.gps_text_view);

        if (!checkPermission()) {
            requestPermission();
        }

        createLocationRequest();
        fusedLocClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            // Takes in a location result and set the location text.
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                setLocationText(locationResult.getLastLocation());
            };
        };
    }

    // Creates a request for the location.
    private void createLocationRequest() {
        locationRequest = LocationRequest.create()
                .setInterval(5000)
                .setFastestInterval(3000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    // Sets the location text to the text view.
    private void setLocationText(Location location) {
        StringBuilder builder = new StringBuilder();
        builder.append("Latitude:\t" + location.getLatitude()+"\n");
        builder.append("Longitude:\t" + location.getLongitude()+"\n");
        builder.append("Speed:\t" + location.getSpeed()+"\n");
        builder.append("Bearing:\t" + location.getBearing()+"\n");
        builder.append("Provider:\t" + location.getProvider()+"\n");
        builder.append("Accuracy:\t" + location.getAccuracy()+"\n");
        builder.append("Altitude:\t" + location.getAltitude()+"\n");
        String time = new SimpleDateFormat(TIME_PATTERN).format(new Date(location.getTime()));
        builder.append("Time:\t" + time +"\n");

        gpsTextView.setText(builder.toString());
    }

    // Starts the location updates when the activity resumes.
    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    // Removes the location updates when the activity pauses.
    @Override
    protected void onPause() {
        super.onPause();
        fusedLocClient.removeLocationUpdates(locationCallback);
    }

    private void startLocationUpdates() {
        fusedLocClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    // Reqeuest the permission to access location.
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(PERMISSIONS, PERMISSION_REQUEST);
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST);
        }
    }

    // Checks if the permission to access location has been granted.
    private boolean checkPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // Handles the result of the permission request.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "Permission Needed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
