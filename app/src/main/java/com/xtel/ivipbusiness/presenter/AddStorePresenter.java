package com.xtel.ivipbusiness.presenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.view.activity.inf.IAddStoreView;

/**
 * Created by Vulcl on 1/15/2017
 */

public class AddStorePresenter extends BasicPresenter {
    private IAddStoreView view;
    private int TAKE_PICTURE_TYPE = 0;
    private final int REQUEST_CODE_CAMERA = 101;

    public AddStorePresenter(IAddStoreView view) {
        this.view = view;
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