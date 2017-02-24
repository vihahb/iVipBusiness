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
import com.xtel.ivipbusiness.model.UserModel;
import com.xtel.ivipbusiness.model.entity.PlaceModel;
import com.xtel.ivipbusiness.model.entity.RESP_Full_Profile;
import com.xtel.ivipbusiness.model.entity.RESP_Image;
import com.xtel.ivipbusiness.view.activity.inf.IProfileView;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_None;
import com.xtel.nipservicesdk.utils.JsonHelper;
import com.xtel.nipservicesdk.utils.PermissionHelper;
import com.xtel.sdk.callback.CallbackImageListener;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.ImageManager;
import com.xtel.sdk.utils.NetWorkInfo;

import java.io.File;

/**
 * Created by Vulcl on 1/17/2017
 */

public class ProfilePresenter extends BasicPresenter {
    private IProfileView view;

    private RESP_Full_Profile resp_full_profile;

    private String[] permission = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int TAKE_PICTURE_TYPE = 0;
    private final int REQUEST_CODE_CAMERA = 101, REQUEST_CAMERA = 100;
    private String PATH_AVATAR, URL_AVATAR;

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(final Object... params) {
            if (((int) params[0]) == 1)
                UserModel.getInstance().getFulllUserInfo(new ResponseHandle<RESP_Full_Profile>(RESP_Full_Profile.class) {
                    @Override
                    public void onSuccess(RESP_Full_Profile obj) {
                        UserModel.getInstance().saveFullUserInfo(obj);
//                        URL_AVATAR = obj.getAvatar();
                        view.onGetProfileSuccess(obj);
                    }

                    @Override
                    public void onError(Error error) {
                        if (error.getCode() == 2)
                            view.getNewSession(iCmd, ((int) params[0]));
                        else
                            view.onRequestError(error);
                    }
                });
            else if (((int) params[0]) == 2)
                UserModel.getInstance().updateUserInfo(resp_full_profile, new ResponseHandle<RESP_None>(RESP_None.class) {
                    @Override
                    public void onSuccess(RESP_None obj) {
                        resp_full_profile.setAvatar(URL_AVATAR);
                        UserModel.getInstance().saveFullUserInfo(resp_full_profile);
                        view.onUpdateProfileSuccess();
                    }

                    @Override
                    public void onError(Error error) {
                        if (error.getCode() == 2)
                            view.getNewSession(iCmd, ((int) params[0]));
                        else if (error.getCode() == 101) {
                            view.closeProgressBar();
                            view.onValidateError(view.getActivity().getString(R.string.error_user_not_exists));
                        } else {
                            view.closeProgressBar();
                            view.onRequestError(error);
                        }
                    }
                });
        }
    };

    public ProfilePresenter(IProfileView view) {
        this.view = view;
    }

    public void getProfile() {
        resp_full_profile = UserModel.getInstance().getFulllUserInfo();

        if (resp_full_profile != null) {
//            URL_AVATAR = resp_full_profile.getAvatar();
            view.onGetProfileSuccess(resp_full_profile);
        } else
            iCmd.execute(1);
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
        ImageManager.getInstance().postImage(view.getActivity(), bitmap, true, new CallbackImageListener() {
            @Override
            public void onSuccess(RESP_Image resp_image, File file) {
//                if (type == 0) {
//                    PATH_BANNER = resp_image.getServer_path();
//                    view.onLoadPicture(file, type);
//                } else {
                PATH_AVATAR = resp_image.getServer_path();
                URL_AVATAR = resp_image.getUri();
                view.onLoadPicture(file, type);
//                }
            }

            @Override
            public void onError() {
                view.onValidateError(view.getActivity().getString(R.string.error_try_again));
            }
        });
    }

    public void updateUser(String fullname, int gender, String birthday, String email, PlaceModel placeModel) {
        if (!validateInput("notnull", fullname, gender, birthday, email, placeModel))
            return;
        if (!NetWorkInfo.isOnline(view.getActivity())) {
            view.onNoInternet();
            return;
        }

        view.showProgressBar(false, false, null, view.getActivity().getString(R.string.updating));

        resp_full_profile.setAvatar(PATH_AVATAR);
        resp_full_profile.setFullname(fullname);
        resp_full_profile.setGender(gender);
        resp_full_profile.setBirthday(Constants.convertDataToLong(birthday));
        resp_full_profile.setEmail(email);
        resp_full_profile.setAddress(placeModel.getAddress());

        Log.e(this.getClass().getSimpleName(), JsonHelper.toJson(resp_full_profile));
        iCmd.execute(2);
    }

    private boolean validateInput(String avatar, String fullname, int gender, String birthday, String email, PlaceModel placeModel) {
        if (!validateText(avatar)) {
            view.onValidateError(view.getActivity().getString(R.string.error_input_avatar));
            return false;
        }
        if (!validateText(fullname)) {
            view.onValidateError(view.getActivity().getString(R.string.error_input_fullname));
            return false;
        }
        if (gender == 0) {
            view.onValidateError(view.getActivity().getString(R.string.error_input_gender));
            return false;
        }
        if (!validateText(birthday)) {
            view.onValidateError(view.getActivity().getString(R.string.error_input_birthday));
            return false;
        }
        if (!validateEmail(email)) {
            view.onValidateError(view.getActivity().getString(R.string.error_input_email));
            return false;
        }
        if (placeModel == null) {
            view.onValidateError(view.getActivity().getString(R.string.error_input_address));
            return false;
        }

        return true;
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
                view.onValidateError(view.getActivity().getString(R.string.error_permission));
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
