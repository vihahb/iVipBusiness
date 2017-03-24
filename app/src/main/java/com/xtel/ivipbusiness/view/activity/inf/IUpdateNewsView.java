package com.xtel.ivipbusiness.view.activity.inf;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.xtel.ivipbusiness.model.entity.RESP_News;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;

import java.io.File;

/**
 * Created by Vulcl on 2/21/2017
 */

public interface IUpdateNewsView {

    void onGetDataError();
    void onGetNewsInfoSuccess(RESP_News obj);
    void onTakePictureGallary(int type, Uri uri);
    void onTakePictureCamera(int type, Bitmap bitmap);
    void onLoadPicture(File url);
    void onUpdateSuccess();
    void getNewSession(ICmd iCmd, Object... params);
    void onRequestError(Error error);

    void showShortToast(int type, String message);
    void showProgressBar(boolean isTouchOutside, boolean isCancel, String title, String message);
    void closeProgressBar();

    void startActivityForResult(Intent intent, int requestCode);
    Activity getActivity();
}
