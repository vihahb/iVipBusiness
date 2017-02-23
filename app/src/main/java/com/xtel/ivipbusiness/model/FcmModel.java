package com.xtel.ivipbusiness.model;

import android.util.Log;

import com.xtel.nipservicesdk.LoginManager;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.BasicModel;

/**
 * Created by Vulcl on 2/23/2017
 */

public class FcmModel extends BasicModel {
    private static FcmModel instance;

    public static FcmModel getInstance() {
        if (instance == null)
            instance = new FcmModel();
        return instance;
    }

    public void sendToPeople(String jsonObject, ResponseHandle responseHandle) {
        String url = API_BASE + SEND_NOTIFY;
        String session = LoginManager.getCurrentSession();

        Log.e("sendToPeople", url + "           " + session);
        Log.e("sendToPeople", jsonObject);
        requestServer.postApi(url, jsonObject, session, responseHandle);
    }
}