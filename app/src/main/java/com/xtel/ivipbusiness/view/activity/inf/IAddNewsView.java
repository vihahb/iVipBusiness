package com.xtel.ivipbusiness.view.activity.inf;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.xtel.nipservicesdk.callback.ICmd;

import java.io.File;

/**
 * Created by Vulcl on 2/18/2017
 */

public interface IAddNewsView {

    void onGetDataError();
    void onTakePictureGallary(int type, Uri uri);
    void onTakePictureCamera(int type, Bitmap bitmap);
    void onAddNewsSuccess();
    void getNewSession(ICmd iCmd);

    void showShortToast(int type, String message);
    void showProgressBar(boolean isTouchOutside, boolean isCancel, String title, String message);
    void closeProgressBar();

    void startActivityForResult(Intent intent, int requestCode);
    Activity getActivity();
}
