package com.mprog.spelainochvisafilm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class VideoFragment extends Fragment {

    private static final String TAG = "VideoFragment";

    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final int REQUEST_VIDEO = 1;

    private Button recordVideoButton;
    private VideoView videoView;
    private Intent intent;
    private Uri videoUri;

    // Sets up the record video  button and video view.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.video_fragment, container, false);

        recordVideoButton =  v.findViewById(R.id.record_video_button);
        recordVideoButton.setOnClickListener(new View.OnClickListener() {
            // Starts an intent to record video.
            // Checks for permission, and requests it if permission is not granted.
            @Override
            public void onClick(View v) {
                intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if(intent.resolveActivity(getActivity().getPackageManager()) !=  null){
                    if(checkPermission()) {
                        startActivityForResult(intent, REQUEST_VIDEO);
                    } else {
                        requestPermissions(new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                }

            }
        });

        videoView = v.findViewById(R.id.video_view);
        if(videoUri != null){
            videoView.setVideoURI(videoUri);
        }

        return v;
    }

    // Checks the permission to use the camera.
    private boolean checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    // Handles the video that has been recorded and starts it.
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == getActivity().RESULT_OK && requestCode == REQUEST_VIDEO){
            videoUri = data.getData();
            videoView.setVideoURI(videoUri);
            MediaController mediaController = new MediaController(getContext());
            videoView.setMediaController(mediaController);
            mediaController.setAnchorView(videoView);
            videoView.start();
        }
    }

    // Handles the request for permission to the camera.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CAMERA_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startActivityForResult(intent, REQUEST_VIDEO);
        } else {
            Log.d(TAG, "PERMISSION DENIED!");
        }
    }
}
