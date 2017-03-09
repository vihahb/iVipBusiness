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
import com.xtel.ivipbusiness.model.GalleryModel;
import com.xtel.ivipbusiness.model.entity.RESP_Gallery;
import com.xtel.ivipbusiness.model.entity.RESP_Image;
import com.xtel.ivipbusiness.model.entity.RESP_Picture;
import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.ivipbusiness.view.fragment.inf.IGalleryView;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_None;
import com.xtel.nipservicesdk.utils.PermissionHelper;
import com.xtel.sdk.callback.CallbackImageListener;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.ImageManager;
import com.xtel.sdk.utils.NetWorkInfo;

import java.io.File;

/**
 * Created by Mr. M.2 on 1/13/2017
 */

public class GalleryPresenter {
    private IGalleryView view;

    private boolean isExists = true;

    private int PAGE = 1;
    private int PAGESIZE = 10;
    private final String STORE_TYPE = "STORE";

    private String[] permission = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int TAKE_PICTURE_TYPE = 0;
    private final int REQUEST_CODE_CAMERA = 101, REQUEST_CAMERA = 100;

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(final Object... params) {
            if (params.length > 0) {
                int type = ((int) params[0]);
                if (type == 1) {
                    boolean isStore = (Constants.SORT_STORE.getStore_type().equals(STORE_TYPE));

                    GalleryModel.getInstance().getListGallery(Constants.SORT_STORE.getId(), PAGE, PAGESIZE, isStore, new ResponseHandle<RESP_Gallery>(RESP_Gallery.class) {
                        @Override
                        public void onSuccess(RESP_Gallery obj) {
                            if (isExists) {
                                PAGE++;
                                view.onGetGallerySuccess(obj.getData());
                            }
                        }

                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Error error) {
                            if (isExists) {
                                if (error.getCode() == 2)
                                    view.getNewSession(iCmd, params);
                                else
                                    view.onGetStoresError(error);
                            }
                        }
                    });
                } else if (type == 2)
                    GalleryModel.getInstance().deleteGallery((int) params[1], (int) params[2], new ResponseHandle<RESP_None>(RESP_None.class) {
                        @Override
                        public void onSuccess(RESP_None obj) {
                            if (isExists)
                                view.onDeleteSuccess();
                        }

                        @Override
                        public void onSuccess() {
                            if (isExists)
                                view.onDeleteSuccess();
                        }

                        @Override
                        public void onError(Error error) {
                            if (isExists)
                                if (error.getCode() == 2)
                                    view.getNewSession(iCmd, params);
                                else
                                    view.onRequestError(error);
                        }
                    });
                else if (type == 3) {
                    boolean isStore = (Constants.SORT_STORE.getStore_type().equals(STORE_TYPE));

                    GalleryModel.getInstance().addGallery((RESP_Picture) params[1], isStore, new ResponseHandle<RESP_None>(RESP_None.class) {
                        @Override
                        public void onSuccess(RESP_None obj) {
                            if (isExists)
                                view.onAddPictureSuccess();
                        }

                        @Override
                        public void onSuccess() {
                            if (isExists)
                                view.onAddPictureSuccess();
                        }

                        @Override
                        public void onError(Error error) {
                            if (isExists)
                                if (error.getCode() == 2)
                                    view.getNewSession(iCmd, params);
                                else
                                    view.onRequestError(error);
                        }
                    });
                }
            }
        }
    };

    public GalleryPresenter(IGalleryView view) {
        this.view = view;
    }

    public void getGallery(boolean isClear) {
        if (!NetWorkInfo.isOnline(view.getActivity())) {
            view.onNoNetwork();
            return;
        }

        if (!getData())
            view.onGetDataError();

        if (isClear)
            PAGE = 1;

        iCmd.execute(1);
    }

    public void deleteGallery(int gallery_id) {
        iCmd.execute(2, Constants.SORT_STORE.getId(), gallery_id);
    }

    public boolean getData() {
        return  (Constants.SORT_STORE != null);
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
        view.startActivityForResult(chooserIntent, REQUEST_CODE_CAMERA);
    }

    public void postImage(Bitmap bitmap, final int type) {
        boolean isBigImage;
        isBigImage = type == 0;

        ImageManager.getInstance().postImage(view.getActivity(), bitmap, isBigImage, new CallbackImageListener() {
            @Override
            public void onSuccess(RESP_Image resp_image, File file) {
                view.onPostPictureSuccess(resp_image);
            }

            @Override
            public void onError() {
                view.closeProgressBar();
                view.showShortToast(view.getActivity().getString(R.string.error_try_again));
            }
        });
    }

    public void addPicture(RESP_Image resp_image) {
        RESP_Picture resp_picture = new RESP_Picture();
        resp_picture.setId(Constants.SORT_STORE.getId());
        resp_picture.setUrl(resp_image.getUri());

        iCmd.execute(3, resp_picture);
    }

    public void setExists(boolean isExists) {
        this.isExists = isExists;
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