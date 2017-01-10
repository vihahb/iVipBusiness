package com.xtel.ivipbusiness.presenter;

import com.google.firebase.iid.FirebaseInstanceId;
import com.xtel.ivipbusiness.view.activity.inf.IHomeView;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.SharedPreferencesUtils;

/**
 * Created by Lê Công Long Vũ on 12/29/2016
 */

public class HomePresenter {

    private IHomeView iHome;

    public HomePresenter(IHomeView iHome) {
        this.iHome = iHome;

        checkFCM();
    }

    private void checkFCM() {
        if (SharedPreferencesUtils.getInstance().getStringValue(Constants.FCM_TOKEN) == null)
            FirebaseInstanceId.getInstance().getToken();
    }
}
