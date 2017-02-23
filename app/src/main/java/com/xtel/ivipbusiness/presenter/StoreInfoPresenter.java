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
import com.xtel.ivipbusiness.model.entity.RESP_Image;
import com.xtel.ivipbusiness.model.entity.RESP_Store;
import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.ivipbusiness.view.activity.ViewStoreActivity;
import com.xtel.ivipbusiness.view.fragment.inf.IStoreInfoView;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_None;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.xtel.nipservicesdk.utils.PermissionHelper;
import com.xtel.sdk.callback.CallbackImageListener;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.ImageManager;
import com.xtel.sdk.utils.NetWorkInfo;
import com.xtel.sdk.utils.TextUnit;

import java.io.File;

/**
 * Created by Vulcl on 1/21/2017
 */

public class StoreInfoPresenter {
    private IStoreInfoView view;

    private boolean isExists = true;
    private SortStore sortStore;
    private int TAKE_PICTURE_TYPE = 0;
    private final int REQUEST_CODE_CAMERA = 101, REQUEST_CAMERA = 100;
    private String STOREY_TYPE, URL_BANNER, PATH_BANNER, URL_LOGO, PATH_LOGO;
    private String[] permission = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(final Object... params) {
            if (((int) params[0]) == 1)
                StoresModel.getInstance().getStoreInfo((int) params[1], (String) params[2], new ResponseHandle<RESP_Store>(RESP_Store.class) {
                    @Override
                    public void onSuccess(RESP_Store obj) {
                        if (isExists) {
                            obj.setId(sortStore.getId());
                            ((ViewStoreActivity) view.getActivity()).setResp_store(obj);
                            view.onGetStoreInfoSuccess(obj);
                        }
                    }

                    @Override
                    public void onError(Error error) {
                        if (isExists) {
                            if (error.getCode() == 2)
                                view.getNewSession(iCmd);
                            else
                                view.onGetStoreInfoError();
                        }
                    }
                });
            else if (((int) params[0]) == 2)
                StoresModel.getInstance().updateStore((RESP_Store) params[1], new ResponseHandle<RESP_None>(RESP_None.class) {
                    @Override
                    public void onSuccess(RESP_None obj) {
                        RESP_Store resp_store = (RESP_Store) params[1];

                        if (PATH_BANNER != null)
                            resp_store.setBanner(URL_BANNER);
                        if (PATH_LOGO != null)
                            resp_store.setLogo(URL_LOGO);

                        view.onUpdateStoreInfoSuccess();
                    }

                    @Override
                    public void onError(Error error) {
                        if (isExists) {
                            if (error.getCode() == 2)
                                view.getNewSession(iCmd);
                            else {
                                view.closeProgressBar();
                                view.onValidateError(JsonParse.getCodeMessage(error.getCode(), view.getActivity().getString(R.string.error_try_again)));
                            }
                        }
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

    public void postImage(Bitmap bitmap, final int type) {
        boolean isBigImage;
        isBigImage = type == 0;

        ImageManager.getInstance().postImage(view.getActivity(), bitmap, isBigImage, new CallbackImageListener() {
            @Override
            public void onSuccess(RESP_Image resp_image, File file) {
                if (type == 0) {
                    PATH_BANNER = resp_image.getServer_path();
                    URL_BANNER = resp_image.getUri();
                    view.onLoadPicture(file, type);
                } else {
                    PATH_LOGO = resp_image.getServer_path();
                    URL_LOGO = resp_image.getUri();
                    view.onLoadPicture(file, type);
                }
            }

            @Override
            public void onError() {
                view.closeProgressBar();
                view.onValidateError(view.getActivity().getString(R.string.error_try_again));
            }
        });
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

    public void updateStore(RESP_Store resp_store) {
        if (!NetWorkInfo.isOnline(view.getActivity())) {
            view.onValidateError(view.getActivity().getString(R.string.error_no_internet));
            return;
        }

        if (resp_store.getBanner().isEmpty()) {
            view.onValidateError(view.getActivity().getString(R.string.error_input_banner));
            return;
        } else if (resp_store.getLogo().isEmpty()) {
            view.onValidateError(view.getActivity().getString(R.string.error_input_logo));
            return;
        } else if (!TextUnit.getInstance().validateText(resp_store.getName())) {
            view.onValidateError(view.getActivity().getString(R.string.error_input_store_name));
            return;
        } else if (resp_store.getAddress() == null) {
            view.onValidateError(view.getActivity().getString(R.string.error_input_address));
            return;
        } else if (!TextUnit.getInstance().validatePhone(resp_store.getPhonenumber())) {
            view.onValidateError(view.getActivity().getString(R.string.error_input_phone));
            return;
        }

//        if (PATH_BANNER != null)
            resp_store.setBanner(PATH_BANNER);
//        else
//            resp_store.setBanner(null);
//        if (PATH_LOGO != null)
            resp_store.setLogo(PATH_LOGO);
//        else
//            resp_store.setLogo(null);


        view.showProgressBar(false, false, null, view.getActivity().getString(R.string.updating_store));
        iCmd.execute(2, resp_store);
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