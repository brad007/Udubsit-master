package com.ionicframework.udubsit252887.ui;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ionicframework.udubsit252887.R;
import com.ionicframework.udubsit252887.widgets.TouchImageView;

public class ViewImageActivity extends AppCompatActivity {

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        url = getIntent().getStringExtra("ImageUrl");
        TouchImageView imageView = (TouchImageView) findViewById(R.id.viewImage);
        Glide.with(ViewImageActivity.this)
                .load(url)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_images, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_download:
                if (isStoragePermissionGranted()) {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.setDescription("UDUBSit Image");
                    request.setTitle("UDUBSit:" + System.currentTimeMillis());

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    }

                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Udubsit.png");

                    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    manager.enqueue(request);
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {
                Log.v("ViewImageActivity", "Permission is granted");
                return true;
            } else {
                Log.v("ViewImageActivity", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 1);
                return false;
            }
        }

        //permission is automatically granted on sdk < 23 upon installation
        Log.v("ViewImageActivity", "Permission is granted");
        return true;
    }

}
