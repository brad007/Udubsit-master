package com.ionicframework.udubsit252887.ui.dialogs;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ionicframework.udubsit252887.R;
import com.ionicframework.udubsit252887.Utils.Constants;
import com.ionicframework.udubsit252887.Utils.Utils;
import com.ionicframework.udubsit252887.Utils.managers.GroupManager;
import com.ionicframework.udubsit252887.models.Person;

/**
 * Created by CoLab on 2016/06/07.
 */
@SuppressLint("ValidFragment")
public class TaglineDialogs extends DialogFragment {
    private String pushID;
    private String UID;

    public TaglineDialogs(String pushID, String UID) {

        this.pushID = pushID;
        this.UID = UID;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View rootView = getActivity().getLayoutInflater().inflate(R.layout.dialog_tagline, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Why do you want to join?");
        builder.setView(rootView);
        builder.setPositiveButton("Join", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference groupUrl = FirebaseDatabase.getInstance().getReference(Constants.GROUPS_KEY);
                final DatabaseReference groupList = FirebaseDatabase.getInstance().getReference(Constants.GROUP_MEMBER_LIST_KEY);

                EditText editText = (EditText) rootView.findViewById(R.id.tagline_view);
                final String tagline = editText.getText().toString();

                //Incrementing group member number
                GroupManager.increaseGroupMembers(1, pushID);
                //Adding my email to group member list
                Utils.addToList(groupList.child(pushID), getContext());
                Person person = new Person(Utils.getUserEmail(), tagline);
                DatabaseReference groupMemberRef = FirebaseDatabase.getInstance().getReference(Constants.GROUP_MEMBERS)
                        .child(pushID)
                        .child(UID);
                //Adding my person object ot group
                GroupManager.addMember(person, groupMemberRef, getContext());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }
}
