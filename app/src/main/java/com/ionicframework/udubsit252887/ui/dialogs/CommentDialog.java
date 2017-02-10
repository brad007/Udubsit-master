package com.ionicframework.udubsit252887.ui.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ionicframework.udubsit252887.R;
import com.ionicframework.udubsit252887.Utils.Constants;
import com.ionicframework.udubsit252887.Utils.Utils;
import com.ionicframework.udubsit252887.models.Comment;

/**
 * Created by brad on 2016/07/05.
 */
@SuppressLint("ValidFragment")
public class CommentDialog extends DialogFragment {
    private View rootView;
    private String pushId;

    public CommentDialog(String pushId) {
        this.pushId = pushId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Send new comment");

        rootView = getActivity().getLayoutInflater().inflate(R.layout.dialog_comment, null);

        Button button = (Button) rootView.findViewById(R.id.comment_send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.COMMENTS_KEY)
                        .child(pushId);
                EditText editText = (EditText) rootView.findViewById(R.id.comment_edittext);
                Comment comment = new Comment(
                        Utils.getUserEmail(),
                        editText.getText().toString(),
                        System.currentTimeMillis()
                );
                databaseReference.push().setValue(comment, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        dismiss();
                    }
                });
            }
        });
        builder.setView(rootView);
        return builder.create();
    }
}
