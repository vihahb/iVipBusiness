package com.xtel.ivipbusiness.view.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.xtel.ivipbusiness.model.entity.MessageObj;
import com.xtel.nipservicesdk.utils.JsonHelper;
import com.xtel.sdk.commons.Constants;

public class NotifycationManagerActivity extends AppCompatActivity {
    protected final String TAG = "NotifycationMNG";
    protected final String GOOGLE_PLAY_URL = "https://play.google.com/store/apps/details?id=";
    protected final String MARKET = "market://details?id=";
    protected NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        getData();
    }

    protected void getData() {
        MessageObj messageObj = null;

        try {
            messageObj = (MessageObj) getIntent().getExtras().getSerializable(Constants.MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (messageObj != null)
            checkMessage(messageObj);
        else
            Log.e(TAG, "get data: null");
    }

    protected void cancelNotifycation(int id) {
        try {
            notificationManager.cancel(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        finish();
    }

    protected void checkMessage(MessageObj messageObj) {
        Log.e("checkMessage", JsonHelper.toJson(messageObj));

        switch (messageObj.getAction()) {
            case 1:

                break;
            case 2:

                break;
            case 3:

                break;
            case 4:
                goToGooglePlay(messageObj);
                break;
            case 5:
                cancelNotifycation(messageObj.getAction());
                break;
            default:
                break;
        }
    }

    protected void goToGooglePlay(MessageObj messageObj) {
        if (messageObj.getContent() == null || messageObj.getContent().isEmpty()) {
            cancelNotifycation(messageObj.getAction());
            return;
        }

        String uri_app = GOOGLE_PLAY_URL + messageObj.getContent();
        Log.e("uri_app", uri_app);

        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET + messageObj.getContent())));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_URL + messageObj.getContent())));
        }

        cancelNotifycation(messageObj.getAction());
    }
}
