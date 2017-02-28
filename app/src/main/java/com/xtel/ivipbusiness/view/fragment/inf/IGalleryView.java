package com.xtel.ivipbusiness.view.fragment.inf;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;

import com.xtel.ivipbusiness.model.entity.Gallery;
import com.xtel.ivipbusiness.model.entity.RESP_Image;
import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;

import java.util.ArrayList;

/**
 * Created by Mr. M.2 on 1/13/2017
 */

public interface IGalleryView {

    void onGetDataError();
    void getNewSession(ICmd iCmd, Object... parame);

    void onLoadMore();
    void onGetGallerySuccess(ArrayList<Gallery> arrayList);
    void onGetStoresError(Error error);
    void onDeleteSuccess();
    void onRequestError(Error error);

    void onTakePictureGallary(int type, Uri uri);
    void onTakePictureCamera(int type, Bitmap bitmap);
    void onPostPictureSuccess(RESP_Image resp_image);
    void onAddPictureSuccess();

    void onDeleteGallery(int id, int position);
    void closeProgressBar();
    void showShortToast(String message);
    void onNoNetwork();

    void startActivity(Class clazz, String key, Object object);
    void startActivityForResult(Intent intent, int requestCode);
    Activity getActivity();
    Fragment getFragment();
}
