package com.xtel.sdk.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.annotations.Expose;
import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.view.MyApplication;
import com.xtel.ivipbusiness.view.activity.ProfileActivity;
import com.xtel.nipservicesdk.utils.JsonHelper;

/**
 * Created by Lê Công Long Vũ on 1/4/2017
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    protected final String TAG = "MyFirebaseMsgService";
    protected final String GOOGLE_PLAY_URL = "https://play.google.com/store/apps/details?id=";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            String body = remoteMessage.getNotification().getBody();
            Log.e(TAG, "Message Notification Body: " + body);

            MessageObj messageObj = JsonHelper.getObject(body, MessageObj.class);

            if (messageObj != null)
                checkAction(messageObj);
        }
    }
    // [END receive_message]

    /*
    * Kiểm tra action của notifycation để hiển thị thông báo tương ứng
    * */
    protected void checkAction(MessageObj messageObj) {
        switch (messageObj.getAction()) {
            case 1:

                break;
            case 2:

                break;
            case 3:

                break;
            case 4:
                sendNotifycationUpdate(messageObj);
                break;
            case 5:
                sendNotifycationDisplay(messageObj);
                break;
            default:
                break;
        }
    }

    protected void sendNotification(MessageObj message) {
        Log.e("sendNotification", JsonHelper.toJson(message));

        Intent intent = new Intent(this, ProfileActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(MyApplication.context.getString(R.string.ivip_business))
                .setContentText(message.getContent())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    protected void sendNotifycationUpdate(MessageObj messageObj) {
        Log.e("sendNotifycationUpdate", JsonHelper.toJson(messageObj));
        int id = 0;

        Intent noReceive = new Intent();
        noReceive.setAction(null);
        PendingIntent pendingIntentNo = PendingIntent.getBroadcast(getApplicationContext(), id, noReceive, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_URL + messageObj.getContent()));
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), id /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.ivip_business))
                .setContentText(getString(R.string.ask_update_version))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .addAction(R.drawable.ic_action_done, getString(R.string.cancel), pendingIntentNo)
                .addAction(R.drawable.ic_action_done, getString(R.string.update_now), pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id /* ID of notification */, notificationBuilder.build());
    }

    protected void sendNotifycationDisplay(MessageObj messageObj) {
        Log.e("sendNotifycationUpdate", JsonHelper.toJson(messageObj));
        int id = 0;

//        Intent noReceive = new Intent();
//        noReceive.setAction(null);
//        PendingIntent pendingIntentNo = PendingIntent.getBroadcast(getApplicationContext(), id, noReceive, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_URL + message.getContent()));
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), id /* Request code */, intent, PendingIntent.FLA1G_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.ivip_business))
                .setContentText(messageObj.getContent())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageObj.getContent()));

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id /* ID of notification */, notificationBuilder.build());
    }

    public class MessageObj {
        @Expose
        private Integer action;
        @Expose
        private String content;

        public MessageObj(Integer action, String content) {
            this.action = action;
            this.content = content;
        }

        public int getAction() {
            return action;
        }

        public void setAction(int action) {
            this.action = action;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}