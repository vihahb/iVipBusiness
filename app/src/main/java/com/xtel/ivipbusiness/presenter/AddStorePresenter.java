package com.xtel.ivipbusiness.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.StoresModel;
import com.xtel.ivipbusiness.model.entity.PlaceModel;
import com.xtel.ivipbusiness.model.entity.RESP_Image;
import com.xtel.ivipbusiness.model.entity.RESP_Store;
import com.xtel.ivipbusiness.view.activity.inf.IAddStoreView;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.utils.JsonHelper;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.xtel.nipservicesdk.utils.PermissionHelper;
import com.xtel.sdk.callback.CallbackImageListener;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.ImageManager;
import com.xtel.sdk.utils.TextUnit;

/**
 * Created by Vulcl on 1/15/2017
 */

public class AddStorePresenter extends BasicPresenter {
    private IAddStoreView view;

    private String[] permission = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int TAKE_PICTURE_TYPE = 0;
    private final int REQUEST_CODE_CAMERA = 101, REQUEST_CAMERA = 100;

    private String STOREY_TYPE, URL_BANNER = "", URL_LOGO = "";


    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(Object... params) {
            if ((int) params[0] == 1) {
                StoresModel.getInstance().addStore((String) params[1], new ResponseHandle<RESP_Store>(RESP_Store.class) {
                    @Override
                    public void onSuccess(RESP_Store obj) {
                        view.closeProgressBar();
                        view.onAddStoreSuccess();
                    }

                    @Override
                    public void onError(Error error) {
                        if (error.getCode() == 2)
                            view.getNewSession(iCmd);
                        else {
                            view.closeProgressBar();
                            view.showShortToast(JsonParse.getCodeMessage(error.getCode(), view.getActivity().getString(R.string.error_try_again)));
                        }
                    }
                });
            }
        }
    };


    public AddStorePresenter(IAddStoreView view) {
        this.view = view;
    }

    public void getData() {
        STOREY_TYPE = view.getActivity().getIntent().getStringExtra(Constants.MODEL);

        if (STOREY_TYPE == null)
            view.onGetDataError();
    }

    public void takePicture(int type) {
        TAKE_PICTURE_TYPE = type;
        if (!PermissionHelper.checkListPermission(permission, view.getActivity(), REQUEST_CAMERA))
            return;

        takePictureNow();
    }

    private void takePictureNow() {
        final Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");

        //Create any other intents you want
        final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Add them to an intent array
        Intent[] intents = new Intent[]{cameraIntent};

        //Create a choose from your first intent then pass in the intent array
        final Intent chooserIntent = Intent.createChooser(galleryIntent, view.getActivity().getString(R.string.sselect_image));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);
        view.startActivityForResult(chooserIntent, REQUEST_CODE_CAMERA);
    }

    public void postImage(Bitmap bitmap, final int type) {
        boolean isBigImage;
        if (type == 0)
            isBigImage = true;
        else
            isBigImage = false;

        ImageManager.getInstance().postImage(view.getActivity(), bitmap, isBigImage, new CallbackImageListener() {
            @Override
            public void onSuccess(RESP_Image resp_image) {
                Log.e("url_success", resp_image.getUri());
                if (type == 0) {
                    URL_BANNER = resp_image.getServer_path();
                    view.onLoadPicture(resp_image.getUri(), type);
                } else {
                    URL_LOGO = resp_image.getServer_path();;
                    view.onLoadPicture(resp_image.getUri(), type);
                }
                Log.e("url_success_2", resp_image.getUri());
            }

            @Override
            public void onError() {
                view.showShortToast(view.getActivity().getString(R.string.error_try_again));
            }
        });
    }

    public void addStore(String name, PlaceModel placeModel, String phone, String des, String begin_time, String end_time, int type) {
        if (URL_BANNER.isEmpty()) {
            view.showShortToast(view.getActivity().getString(R.string.error_input_banner));
            return;
        } else if (URL_LOGO.isEmpty()) {
            view.showShortToast(view.getActivity().getString(R.string.error_input_logo));
            return;
        } else if (!TextUnit.getInstance().validateText(name)) {
            view.showShortToast(view.getActivity().getString(R.string.error_input_store_name));
            return;
        } else if (placeModel == null) {
            view.showShortToast(view.getActivity().getString(R.string.error_input_address));
            return;
        } else if (!TextUnit.getInstance().validatePhone(phone)) {
            view.showShortToast(view.getActivity().getString(R.string.error_input_phone));
            return;
        } else if (!TextUnit.getInstance().validateText(begin_time)) {
            view.showShortToast(view.getActivity().getString(R.string.error_input_begin_time));
            return;
        } else if (!TextUnit.getInstance().validateText(end_time)) {
            view.showShortToast(view.getActivity().getString(R.string.error_input_end_time));
            return;
        }

        view.showProgressBar(false,false,null, view.getActivity().getString(R.string.adding_store));

        RESP_Store store = new RESP_Store();
        store.setBanner(URL_BANNER);
        store.setLogo(URL_LOGO);
        store.setName(name);
        store.setStore_type(STOREY_TYPE);
        store.setAddress(placeModel.getAddress());
        store.setLocation_lat(placeModel.getLatitude());
        store.setLocation_lng(placeModel.getLongtitude());
        store.setPhonenumber(phone);
        store.setDescription(des);
        store.setBegin_time(Constants.convertTimeToLong(begin_time));
        store.setEnd_time(Constants.convertTimeToLong(end_time));
        store.setType(type);

        Log.e("store_object", JsonHelper.toJson(store));
        iCmd.execute(1, JsonHelper.toJson(store));
    }















    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA) {
            boolean check = true;
            for (int grantresults : grantResults) {
                if (grantresults == PackageManager.PERMISSION_DENIED) {
                    check = false;
                    break;
                }
            }

            if (check)
                takePictureNow();
            else
                view.showShortToast(view.getActivity().getString(R.string.error_permission));
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                if (uri != null) {
                    view.onTakePictureGallary(TAKE_PICTURE_TYPE, uri);
                } else {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    view.onTakePictureCamera(TAKE_PICTURE_TYPE, bitmap);
                }
            }
        }
    }
}