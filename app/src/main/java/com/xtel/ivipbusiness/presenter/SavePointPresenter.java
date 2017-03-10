package com.xtel.ivipbusiness.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.MemberModel;
import com.xtel.ivipbusiness.model.entity.RESP_Image;
import com.xtel.ivipbusiness.model.entity.RESP_Point;
import com.xtel.ivipbusiness.model.entity.RESP_Save_Point;
import com.xtel.ivipbusiness.view.activity.inf.ISavePointView;
import com.xtel.ivipbusiness.model.entity.RESP_Member_Info;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.utils.JsonHelper;
import com.xtel.nipservicesdk.utils.PermissionHelper;
import com.xtel.sdk.callback.CallbackImageListener;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.ImageManager;
import com.xtel.sdk.utils.TextUnit;

import java.io.File;

/**
 * Created by Vulcl on 3/3/2017
 */

public class SavePointPresenter {
    private ISavePointView view;

    private RESP_Member_Info resp_member_info;
    private String BILL_PATH;

    private String[] permission = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final int REQUEST_CODE_CAMERA = 101, REQUEST_CAMERA = 100;

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(final Object... params) {
            if (params.length > 0) {
                final RESP_Save_Point resp_save_point = (RESP_Save_Point) params[0];
                MemberModel.getInstance().savePointForMember(JsonHelper.toJson(resp_save_point), new ResponseHandle<RESP_Point>(RESP_Point.class) {
                    @Override
                    public void onSuccess(RESP_Point obj) {
                        view.onSavePointSuccess(resp_member_info.getFull_name(), obj.getPoint());
                    }

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Error error) {
                        if (error.getCode() == 2)
                            view.getNewSession(iCmd, params);
                        else
                            view.onRequestError(error);
                    }
                });
            }
        }
    };

    public SavePointPresenter(ISavePointView view) {
        this.view = view;
    }

    public void getData() {
        try {
            resp_member_info = (RESP_Member_Info) view.getActivity().getIntent().getSerializableExtra(Constants.MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (resp_member_info == null)
            view.onGetDataError();
        else
            view.onGetDataSuccess(resp_member_info);
    }

    public void takePicture() {
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
        view.getActivity().startActivityForResult(chooserIntent, REQUEST_CODE_CAMERA);
    }

    public void postImage(Bitmap bitmap) {
        ImageManager.getInstance().postImage(view.getActivity(), bitmap, true, new CallbackImageListener() {
            @Override
            public void onSuccess(RESP_Image resp_image, File file) {
                BILL_PATH = resp_image.getServer_path();
                view.onLoadPicture(file);
            }

            @Override
            public void onError() {
                view.closeProgressBar();
                view.showShortToast(-1, view.getActivity().getString(R.string.error_try_again));
            }
        });
    }

    public void savePoint(String money, String bill_code) {
        if (money == null) {
            view.showShortToast(1, view.getActivity().getString(R.string.error_input_money));
            return;
        }
        int total_money = TextUnit.getInstance().validateInteger(money);
        if (total_money == -1) {
            view.showShortToast(1, view.getActivity().getString(R.string.error_money));
            return;
        }
        if (!TextUnit.getInstance().validateText(BILL_PATH)) {
            view.showShortToast(1, view.getActivity().getString(R.string.error_input_bill_path));
            return;
        }

        view.showProgressBar(false, false, null, view.getActivity().getString(R.string.doing_save_point));

        RESP_Save_Point resp_save_point = new RESP_Save_Point();
        resp_save_point.setStore_id(resp_member_info.getStore_id());
        resp_save_point.setUser_code(resp_member_info.getUser_code());
        resp_save_point.setMoney(total_money);
        resp_save_point.setBill_code(bill_code);
        resp_save_point.setBill_url(BILL_PATH);

        iCmd.execute(resp_save_point);
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
                view.showShortToast(-1, view.getActivity().getString(R.string.error_permission));
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                if (uri != null) {
                    view.onTakePictureGallary(uri);
                } else {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    view.onTakePictureCamera(bitmap);
                }
            }
        }
    }
}