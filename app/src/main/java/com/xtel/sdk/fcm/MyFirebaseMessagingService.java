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
import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.MessageObj;
import com.xtel.ivipbusiness.view.MyApplication;
import com.xtel.ivipbusiness.view.activity.NotifycationManagerActivity;
import com.xtel.ivipbusiness.view.activity.ProfileActivity;
import com.xtel.nipservicesdk.utils.JsonHelper;
import com.xtel.sdk.commons.Constants;

/**
 * Created by Lê Công Long Vũ on 1/4/2017
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    protected final String TAG = "MyFirebaseMsgService";
    protected final String ACTION = "action";
    protected final String CONTENT = "content";
    protected final String BODY = "body";

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
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            String body = JsonHelper.toJson(remoteMessage.getData());
            Log.e(TAG, "Message data payload: " + body);

            MessageObj messageObj = JsonHelper.getObject(body, MessageObj.class);
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

    protected void sendNotifycationUpdate(MessageObj messageObj) {
        Log.e("sendNotifycationUpdate", JsonHelper.toJson(messageObj));
        MessageObj messageObjNone = new MessageObj(null, null, 4, null);

        Intent noReceive = new Intent(this, NotifycationManagerActivity.class);
        noReceive.putExtra(Constants.MODEL, messageObjNone);
        PendingIntent pendingIntentNo = PendingIntent.getActivity(this, 41, noReceive, PendingIntent.FLAG_ONE_SHOT);

        Intent intent = new Intent(this, NotifycationManagerActivity.class);
        intent.putExtra(Constants.MODEL, messageObj);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 42, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(messageObj.getTitle())
                .setContentText(messageObj.getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .addAction(R.drawable.ic_action_done, getString(R.string.cancel), pendingIntentNo)
                .addAction(R.drawable.ic_action_done, getString(R.string.update_now), pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageObj.getBody()));

        Log.e("sendNotifycationUpdate", "last " + JsonHelper.toJson(messageObj));
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageObj.getAction(), notificationBuilder.build());
    }

    protected void sendNotifycationDisplay(MessageObj messageObj) {
        Log.e("sendNotifycationDisplay", JsonHelper.toJson(messageObj));

        Intent intent = new Intent(this, NotifycationManagerActivity.class);
        intent.putExtra(Constants.MODEL, messageObj);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 5, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(messageObj.getTitle())
                .setContentText(messageObj.getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageObj.getBody()));

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageObj.getAction() /* ID of notification */, notificationBuilder.build());
    }
}