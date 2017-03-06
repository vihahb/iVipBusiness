package com.xtel.ivipbusiness.model;

import android.util.Log;

import com.google.gson.JsonObject;
import com.xtel.ivipbusiness.model.entity.History;
import com.xtel.ivipbusiness.model.entity.Member;
import com.xtel.ivipbusiness.model.entity.RESP_History;
import com.xtel.ivipbusiness.model.entity.RESP_Member;
import com.xtel.nipservicesdk.LoginManager;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.BasicModel;
import com.xtel.nipservicesdk.utils.JsonHelper;

import java.util.ArrayList;

/**
 * Created by Vulcl on 1/19/2017
 */

public class MemberModel extends BasicModel {
    private static MemberModel instance;

    public static MemberModel getInstance() {
        if (instance == null)
            instance = new MemberModel();
        return instance;
    }

    public void getMemberInfo(int store_id, String member_code, ResponseHandle responseHandle) {
        String url = API_BASE + GET_MEMBER_INFO_ID + store_id + GET_MEMBER_INFO_MEMBER_CARD + member_code;
        String session = LoginManager.getCurrentSession();

        Log.e("getMemberInfo", "url   " + url + "           session  " + session);
        requestServer.getApi(url, session, responseHandle);
    }

    public void savePointForMember(String jsonObject, ResponseHandle responseHandle) {
        String url = API_BASE + SAVE_POINT;
        String session = LoginManager.getCurrentSession();

        Log.e("savePointForMember", "url   " + url + "           session  " + session);
        Log.e("savePointForMember", jsonObject);
        requestServer.postApi(url, jsonObject, session, responseHandle);
    }

    public void createMemberCard(int id, String member_code, ResponseHandle responseHandle) {
        String url = API_BASE + CREATE_MEMBER_CARD;
        String session = LoginManager.getCurrentSession();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);
        jsonObject.addProperty("member_code", member_code);

        Log.e("getMemberCardDefault", "url   " + url + "           session  " + session);
        Log.e("getMemberCardDefault", jsonObject.toString());
        requestServer.postApi(url, jsonObject.toString(), session, responseHandle);
    }

    public void getMemberCardDefault(ResponseHandle responseHandle) {
        String url = API_BASE + SAVE_GET_MEMBER_CARD_TEMPLATE;
        String session = LoginManager.getCurrentSession();

        Log.e("getMemberCardDefault", "url   " + url + "           session  " + session);
        requestServer.getApi(url, session, responseHandle);
    }
}