package com.ionicframework.udubsit252887.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/**
 * Created by Test on 3/03/2017.
 */

public class POIErrorDialog extends DialogFragment {


    public static POIErrorDialog newInstance(String title)
    {
        POIErrorDialog dialog = new POIErrorDialog();
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Google Maps is not installed")
                .setPositiveButton("Install Google Maps", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("market://details?id=com.google.android.apps.maps"));
                        startActivity(intent);
                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //User cancelled
                    }
                });

        return builder.create();
    }
}
