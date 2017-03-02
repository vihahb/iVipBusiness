package com.xtel.ivipbusiness.presenter;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.xtel.ivipbusiness.model.UserModel;
import com.xtel.ivipbusiness.model.entity.RESP_Full_Profile;
import com.xtel.ivipbusiness.model.entity.RESP_Short_Profile;
import com.xtel.ivipbusiness.view.activity.inf.IHomeView;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.SharedPreferencesUtils;

/**
 * Created by Lê Công Long Vũ on 12/29/2016
 */

public class HomePresenter {
    private IHomeView view;

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(Object... params) {
            Log.e("HomePresenter", "getFulllUserInfo");
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
                        view.onGetShortUserDataError(error);
                }
            });
        }
    };

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

    public void getFullUserData() {
        RESP_Full_Profile resp_full_profile = UserModel.getInstance().getFulllUserInfo();
        view.onGetShortUserDataSuccess(resp_full_profile);

        iCmd.execute();
    }
}