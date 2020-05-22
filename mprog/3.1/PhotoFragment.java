package com.mprog.taochvisakort;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.List;


public class PhotoFragment extends Fragment {

    private static final int REQUEST_PHOTO = 1;

    private String photoFileName = "photofile";
    private File photoFile;

    private ImageButton photoButton;
    private ImageView photoView;


    // Sets photoFile to the correct file.
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        photoFile = getFile();
    }

    // Creates view, photoButton and photoView.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo, container, false);

        photoButton = v.findViewById(R.id.photo_button);
        photoButton.setOnClickListener(new View.OnClickListener() {
            // Creates and starts intent for the camera to take a photo. Grants permission to write on uri of photoFile.
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    Uri uri = FileProvider.getUriForFile(getActivity(), "com.mprog.taochvisakort", photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                    List<ResolveInfo> resolveInfos = getActivity().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

                    for (ResolveInfo info : resolveInfos) {
                        getActivity().grantUriPermission(info.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    }

                    startActivityForResult(intent, REQUEST_PHOTO);
                }
            }
        });
        photoView = v.findViewById(R.id.photo_image_view);

        return v;
    }

    //Gets file, with right location.
    private File getFile() {
        File filesDir = getActivity().getFilesDir();
        return new File(filesDir, photoFileName);
    }

    /*  Gets result from the camera intent and checks if result is ok. Revokes permission on uri.
        Sets photoView to the photo taken.*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(), "com.mprog.taochvisakort", photoFile);

            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            Bitmap bitmap = createPhotoBitmap();
            photoView.setImageBitmap(bitmap);
        }
    }

    // Creates bitmap for descaled photo.
    private Bitmap createPhotoBitmap() {
        int targetWidth = photoView.getWidth();
        int targetHeight = photoView.getHeight();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        float photoWidth = options.outWidth;
        float photoHeight = options.outHeight;

        int inSampleSize = 1;
        if (photoWidth > targetWidth || photoHeight > targetHeight) {
            float widthScale = photoWidth / targetWidth;
            float heightScale = photoHeight / targetHeight;

            inSampleSize = Math.round(heightScale > widthScale ? heightScale : widthScale);
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(photoFile.getPath(), options);
    }
}
