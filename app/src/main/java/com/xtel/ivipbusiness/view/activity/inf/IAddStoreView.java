package com.xtel.ivipbusiness.view.activity.inf;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by Vulcl on 1/15/2017
 */

public interface IAddStoreView {

    void onGetDataError();
    void onTakePictureGallary(int type, Uri uri);
    void onTakePictureCamera(int type, Bitmap bitmap);
    void onLoadPicture(String url, int type);
    void showShortToast(String message);
    void startActivityForResult(Intent intent, int requestCode);
    Activity getActivity();
}