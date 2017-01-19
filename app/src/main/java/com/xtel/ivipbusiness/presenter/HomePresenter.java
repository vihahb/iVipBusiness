package com.xtel.ivipbusiness.presenter;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.xtel.ivipbusiness.model.UserModel;
import com.xtel.ivipbusiness.model.entity.RESP_Short_Profile;
import com.xtel.ivipbusiness.view.activity.inf.IHomeView;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Basic;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.SharedPreferencesUtils;

/**
 * Created by Lê Công Long Vũ on 12/29/2016
 */

public class HomePresenter {
    private IHomeView view;

    public HomePresenter(IHomeView view) {
        this.view = view;

        checkFCM();
    }

    private void checkFCM() {
        if (SharedPreferencesUtils.getInstance().getStringValue(Constants.FCM_TOKEN) == null) {
            Log.e("co_roi", "ok");
            FirebaseInstanceId.getInstance().getToken();
        }
    }

    public void getShortUserData() {
        UserModel.getIntances().getShortUserInfo(new ResponseHandle<RESP_Short_Profile>(RESP_Short_Profile.class) {
            @Override
            public void onSuccess(RESP_Short_Profile obj) {
                view.onGetShortUserDataSuccess(obj);
            }

            @Override
            public void onError(Error error) {
                view.onGetShortUserDataError(error);
            }
        });
    }
}