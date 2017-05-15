package com.ionicframework.udubsit252887.ui.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ionicframework.udubsit252887.R;
import com.ionicframework.udubsit252887.Utils.Constants;
import com.ionicframework.udubsit252887.Utils.UploadImage;
import com.ionicframework.udubsit252887.Utils.Utils;
import com.ionicframework.udubsit252887.models.EventImageUrl;

/**
 * Created by brad on 2016/07/05.
 */
@SuppressLint("ValidFragment")
public class EventOptionsDialog extends DialogFragment {

    private static final int IMAGE_PICK = 1;
    private String groupID;
    private String eventID;

    public EventOptionsDialog(String groupID, String eventID) {
        this.groupID = groupID;
        this.eventID = eventID;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add:")
                .setSingleChoiceItems(R.array.event_options_array, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), IMAGE_PICK);
                                break;
                            case 1:
                                DialogFragment dialogFragment = new CommentDialog(eventID);
                                dialogFragment.show(getFragmentManager(), null);
                                dismiss();
                                break;
                            case 2 :
//                              Group ID of Selected group
                                DatabaseReference groupUrl = FirebaseDatabase.getInstance().getReference(Constants.GROUPS_KEY);

                                groupUrl.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

//                                //Start the gmail application
//                                final Intent emailIntent = new Intent(Intent.ACTION_SEND);
//
////                              Fill the email with content
//                                emailIntent.setType("plain/text");
//                                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"brent.vanwildemeersch@hotmail.be"});
//                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "UDUBSIT Abuse Event''");
////                              Launching Email App to send email
//                                getContext().startActivity(Intent.createChooser(emailIntent,"Send email...."));
////                                report abuse
                                break;
                            case 3 :
//                                report explicit content
                                //Start the gmail application
                                final Intent emailIntentexplicit = new Intent(Intent.ACTION_SEND);

//                              Fill the email with content
                                emailIntentexplicit.setType("plain/text");
                                emailIntentexplicit.putExtra(Intent.EXTRA_EMAIL, new String[]{});

                                emailIntentexplicit.putExtra(Intent.EXTRA_SUBJECT, "UDUBSIT Explicit content");
//                              Launching Email App to send email
                                getContext().startActivity(Intent.createChooser(emailIntentexplicit,"Send email...."));
//                                report abuse
                                break;
                        }
                    }
                });
        return builder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_PICK) {
                Uri uri = data.getData();
                String[] projections = new String[]{"_data"};
                Cursor cursor = getContext().getContentResolver().query(uri, projections, null, null, null);
                cursor.moveToFirst();
                String str = cursor.getString(cursor.getColumnIndex(projections[0]));
                cursor.close();
                ;
                Bitmap bitmap = BitmapFactory.decodeFile(str);
                EventImageUrl imageUrl = new EventImageUrl();
                imageUrl.setEventID(eventID);
                imageUrl.setImageID(Utils.getPushId());
                UploadImage uploadImage = new UploadImage(imageUrl);
                uploadImage.execute(bitmap);
                dismiss();
            }
        }
    }
}
