package com.ionicframework.udubsit252887.services;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by brad on 2016/06/10.
 */
public class MessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String from = remoteMessage.getFrom();
        Map<String, String> data = remoteMessage.getData();
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        //todo:handle message

        /**
         * Get sender if from firebase settings > Cloud Messaging
         *
         * FirebaseMessaging fm = FirebaseMessaging.getInstance();
         * fm.send(new RemoteMessage.Builder(SENDER_ID + "gcm.googleapis.com)
         * .setMessageId("id-123")
         * .setTtl(86400)
         * .addData("key","value")
         * .build());
         */
    }

    @Override
    public void onMessageSent(String s) {
        //todo:send notifs pojo to user
        super.onMessageSent(s);
    }

}
