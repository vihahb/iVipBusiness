package com.xtel.ivipbusiness.view.activity.inf;

import android.app.Activity;

import com.xtel.ivipbusiness.model.entity.RESP_Short_Profile;
import com.xtel.nipservice.model.entity.Error;

/**
 * Created by Lê Công Long Vũ on 12/27/2016
 */

public interface IHomeView {

    void onGetShortUserDataSuccess(RESP_Short_Profile obj);
    void onGetShortUserDataError(Error error);
    Activity getActivity();
}
