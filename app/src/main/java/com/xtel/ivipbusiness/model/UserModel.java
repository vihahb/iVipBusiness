package com.xtel.ivipbusiness.model;

import android.util.Log;

import com.xtel.ivipbusiness.model.entity.RESP_Full_Profile;
import com.xtel.ivipbusiness.model.entity.RESP_Short_Profile;
import com.xtel.nipservicesdk.LoginManager;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.BasicModel;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Basic;
import com.xtel.nipservicesdk.utils.JsonHelper;
import com.xtel.sdk.utils.SharedPreferencesUtils;

/**
 * Created by Mr. M.2 on 1/12/2017
 */

public class UserModel extends BasicModel {
    private static UserModel instance;

    public static UserModel getInstance() {
        if (instance == null)
            instance = new UserModel();
        return instance;
    }

    public void getShortUserInfo(ResponseHandle responseHandle) {
        String url = API_BASE + GET_SHORT_INFO_USER;
        String session = LoginManager.getCurrentSession();

        Log.e("getShortUserInfo", "url " + url + "      " + session);
        requestServer.getApi(url, session, responseHandle);
    }

    public void getFulllUserInfo(ResponseHandle responseHandle) {
        String url = API_BASE + GET_FULL_INFO_USER;
        String session = LoginManager.getCurrentSession();

        Log.e("getFulllUserInfo", "url " + url + "      " + session);
        requestServer.getApi(url, session, responseHandle);
    }

    public void updateUserInfo(RESP_Full_Profile resp_full_profile, ResponseHandle responseHandle) {
        String url = API_BASE + UPDATE_USER;
        String session = LoginManager.getCurrentSession();

        Log.e("updateUserInfo", "url " + url + "      " + session);
        requestServer.putApi(url, JsonHelper.toJson(resp_full_profile), session, responseHandle);
    }

    public void saveFullUserInfo(RESP_Full_Profile resp_full_profile) {
        SharedPreferencesUtils.getInstance().putStringValue(USER_FULLNAME, resp_full_profile.getFullname());
        SharedPreferencesUtils.getInstance().putIntValue(USER_GENDER, resp_full_profile.getGender());
        SharedPreferencesUtils.getInstance().putLongValue(USER_BIRTHDAY, resp_full_profile.getBirthday());
        SharedPreferencesUtils.getInstance().putStringValue(USER_EMAIL, resp_full_profile.getEmail());
        SharedPreferencesUtils.getInstance().putStringValue(USER_PHONENUMBER, resp_full_profile.getPhonenumber());
        SharedPreferencesUtils.getInstance().putStringValue(USER_ADDRESS, resp_full_profile.getAddress());
        SharedPreferencesUtils.getInstance().putStringValue(USER_AVATAR, resp_full_profile.getAvatar());
        SharedPreferencesUtils.getInstance().putStringValue(USER_QR_CODE, resp_full_profile.getQr_code());
        SharedPreferencesUtils.getInstance().putStringValue(USER_BAR_CODE, resp_full_profile.getBar_code());
        SharedPreferencesUtils.getInstance().putStringValue(USER_STATUS, resp_full_profile.getStatus());
        SharedPreferencesUtils.getInstance().putIntValue(USER_STORE_NUMBER, resp_full_profile.getStore_number());
        SharedPreferencesUtils.getInstance().putLongValue(USER_JOIN_DATE, resp_full_profile.getJoin_date());
    }

    public RESP_Full_Profile getFulllUserInfo() {
        RESP_Full_Profile resp_full_profile = new RESP_Full_Profile();

        resp_full_profile.setFullname(SharedPreferencesUtils.getInstance().getStringValue(USER_FULLNAME));
        resp_full_profile.setGender(SharedPreferencesUtils.getInstance().getIntValue(USER_GENDER));
        resp_full_profile.setBirthday(SharedPreferencesUtils.getInstance().getLongValue(USER_BIRTHDAY));
        resp_full_profile.setEmail(SharedPreferencesUtils.getInstance().getStringValue(USER_EMAIL));
        resp_full_profile.setPhonenumber(SharedPreferencesUtils.getInstance().getStringValue(USER_PHONENUMBER));
        resp_full_profile.setAddress(SharedPreferencesUtils.getInstance().getStringValue(USER_ADDRESS));
        resp_full_profile.setAvatar(SharedPreferencesUtils.getInstance().getStringValue(USER_AVATAR));
        resp_full_profile.setQr_code(SharedPreferencesUtils.getInstance().getStringValue(USER_QR_CODE));
        resp_full_profile.setBar_code(SharedPreferencesUtils.getInstance().getStringValue(USER_BAR_CODE));
        resp_full_profile.setStatus(SharedPreferencesUtils.getInstance().getStringValue(USER_STATUS));
        resp_full_profile.setStore_number(SharedPreferencesUtils.getInstance().getIntValue(USER_STORE_NUMBER));
        resp_full_profile.setJoin_date(SharedPreferencesUtils.getInstance().getLongValue(USER_JOIN_DATE));

        return resp_full_profile;
    }

    public void addNewStore() {
        int store_number = SharedPreferencesUtils.getInstance().getIntValue(USER_STORE_NUMBER);
        store_number++;
        SharedPreferencesUtils.getInstance().putIntValue(USER_STORE_NUMBER, store_number);
    }
}