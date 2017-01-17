package com.xtel.ivipbusiness.view.activity.inf;

import android.app.Activity;

import com.xtel.ivipbusiness.model.entity.RESP_Full_Profile;
import com.xtel.nipservicesdk.model.entity.Error;

/**
 * Created by Vulcl on 1/17/2017
 */

public interface IProfileView {

    void onGetProfileSuccess(RESP_Full_Profile obj);
    void onGetProfileError(Error error);
    Activity getActivity();
}
