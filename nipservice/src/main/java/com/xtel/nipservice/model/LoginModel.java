package com.xtel.nipservice.model;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.xtel.nipservice.callback.ResponseHandle;
import com.xtel.nipservice.commons.Cts;
import com.xtel.nipservice.model.entity.ActiveNip;
import com.xtel.nipservice.model.entity.AuthenNip;
import com.xtel.nipservice.model.entity.AuthenNipModel;
import com.xtel.nipservice.model.entity.LoginNipModel;
import com.xtel.nipservice.model.entity.RESP_Login;
import com.xtel.nipservice.model.entity.RESP_Reactive;
import com.xtel.nipservice.model.entity.RESP_Register;
import com.xtel.nipservice.model.entity.ReactiveNip;
import com.xtel.nipservice.model.entity.RegisterModel;
import com.xtel.nipservice.model.entity.ResetEntity;
import com.xtel.nipservice.utils.DeviceInfo;
import com.xtel.nipservice.utils.JsonHelper;
import com.xtel.nipservice.utils.SharedUtils;

/**
 * Created by Lê Công Long Vũ on 12/28/2016
 */

public class LoginModel extends BasicModel {
    private static LoginModel instance;
    String url_facebook = Cts.URL_NIP + Cts.API_FACEBOOK;
    String url_account_kit = Cts.URL_NIP + Cts.API_ACCOUNT_KIT;
    String url_sesion_authenticate = Cts.URL_NIP + Cts.API_SESSION_AUTHENTICATE;
    String url_reg_nip_acc = Cts.URL_NIP + Cts.API_REGISTER_NIP;
    String url_reset_password = Cts.URL_NIP + Cts.API_RESET_ACC_NIP;
    String url_login = Cts.URL_NIP + Cts.API_LOGIN_ACC_NIP;

    public static LoginModel getInstance() {
        if (instance == null)
            instance = new LoginModel();
        return instance;
    }

    public void postFacebookData2Server(String service_code, String token_key, ResponseHandle<RESP_Login> responseHandle) {
        AuthenNipModel facebookModel = new AuthenNipModel();
        facebookModel.setAccess_token_key(token_key);
        facebookModel.setService_code(service_code);
        facebookModel.setDevInfo(DeviceInfo.getDeviceObject());

        requestServer.postApi(url_facebook, JsonHelper.toJson(facebookModel), null, responseHandle);
    }

    public void postAccountKitData2Server(String service_code, String authorization_code, ResponseHandle<RESP_Login> responseHandle) {
        AuthenNipModel accountKitModel = new AuthenNipModel();
        accountKitModel.setAuthorization_code(authorization_code);
        accountKitModel.setService_code(service_code);
        accountKitModel.setDevInfo(DeviceInfo.getDeviceObject());
        requestServer.postApi(url_account_kit, JsonHelper.toJson(accountKitModel), null, responseHandle);
    }


    public void registerAccountNip(String user_name, String password, String email, boolean isPhone, String service_code, ResponseHandle<RESP_Register> responseHandle) {
        RegisterModel register = new RegisterModel();
        register.setUsername(user_name);
        register.setPassword(password);
        register.setEmail(email);
        register.setService_code(service_code);
        if (!isPhone) {
            register.setAccountType("EMAIL");
            register.setSendEmail(1);
        } else {
            register.setSendEmail(0);
            register.setAccountType("PHONE-NUMBER");
        }

        Log.e("registerAccountNip", JsonHelper.toJson(register));

        requestServer.postApi(url_reg_nip_acc, JsonHelper.toJson(register), null, responseHandle);
    }

    public void loginNipServices(String user_name, String password, String service_code, boolean isPhone, ResponseHandle<RESP_Login> responseHandle) {
        LoginNipModel loginNipModel = new LoginNipModel();
        loginNipModel.setUsername(user_name);
        loginNipModel.setPassword(password);
        loginNipModel.setService_code(service_code);
        loginNipModel.setDevInfo(DeviceInfo.getDeviceObject());
        String request = JsonHelper.toJson(loginNipModel);
        Log.e("nip_login", JsonHelper.toJson(loginNipModel));

        if (!isPhone) {
            loginNipModel.setAccountType("EMAIL");
        } else {
            loginNipModel.setAccountType("PHONE-NUMBER");
        }

        requestServer.postApi(url_login, JsonHelper.toJson(loginNipModel), null, responseHandle);
    }

    public void resetPassworf(String email, String password, String service_code, boolean isPhone, String authorization_code, ResponseHandle responseHandle) {
        ResetEntity resetEntity = new ResetEntity();

        resetEntity.setService_code(service_code);

        if (isPhone) {
//            resetEntity.setUsename(email);
            resetEntity.setSendMail(0);
            resetEntity.setAccountType("PHONE-NUMBER");
            resetEntity.setPassword(password);
            resetEntity.setAuthorization_code(authorization_code);
        } else {
            resetEntity.setEmail(email);
            resetEntity.setSendMail(1);
            resetEntity.setAccountType("EMAIL");
        }

        requestServer.putApi(url_reset_password, JsonHelper.toJson(resetEntity), null, responseHandle);
    }

    public void reactiveNipAccoint(String user_name, String service_code, boolean isPhone, ResponseHandle<RESP_Reactive> responseHandle) {
        String url_reactive = Cts.URL_NIP + Cts.API_RE_ACTIVE_ACC_NIP;

        ReactiveNip reactiveNip = new ReactiveNip();
        reactiveNip.setUsername(user_name);
        reactiveNip.setService_code(service_code);

        if (isPhone) {
            reactiveNip.setSendMail(0);
            reactiveNip.setAccountType("PHONE-NUMBER");
        } else {
            reactiveNip.setSendMail(1);
            reactiveNip.setAccountType("EMAIL");
        }

        Log.e("reactiveNipAccoint", JsonHelper.toJson(reactiveNip));
        requestServer.putApi(url_reactive, JsonHelper.toJson(reactiveNip), null, responseHandle);
    }


    public String getSession() {
        return SharedUtils.getInstance().getStringValue(Cts.USER_SESSION);
    }

    public String getCurrentAuthenticationId() {
        return SharedUtils.getInstance().getStringValue(Cts.USER_AUTH_ID);
    }

    public void getNewSession(String service_code, ResponseHandle<RESP_Login> responseHandle) {
        AuthenNip authenNip = new AuthenNip();
        authenNip.setAuthenticationid(SharedUtils.getInstance().getStringValue(Cts.USER_AUTH_ID));
        authenNip.setService_code(service_code);
        authenNip.setDevInfo(DeviceInfo.getDeviceObject());

        String url_authen = Cts.URL_NIP + Cts.API_SESSION_AUTHENTICATE;
        requestServer.postApi(url_authen, JsonHelper.toJson(authenNip), null, responseHandle);
    }

    public void activeAccount(String authorization_code, String accountType, String service_code, ResponseHandle responseHandle) {
        String url_active = Cts.URL_NIP + Cts.API_ACTIVE_ACCOUNT;
        ActiveNip activeNip = new ActiveNip();
        activeNip.setAuthorization_code(authorization_code);
        activeNip.setActivation_code(SharedUtils.getInstance().getStringValue(Cts.USER_ACTIVATION_CODE));
        activeNip.setAccountType(accountType);
        activeNip.setService_code(service_code);

        Log.e("activeAccount", JsonHelper.toJson(activeNip));
        requestServer.postApi(url_active, JsonHelper.toJson(activeNip), null, responseHandle);
    }

    public String getServiceCode(Activity activity) {
        try {
            ApplicationInfo ai = activity.getPackageManager().getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            return bundle.getString(Cts.META_DATA_NAME);
        } catch (PackageManager.NameNotFoundException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void logout() {
        SharedUtils.getInstance().clearData();
    }
}
