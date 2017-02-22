package com.xtel.ivipbusiness.view.fragment.inf;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;

import com.xtel.ivipbusiness.model.entity.RESP_Store;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.sdk.callback.DialogListener;

import java.io.File;

/**
 * Created by Vulcl on 1/21/2017
 */

public interface IStoreInfoView {

    void onGetDataError();
    void onGetStoreInfoSuccess(RESP_Store resp_store);
    void onUpdateStoreInfoSuccess();
    void onGetStoreInfoError();
    void getNewSession(ICmd iCmd);

    void onTakePictureGallary(int type, Uri uri);
    void onTakePictureCamera(int type, Bitmap bitmap);
    void onLoadPicture(File file, int type);
    void onValidateError(String error);
    void startActivityForResult(Intent intent, int requestCode);
    void showProgressBar(boolean isTouchOutside, boolean isCancel, String title, String message);
    void closeProgressBar();
    Activity getActivity();
    Fragment getFragment();
}