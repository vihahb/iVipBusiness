package com.xtel.ivipbusiness.view.fragment.inf;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;

import com.xtel.ivipbusiness.model.entity.RESP_Store;

/**
 * Created by Vulcl on 1/21/2017
 */

public interface IStoreInfoView {

    void onGetDataError();
    void onGetStoreInfoSuccess(RESP_Store resp_store);
    void onGetStoreInfoError();

    void onTakePictureGallary(int type, Uri uri);
    void onTakePictureCamera(int type, Bitmap bitmap);
    void onValidateError(String error);
    void startActivityForResult(Intent intent, int requestCode);
    Activity getActivity();
    Fragment getFragment();
}