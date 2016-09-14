package com.ionicframework.udubsit252887.Utils.managers;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.ionicframework.udubsit252887.Utils.Constants;
import com.ionicframework.udubsit252887.Utils.Utils;
import com.ionicframework.udubsit252887.models.Person;

/**
 * Created by brad on 2016/06/09.
 */
public class GroupManager {

    public static void increaseGroupMembers(final int n, String pushID) {
        DatabaseReference groupUrl = FirebaseDatabase.getInstance().getReference(Constants.GROUPS_KEY);
        groupUrl.child(pushID)
                .child(Constants.NUM_OF_USERS_KEY)
                .runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        long numOfUsers = (long) mutableData.getValue();
                        mutableData.setValue(numOfUsers + n);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, com.google.firebase.database.DataSnapshot dataSnapshot) {

                    }
                });
    }

    public static void removeMember(final String pushID) {
        FirebaseDatabase.getInstance().getReference(Constants.USERS_KEY)
                .child(Utils.getUserEmail().replace(".", ","))
                .child("uid")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String UID = (String) dataSnapshot.getValue();

                        FirebaseDatabase.getInstance().getReference(Constants.GROUP_MEMBERS)
                                .child(pushID)
                                .child(UID)
                                .setValue(null);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference(Constants.GROUPS_KEY)
                .child(pushID)
                .child("numOfUsers")
                .runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        long numOfUsers = (long) mutableData.getValue();
                        numOfUsers++;
                        mutableData.setValue(numOfUsers);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                    }
                });
    }

    public static void addMember(Person person, DatabaseReference databaseRef, Context context) {
        databaseRef.setValue(person);
        Toast.makeText(context, "Joined", Toast.LENGTH_SHORT).show();
    }
}
