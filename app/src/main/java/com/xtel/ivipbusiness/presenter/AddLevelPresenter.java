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
import com.xtel.ivipbusiness.model.entity.Card;
import com.xtel.ivipbusiness.model.entity.LevelObject;
import com.xtel.ivipbusiness.model.entity.RESP_Card;
import com.xtel.ivipbusiness.model.entity.RESP_Image;
import com.xtel.ivipbusiness.view.activity.inf.IAddLevelView;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Basic;
import com.xtel.nipservicesdk.utils.PermissionHelper;
import com.xtel.sdk.callback.CallbackImageListener;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.ImageManager;
import com.xtel.sdk.utils.TextUnit;

import java.io.File;

/**
 * Created by Vulcl on 3/4/2017
 */

public class AddLevelPresenter {
    private IAddLevelView view;

    private String[] permission = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final int REQUEST_CODE_CAMERA = 101, REQUEST_CAMERA = 100;
    private int LEVEL = -1;

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(final Object... params) {
            MemberModel.getInstance().getMemberCardDefault(new ResponseHandle<RESP_Card>(RESP_Card.class) {
                @Override
                public void onSuccess(RESP_Card obj) {
                    view.onGetCardDefaultSuccess(obj.getData());
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
    };

    public AddLevelPresenter(IAddLevelView view) {
        this.view = view;
    }

    public void getData() {
        try {
            LEVEL = view.getActivity().getIntent().getIntExtra(Constants.MODEL, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (LEVEL != -1) {
            view.onGetDataSuccess(LEVEL);
            iCmd.execute();
        } else
            view.onGetDataError();
    }

    public void done(String limit, String name, Card card) {
        int limit_point = TextUnit.getInstance().validateInteger(limit);

        if (checkData(limit_point, name, card)) {
            LevelObject levelObject = new LevelObject();
            levelObject.setLevel_limit(limit_point);
            levelObject.setLevel_name(name);
            levelObject.setLevel(null);
            levelObject.setMember_card(card.getCard_path());
            levelObject.setUrl_card(card.getCard_url());

            Intent intent = new Intent();
            intent.putExtra(Constants.MODEL, levelObject);
            view.getActivity().setResult(Activity.RESULT_OK, intent);
            view.getActivity().finish();
        }
    }

    private boolean checkData(int limit, String name, Card member_card) {
        if (limit == -1) {
            view.showShortToast(view.getActivity().getString(R.string.error_input_min_point));
            return false;
        }
        if (!TextUnit.getInstance().validateText(name)) {
            view.showShortToast(view.getActivity().getString(R.string.error_input_level_name));
            return false;
        }
        if (member_card == null) {
            view.showShortToast(view.getActivity().getString(R.string.error_input_member_card));
            return false;
        }

        return true;
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

//    public void postImage(Bitmap bitmap, boolean isBigImage) {
//
//        ImageManager.getInstance().postImage(view.getActivity(), bitmap, isBigImage, new CallbackImageListener() {
//            @Override
//            public void onSuccess(RESP_Image resp_image, File file) {
//                resp_image.setFile_path(file.getPath());
//                view.onPostImageSuccess(resp_image);
//            }
//
//            @Override
//            public void onError() {
//                view.closeProgressBar();
//                view.showShortToast(view.getActivity().getString(R.string.error_try_again));
//            }
//        });
//    }


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
                    view.onTakePictureGallary(uri);
                } else {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    view.onTakePictureCamera(bitmap);
                }
            }
        }
    }

}