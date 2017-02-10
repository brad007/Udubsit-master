package com.ionicframework.udubsit252887.ui.dialogs;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.ionicframework.udubsit252887.R;
import com.ionicframework.udubsit252887.Utils.Constants;

/**
 * Created by CoLab on 2016/06/02.
 */
public class SchedualFilterDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Filter")
                .setSingleChoiceItems(R.array.schedual_filter, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor spe = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                        switch (which) {
                            case 0:
                                spe.putString(Constants.SCHEDULE_FILTER, "location").apply();
                                break;
                            case 1:
                                spe.putString(Constants.SCHEDULE_FILTER, "room").apply();
                                break;
                            case 2:
                                spe.putString(Constants.SCHEDULE_FILTER, "contact").apply();
                                break;
                            case 3:
                                spe.putString(Constants.SCHEDULE_FILTER, "title").apply();
                                break;
                            case 4:
                                spe.putString(Constants.SCHEDULE_FILTER, "startdate").apply();
                                break;
                        }
                    }
                });

        return builder.create();
    }
}
