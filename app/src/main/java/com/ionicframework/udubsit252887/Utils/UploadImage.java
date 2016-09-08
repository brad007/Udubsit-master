package com.ionicframework.udubsit252887.Utils;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ionicframework.udubsit252887.models.Ads;
import com.ionicframework.udubsit252887.models.Event;
import com.ionicframework.udubsit252887.models.EventImageUrl;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

/**
 * Created by CoLab on 2016/05/23.
 */
public class UploadImage extends AsyncTask<Bitmap, Void, Void> {
    private DatabaseReference dRef;
    private StorageReference sRef;
    private Bitmap thumbnailBitmap;
    private Bitmap originalBitmap;
    private Object o;

    /**
     * @param o
     */
    public UploadImage(Object o) {
        this.o = o;
        if (o instanceof EventImageUrl) {
            EventImageUrl imageUrlTemp = (EventImageUrl) o;
            dRef = FirebaseDatabase.getInstance().getReference(Constants.EVENT_IMAGES)
                    .child(imageUrlTemp.getEventID()).child(imageUrlTemp.getImageID());
            sRef = FirebaseStorage.getInstance().getReference(Constants.EVENT_IMAGES)
                    .child(imageUrlTemp.getEventID()).child(imageUrlTemp.getImageID());
        } else if (o instanceof Event) {
            Event eventTemp = (Event) o;
            dRef = FirebaseDatabase.getInstance().getReference(Constants.EVENTS_KEY)
                    .child(eventTemp.getGroupId()).child(eventTemp.getEventId());
            sRef = FirebaseStorage.getInstance().getReference(Constants.EVENTS_KEY)
                    .child(eventTemp.getGroupId()).child(eventTemp.getEventId());
        } else if (o instanceof Ads) {
            Log.v("BuildingAd", "step 1");
            Ads adsTemp = (Ads) o;
            dRef = FirebaseDatabase.getInstance().getReference(Constants.ADS_KEY)
                    .child(adsTemp.getAdvertGroupId()).child(adsTemp.getAdvertId());
            sRef = FirebaseStorage.getInstance().getReference(Constants.ADS_KEY)
                    .child(adsTemp.getAdvertGroupId()).child(adsTemp.getAdvertId());
        }
    }

    @Override
    protected Void doInBackground(Bitmap... bitmaps) {
        originalBitmap = bitmaps[0];

        //Creating original and thumbnail bitmaps
        thumbnailBitmap = ThumbnailUtils.extractThumbnail(originalBitmap, (originalBitmap.getWidth() * 2) / 3, (originalBitmap.getHeight() * 2) / 3);

        StorageReference thumbnailRef = sRef.child(Constants.THUMBNAIL).child(UUID.randomUUID().toString() + ".png");
        ByteArrayOutputStream streamThumbnail = new ByteArrayOutputStream();
        thumbnailBitmap.compress(Bitmap.CompressFormat.PNG, 100, streamThumbnail);

        thumbnailRef.putBytes(streamThumbnail.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String urlThumbnail = taskSnapshot.getDownloadUrl().toString();

                if (o instanceof Event) {
                    Event event = (Event) o;
                    event.setEventThumbnailUrl(urlThumbnail);
                    dRef.setValue(event);
                } else if (o instanceof EventImageUrl) {
                    EventImageUrl imageUrl = (EventImageUrl) o;
                    imageUrl.setThumbnail(urlThumbnail);
                    dRef.setValue(imageUrl);
                } else if (o instanceof Ads) {
                    Log.v("BuildingAd", "step 2");
                    Ads ads = (Ads) o;
                    ads.setAdvertImageUrl(urlThumbnail);
                    dRef.setValue(ads);

                    FirebaseDatabase.getInstance().getReference(Constants.MY_ADS_KEY)
                            .child(Utils.getUserEmail())
                            .child(ads.getAdvertId())
                            .setValue(ads);
                }
            }
        });

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (!(o instanceof Ads)) {
            StorageReference originalRef = sRef.child(Constants.ORIGINAL).child(UUID.randomUUID().toString() + ".png");
            ByteArrayOutputStream streamTOriginal = new ByteArrayOutputStream();
            originalBitmap.compress(Bitmap.CompressFormat.PNG, 100, streamTOriginal);

            originalRef.putBytes(streamTOriginal.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String urlOriginal = taskSnapshot.getDownloadUrl().toString();
                    if (o instanceof Event) {
                        Event event = (Event) o;
                        event.setEventImageUrl(urlOriginal);
                        dRef.setValue(event);
                    } else if (o instanceof EventImageUrl) {
                        EventImageUrl imageUrl = (EventImageUrl) o;
                        imageUrl.setOriginal(urlOriginal);
                        dRef.setValue(imageUrl);
                    }
                }
            });
        }
    }
}
