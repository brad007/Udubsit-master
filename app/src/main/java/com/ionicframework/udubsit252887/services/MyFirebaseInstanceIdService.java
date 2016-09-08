package com.ionicframework.udubsit252887.services;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by brad on 2016/06/10.
 */
public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String newToken = FirebaseInstanceId.getInstance().getToken();
        // upload the token to my server
    }
}
