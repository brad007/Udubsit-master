package com.ionicframework.udubsit252887.ui.dialogs;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.ionicframework.udubsit252887.R;
import com.ionicframework.udubsit252887.Utils.Constants;
import com.ionicframework.udubsit252887.Utils.Utils;
import com.ionicframework.udubsit252887.models.Event;

import java.util.ArrayList;

import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by CoLab on 2016/05/26.
 */
@SuppressLint("ValidFragment")
public class RSVPDialog extends DialogFragment implements
        CompoundButton.OnCheckedChangeListener,
        AdapterView.OnItemSelectedListener {
    long total;
    private View rootView;
    private boolean going;
    private long num;
    private String eventID;
    private String groupID;
    private boolean update;
    private long newNum;
    private MaterialSpinner materialSpinner;


    public RSVPDialog(long num, String eventID, String groupID, boolean update) {
        this.num = num;
        this.eventID = eventID;
        this.groupID = groupID;
        this.update = update;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        rootView = getActivity().getLayoutInflater().inflate(R.layout.dialog_rsvp, null);
        builder.setView(rootView);

        Switch goingSwitch = (Switch) rootView.findViewById(R.id.going_switch);
        goingSwitch.setChecked(update);

        goingSwitch.setOnCheckedChangeListener(this);

        String[] numOfGuestArray = new String[100];
        numOfGuestArray[0] = "Just me";
        numOfGuestArray[1] = "1 guest";
        for (int i = 2; i < 100; i++) {
            numOfGuestArray[i] = i + " guests";
        }

        materialSpinner = (MaterialSpinner) rootView.findViewById(R.id.numGuestSpinner);

        ArrayAdapter<String> guestAdapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, numOfGuestArray
        );

        guestAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        materialSpinner.setAdapter(guestAdapter);
        materialSpinner.setOnItemSelectedListener(this);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //todo:set event attending
                //todo:increment attending counter
                if (going) {
                    //todo:going
                    attending();
                } else {
                    notAttending();
                }
            }
        });

        return builder.create();
    }

    private void notAttending() {
        //todo: remove attending node
        //todo: decrement member attending counter

        DatabaseReference newNumRef = FirebaseDatabase.getInstance().getReference(Constants.EVENT_ATTENDING_KEY)
                .child(eventID)
                .child(Utils.getUserEmail().replace(".", ","));
        newNumRef.setValue(null);

        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference(Constants.EVENTS_KEY)
                .child(groupID)
                .child(eventID)
                .child("numOfPeople");
        eventRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                long numOfUsers = (long) mutableData.getValue();
                numOfUsers -= num;
                mutableData.setValue(numOfUsers);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });

    }

    private void attending() {
        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference(Constants.EVENTS_KEY)
                .child(groupID)
                .child(eventID)
                .child("numOfPeople");
        eventRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                long numOfUsers = (long) mutableData.getValue();
                Log.v("Attending", "Total: " + total + " numOfUsers: " + numOfUsers);
                numOfUsers += (total + 1);
                mutableData.setValue(numOfUsers);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });

        //

        DatabaseReference newNumRef = FirebaseDatabase.getInstance().getReference(Constants.EVENT_ATTENDING_KEY)
                .child(eventID)
                .child(Utils.getUserEmail().replace(".", ","));
        newNumRef.setValue(newNum);

        FirebaseDatabase.getInstance().getReference(Constants.EVENTS_KEY)
                .child(groupID)
                .child(eventID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Event event = dataSnapshot.getValue(Event.class);
                        DatabaseReference myEventsRef = FirebaseDatabase.getInstance().getReference(Constants.MY_EVENTS_KEY)
                                .child(Utils.getUserEmail().replace(".", ","))
                                .child(eventID);
                        myEventsRef.setValue(event);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        DatabaseReference rsvpRef = FirebaseDatabase.getInstance().getReference(Constants.RSVP_LIST_KEY)
                .child(eventID);
        rsvpRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //todo:change this to transaction
                ArrayList arrayList = (ArrayList) dataSnapshot.getValue();
                if (arrayList == null) {
                    arrayList = new ArrayList();
                }
                arrayList.add(Utils.getUserEmail());
                FirebaseDatabase.getInstance().getReference(Constants.RSVP_LIST_KEY)
                        .child(eventID)
                        .setValue(arrayList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        going = isChecked;
        materialSpinner.setEnabled(going);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.v("Attending", "Position: " + position);
        newNum = position + 1;
        total = position - num;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
