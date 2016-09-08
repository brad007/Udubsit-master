package com.ionicframework.udubsit252887;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by CoLab on 2016/05/10.
 */
public class UdubsitApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }
}
