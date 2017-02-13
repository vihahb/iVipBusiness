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
import com.xtel.ivipbusiness.model.StoresModel;
import com.xtel.ivipbusiness.model.entity.RESP_Store;
import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.ivipbusiness.view.activity.ViewStoreActivity;
import com.xtel.ivipbusiness.view.fragment.inf.IStoreInfoView;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.utils.PermissionHelper;
import com.xtel.sdk.commons.Constants;

/**
 * Created by Vulcl on 1/21/2017
 */

public class StoreInfoPresenter {
    private IStoreInfoView view;

    private boolean isExists = true;
    private SortStore sortStore;
    private int TAKE_PICTURE_TYPE = 0;
    private final int REQUEST_CODE_CAMERA = 101, REQUEST_CAMERA = 100;
    private String[] permission = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(Object... params) {
            if (((int) params[0]) == 1)
                StoresModel.getInstance().getStoreInfo((int) params[1], (String) params[2], new ResponseHandle<RESP_Store>(RESP_Store.class) {
                    @Override
                    public void onSuccess(RESP_Store obj) {
                        if (isExists) {
                            ((ViewStoreActivity) view.getActivity()).setResp_store(obj);
                            view.onGetStoreInfoSuccess(obj);
                        }
                    }

                    @Override
                    public void onError(Error error) {
                        if (isExists)
                        view.onGetStoreInfoError();
                    }
                });
        }
    };

    public StoreInfoPresenter(IStoreInfoView view) {
        this.view = view;
    }

//    Kiểm tra xem có data truyền vào hay không
    public void getData() {
        try {
            sortStore = (SortStore) view.getFragment().getArguments().getSerializable(Constants.MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sortStore != null) {
            if (((ViewStoreActivity) view.getActivity()).getResp_store() == null)
                getStoreInfo();
            else
                view.onGetStoreInfoSuccess(((ViewStoreActivity) view.getActivity()).getResp_store());
        } else
            view.onGetDataError();
    }

    private void getStoreInfo() {
        iCmd.execute(1, sortStore.getId(), sortStore.getStore_type());
    }

//    Kiểm tra cấp quyền camera
    public void takePicture(int type) {
        TAKE_PICTURE_TYPE = type;
        if (!PermissionHelper.checkListPermission(permission, view.getActivity(), REQUEST_CAMERA))
            return;

        takePictureNow();
    }

//    Bắt đầu chọn ảnh từ camera hoặc gallary
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

//    Lắng nghe người dùng cấp quyền truy cập camera hay không
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
                view.onValidateError(view.getActivity().getString(R.string.error_permission));
        }
    }

//    Lắng nghe khi người dùng đã chọn xong ảnh hoặc hủy
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

//    set giá trị cho biến để kiểm tra xem fragment có đang được view hay không
    public void setExists(boolean exists) {
        isExists = exists;
    }
}