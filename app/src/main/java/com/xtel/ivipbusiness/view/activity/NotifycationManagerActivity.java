package com.xtel.ivipbusiness.view.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.MessageObj;
import com.xtel.sdk.commons.Constants;

public class NotifycationManagerActivity extends AppCompatActivity {
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
            messageObj = (MessageObj) getIntent().getSerializableExtra(Constants.MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (messageObj != null)
            checkMessage(messageObj);
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
        if (messageObj.getContent() == null) {
            cancelNotifycation(messageObj.getAction());
            return;
        }

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
//            case 5:
//
//                break;
            default:
                break;
        }
    }

    protected void goToGooglePlay(MessageObj messageObj) {
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
