package com.xtel.ivipbusiness.presenter;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.xtel.ivipbusiness.model.NotifyModel;
import com.xtel.ivipbusiness.model.UserModel;
import com.xtel.ivipbusiness.model.entity.RESP_Full_Profile;
import com.xtel.ivipbusiness.model.entity.RESP_Short_Profile;
import com.xtel.ivipbusiness.view.activity.inf.IHomeView;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Basic;
import com.xtel.nipservicesdk.model.entity.RESP_None;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.SharedPreferencesUtils;

/**
 * Created by Lê Công Long Vũ on 12/29/2016
 */

public class HomePresenter {
    private IHomeView view;

    //    Business
    protected final String IS_REGISTER_FCM = "is_register_fcm";

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(Object... params) {
            Log.e("HomePresenter", "getFulllUserInfo");
            if (params.length > 0) {
                int type = (int) params[0];
                if (type == 1) {
                    UserModel.getInstance().getFulllUserInfo(new ResponseHandle<RESP_Full_Profile>(RESP_Full_Profile.class) {
                        @Override
                        public void onSuccess(RESP_Full_Profile obj) {
                            UserModel.getInstance().saveFullUserInfo(obj);
                            view.onGetShortUserDataSuccess(obj);
                        }

                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Error error) {
                            if (error.getCode() == 2)
                                view.getNewSession(iCmd, 1);
                            else
                                view.onGetUserDataError();
                        }
                    });
                } else if (type == 2) {
                    NotifyModel.getInstance().registerFCM((String) params[1], new ResponseHandle<RESP_None>(RESP_None.class) {
                        @Override
                        public void onSuccess(RESP_None obj) {
                            Log.e("registerFCM", "ok");
                            SharedPreferencesUtils.getInstance().putBooleanValue(IS_REGISTER_FCM, true);
                        }

                        @Override
                        public void onSuccess() {
                            Log.e("registerFCM", "ok");
                            SharedPreferencesUtils.getInstance().putBooleanValue(IS_REGISTER_FCM, true);
                        }

                        @Override
                        public void onError(Error error) {
                            if (error.getCode() == 2)
                                view.getNewSession(iCmd, 1);
                            else
                                view.onRegisterFcmError();
                        }
                    });
                }
            }
        }
    };

    public HomePresenter(IHomeView view) {
        this.view = view;

        registerFCm();
    }

    public void registerFCm() {
        Log.e("token", "null k: " + SharedPreferencesUtils.getInstance().getStringValue(Constants.FCM_TOKEN));
        if (!SharedPreferencesUtils.getInstance().getBooleanValue(IS_REGISTER_FCM)) {
            iCmd.execute(2, SharedPreferencesUtils.getInstance().getStringValue(Constants.FCM_TOKEN));
        }
    }

    public void getFullUserData() {
        RESP_Full_Profile resp_full_profile = UserModel.getInstance().getFulllUserInfo();
        view.onGetShortUserDataSuccess(resp_full_profile);

        iCmd.execute(1);
    }
}