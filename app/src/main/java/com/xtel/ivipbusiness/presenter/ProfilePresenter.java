package com.xtel.ivipbusiness.presenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.UserModel;
import com.xtel.ivipbusiness.model.entity.RESP_Full_Profile;
import com.xtel.ivipbusiness.view.activity.inf.IProfileView;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.utils.JsonHelper;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.NetWorkInfo;

/**
 * Created by Vulcl on 1/17/2017
 */

public class ProfilePresenter extends BasicPresenter {
    private IProfileView view;

    private int TAKE_PICTURE_TYPE = 0;
    private final int REQUEST_CODE_CAMERA = 101;

    public ProfilePresenter(IProfileView view) {
        this.view = view;
    }

    public void getProfile() {
        UserModel.getIntances().getUserInfo(new ResponseHandle<RESP_Full_Profile>(RESP_Full_Profile.class) {
            @Override
            public void onSuccess(RESP_Full_Profile obj) {
                view.onGetProfileSuccess(obj);
            }

            @Override
            public void onError(Error error) {
                view.onGetProfileError(error);
            }
        });
    }

    public void takePicture(int type) {
        TAKE_PICTURE_TYPE = type;

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

    public void updateUser(String avatar, String fullname, int gender, String birthday, String phone, String email, String address) {
        if (!validateInput(avatar, fullname, gender, birthday, email, address))
            return;
        if (!NetWorkInfo.isOnline(view.getActivity())) {
            view.onNoInternet();
            return;
        }

        RESP_Full_Profile resp_full_profile = new RESP_Full_Profile();
        resp_full_profile.setAvatar(avatar);
        resp_full_profile.setFullname(fullname);
        resp_full_profile.setGender(gender);
        resp_full_profile.setPhonenumber(phone);
        resp_full_profile.setBirthday(Constants.convertDataToLong(birthday));
        resp_full_profile.setEmail(email);

        Log.e(this.getClass().getSimpleName(), JsonHelper.toJson(resp_full_profile));
    }

    private boolean validateInput(String avatar, String fullname, int gender, String birthday, String email, String address) {
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
        if (!validateText(email)) {
            view.onValidateError(view.getActivity().getString(R.string.error_input_email));
            return false;
        }
        if (!validateText(address)) {
            view.onValidateError(view.getActivity().getString(R.string.error_input_address));
            return false;
        }

        return true;
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
