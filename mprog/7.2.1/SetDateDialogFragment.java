package com.mprog.anvandanotifikationer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SetDateDialogFragment extends DialogFragment {

    // Interface for the dialog listener.
    public interface SetDialogListener {
        void onYesButtonClicked();
        void onNoButtonClicked();
    }

    private SetDialogListener listener;

    // Creates the set a date dialog.
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setMessage(R.string.set_date_message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onYesButtonClicked();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onNoButtonClicked();
                    }
                })
                .create();
    }

    // Attaches the context activity as a listener to the dialog.
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (SetDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + " has not implemented SetDialogListener");
        }
    }
}


