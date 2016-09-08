package com.ionicframework.udubsit252887.dialogs;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ionicframework.udubsit252887.R;
import com.ionicframework.udubsit252887.Utils.Constants;
import com.ionicframework.udubsit252887.Utils.Utils;
import com.ionicframework.udubsit252887.models.Groups;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

/**
 * Created by CoLab on 2016/05/25.
 */
public class AddGroupDialog extends DialogFragment implements View.OnClickListener {
    private static final int PICK_IMAGE = 1;
    private View rootView;
    private EditText editText;
    private String location_url;


    public AddGroupDialog() {
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        rootView = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_group, null);
        editText = (EditText) rootView.findViewById(R.id.group_nameview);
        Button button = (Button) rootView.findViewById(R.id.get_image);
        location_url = "gs://udubsit-firebase.appspot.com/groupImages";
        button.setOnClickListener(this);
        builder.setView(rootView);
        return builder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                Uri uri = data.getData();
                String[] projections = new String[]{"_data"};
                Cursor cursor = getContext().getContentResolver().query(uri, projections, null, null, null);
                cursor.moveToFirst();
                String string = cursor.getString(cursor.getColumnIndex(projections[0]));
                cursor.close();
                Bitmap bitmap = BitmapFactory.decodeFile(string);

                UploadGroupImage uploadGroupImage = new UploadGroupImage();
                uploadGroupImage.execute(bitmap);

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_image:
                Intent intent = new Intent();
                intent.setType("images/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                break;
        }
    }

    public class UploadGroupImage extends AsyncTask<Bitmap, Void, Void> {
        @Override
        protected Void doInBackground(Bitmap... params) {
            Bitmap original = params[0];
            final Bitmap thumbnail = ThumbnailUtils.extractThumbnail(original, 256, 256);

            ByteArrayOutputStream originalStream = new ByteArrayOutputStream();
            original.compress(Bitmap.CompressFormat.PNG, 100, originalStream);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            final StorageReference photoRef = storage.getReferenceFromUrl(location_url);
            photoRef.child(Constants.ORIGINAL + "/" + UUID.randomUUID().toString() + ".png")
                    .putBytes(originalStream.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final String originalUrl = taskSnapshot.getDownloadUrl().toString();

                    ByteArrayOutputStream thumbnailStream = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.PNG, 100, thumbnailStream);

                    photoRef.child(Constants.THUMBNAIL + "/" + UUID.randomUUID().toString() + ".png")
                            .putBytes(thumbnailStream.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot2) {
                            String thumbnailUrl = taskSnapshot2.getDownloadUrl().toString();
//public Groups(String groupId, String name, String description, long numOfUsers, String imageUrl, String thumbnailUrl) {
                            String UID = Utils.getPushId();
                            Groups groups = new Groups(
                                    UID,
                                    editText.getText().toString(),
                                    null,
                                    0,
                                    originalUrl,
                                    thumbnailUrl
                            );

                            DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                    .getReference(Constants.GROUPS_KEY)
                                    .push();
                            databaseReference.setValue(groups);
                        }
                    });
                }
            });
            return null;
        }
    }

}

