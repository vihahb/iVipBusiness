package com.xtel.ivipbusiness.view.activity.inf;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import com.xtel.nipservicesdk.callback.ICmd;

import java.io.File;

/**
 * Created by Vulcl on 1/15/2017
 */

public interface IAddStoreView {

    void onGetDataChain();
    void onGetDataError();
    void onTakePictureGallary(int type, Uri uri);
    void onTakePictureCamera(int type, Bitmap bitmap);
//    void onLoadPicture(File url, int type);
    void onAddStoreSuccess(String message);
    void getNewSession(ICmd iCmd);

    void showShortToast(String message);
    void showProgressBar(boolean isTouchOutside, boolean isCancel, String title, String message);
    void closeProgressBar();

    void startActivityForResult(Intent intent, int requestCode);
    Activity getActivity();
}