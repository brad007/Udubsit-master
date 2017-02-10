package com.ionicframework.udubsit252887.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.ionicframework.udubsit252887.R;

/**
 * Created by Brad on 9/21/2016.
 */

public class TermsAndConditionDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.terms_and_cond_dialog_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Terms and Conditions");
        builder.setView(rootView);
        return builder.create();
    }
}
