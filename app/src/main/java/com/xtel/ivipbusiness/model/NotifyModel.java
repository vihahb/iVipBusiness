package com.xtel.ivipbusiness.model;

import android.util.Log;

import com.xtel.nipservicesdk.LoginManager;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.BasicModel;

/**
 * Created by Vulcl on 2/23/2017
 */

public class NotifyModel extends BasicModel {
    private static NotifyModel instance;

    public static NotifyModel getInstance() {
        if (instance == null)
            instance = new NotifyModel();
        return instance;
    }

    public void getListNotify(int id, ResponseHandle responseHandle) {
        String url = API_BASE + GET_LIST_NOTIFY + id;
        String session = LoginManager.getCurrentSession();

        Log.e("getListFcm", url + "           " + session);
        requestServer.getApi(url, session, responseHandle);
    }

    public void sendToPeople(String jsonObject, ResponseHandle responseHandle) {
        String url = API_BASE + SEND_NOTIFY;
        String session = LoginManager.getCurrentSession();

        Log.e("sendToPeople", url + "           " + session);
        Log.e("sendToPeople", jsonObject);
        requestServer.postApi(url, jsonObject, session, responseHandle);
    }
}