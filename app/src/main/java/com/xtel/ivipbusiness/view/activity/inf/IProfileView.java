package com.xtel.ivipbusiness.view.activity.inf;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.xtel.ivipbusiness.model.entity.RESP_Full_Profile;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;

import java.io.File;

/**
 * Created by Vulcl on 1/17/2017
 */

public interface IProfileView {

    void onGetProfileSuccess(RESP_Full_Profile obj);
    void onRequestError(Error error);
    void onUpdateProfileSuccess();
    void getNewSession(ICmd iCmd, int type);

    void onTakePictureGallary(int type, Uri uri);
    void onTakePictureCamera(int type, Bitmap bitmap);

    void showProgressBar(boolean isTouchOutside, boolean isCancel, String title, String message);
    void closeProgressBar();
    void onValidateError(String error);
    void onNoInternet();
    void startActivityForResult(Intent intent, int requestCode);
    Activity getActivity();
}
