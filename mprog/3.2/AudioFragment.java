package com.mprog.spelainochuppljud;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.Arrays;

public class AudioFragment extends Fragment {

    private static final String TAG = "AudioFragment";
    private static final int REQUEST_AUDIO_PERMISSION = 1000;

    private ImageButton recordButton, playButton;
    private boolean recording, playing;

    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;

    private String fileName;

    // Finds and set path for file.
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fileName = getActivity().getExternalCacheDir().getAbsolutePath();
        fileName += "/audiofragment.3gp";
    }

    // Creates view and the buttons for record and play.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.audio_fragment, container, false);

        recordButton = (ImageButton) v.findViewById(R.id.record_button);
        recordButton.setOnClickListener(new View.OnClickListener() {
            // Start the recording if permission is granted,
            // Stops the recording if the recording has started.
            @Override
            public void onClick(View v) {
                recording = !recording;
                if (recording) {
                    setDrawable(recordButton, R.drawable.ic_action_stop);
                } else {
                    setDrawable(recordButton, R.drawable.ic_action_record);
                }

                if (recording) {
                    if (checkPermission()) {
                        startRecording();
                    } else {
                        requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION);
                    }
                } else {
                    stopRecording();
                    setButtonEnabled(false, true);
                }
            }
        });


        playButton = (ImageButton) v.findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            // Starts playing the audio.
            // Stops playing the audio if the recording has started.
            @Override
            public void onClick(View v) {
                playing = !playing;
                if (playing) {
                    setDrawable(playButton, R.drawable.ic_action_stop);
                } else {
                    setDrawable(playButton, R.drawable.ic_action_play);
                }
                if (playing) {
                    startPlaying();
                } else {
                    stopPlaying();
                    setButtonEnabled(true, false);
                }
            }
        });

        setButtonEnabled(true, false);

        return v;
    }

    // Check for permission to record audio and write to external storage.
    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getActivity().checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                    && getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    // Enables or disables the buttons, sets their alpha to indicate if they are enabled or not.
    private void setButtonEnabled(boolean recordButtonEnable, boolean playButtonEnable) {
        recordButton.setEnabled(recordButtonEnable);
        recordButton.setAlpha(recordButtonEnable ? 1.0f : 0.2f);
        playButton.setEnabled(playButtonEnable);
        playButton.setAlpha(playButtonEnable ? 1.0f : 0.2f);
    }

    // Stops and releases the mediaPlayer.
    private void stopPlaying() {
        mediaPlayer.release();
        mediaPlayer = null;

        Toast.makeText(getContext(), "Play Stopped", Toast.LENGTH_SHORT).show();
    }

    // Creates the mediaPlayer and starts the recording.
    private void startPlaying() {
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(fileName);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            Log.e(TAG, "mediaPlayer.prepare() failed");
        }
        Toast.makeText(getContext(), "Play Started", Toast.LENGTH_SHORT).show();
    }

    // Stops the recording.
    private void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;

        Toast.makeText(getContext(), "Recording Stopped", Toast.LENGTH_SHORT).show();
    }

    // Creates mediaRecorder, set its settings and starts the recording.
    private void startRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(fileName);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "mediaRecorder.prepare() failed");
        }

        mediaRecorder.start();
        Toast.makeText(getContext(), "Recording started", Toast.LENGTH_SHORT).show();
    }

    // Set the image, based on the drawable id,  on the ImageButton.
    private void setDrawable(ImageButton button, int drawableId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            button.setImageDrawable(getActivity().getDrawable(drawableId));
        } else {
            button.setImageDrawable(getActivity().getResources().getDrawable(drawableId));
        }
    }

    // Handles the result of the permission request and starts recording if permission is granted.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_AUDIO_PERMISSION) {
            if (checkGrantResults(grantResults)) {
                startRecording();
            } else {
                Log.d(TAG, "PERMISSION DENIED");
            }
        }
    }

    // Checks the grantResults from the permission request.
    private boolean checkGrantResults(@NonNull int[] grantResults) {
        Log.d(TAG, Arrays.toString(grantResults));
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
