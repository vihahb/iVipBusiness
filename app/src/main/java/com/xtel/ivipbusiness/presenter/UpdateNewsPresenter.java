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
import com.xtel.ivipbusiness.model.NewsModel;
import com.xtel.ivipbusiness.model.entity.News;
import com.xtel.ivipbusiness.model.entity.RESP_Image;
import com.xtel.ivipbusiness.model.entity.RESP_News;
import com.xtel.ivipbusiness.model.entity.Voucher;
import com.xtel.ivipbusiness.view.activity.inf.IUpdateNewsView;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_None;
import com.xtel.nipservicesdk.utils.JsonHelper;
import com.xtel.nipservicesdk.utils.PermissionHelper;
import com.xtel.sdk.callback.CallbackImageListener;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.ImageManager;

import java.io.File;

/**
 * Created by Vulcl on 2/21/2017
 */

public class UpdateNewsPresenter extends BasicPresenter {
    private IUpdateNewsView view;

    private String[] permission = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final int REQUEST_CODE_CAMERA = 101, REQUEST_CAMERA = 100;

    private RESP_News resp_news;
    private News news;
    private final String CHAIN = "CHAIN";
    private String URL_BANNER;

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(final Object... params) {
            if (params.length > 0) {
                int type = ((int) params[0]);
                if (type == 1) {
                    NewsModel.getInstance().getNewsInfo((int) params[1], new ResponseHandle<RESP_News>(RESP_News.class) {
                        @Override
                        public void onSuccess(RESP_News obj) {
                            resp_news = obj;
                            URL_BANNER = obj.getBanner();
                            view.onGetNewsInfoSuccess(obj);
                        }

                        @Override
                        public void onError(Error error) {
                            if (error.getCode() == 2)
                                view.getNewSession(iCmd, params);
                            else
                                view.onRequestError(error);
                        }
                    });
                } else if (type == 2) {
                    NewsModel.getInstance().updateNews(news.getId(), (String) params[1], new ResponseHandle<RESP_None>(RESP_None.class) {
                        @Override
                        public void onSuccess(RESP_None obj) {
                             view.onUpdateSuccess();
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
        }
    };

    public UpdateNewsPresenter(IUpdateNewsView view) {
        this.view = view;
    }

    public void getData() {
        try {
            news = (News) view.getActivity().getIntent().getSerializableExtra(Constants.MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (news == null)
            view.onGetDataError();
        else
            iCmd.execute(1, news.getId());
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

    public void postImage(Bitmap bitmap) {
        ImageManager.getInstance().postImage(view.getActivity(), bitmap, true, new CallbackImageListener() {
            @Override
            public void onSuccess(RESP_Image resp_image, File file) {
                URL_BANNER = resp_image.getServer_path();
                view.onLoadPicture(file);
            }

            @Override
            public void onError() {
                view.closeProgressBar();
                view.showShortToast(-1, view.getActivity().getString(R.string.error_try_again));
            }
        });
    }

    public void updateNews(String title, int news_type, String des, boolean isPublic, boolean isVoucher, String begin_time, String end_time, String time_alive, String point, String number, String sale, int sale_type) {
        if (URL_BANNER == null) {
            view.showShortToast(-1, view.getActivity().getString(R.string.error_input_banner));
            return;
        }
        if (!validateText(title)) {
            view.showShortToast(0, view.getActivity().getString(R.string.error_input_title));
            return;
        }

        if (isVoucher) {
            if (!validateText(begin_time)) {
                view.showShortToast(-1, view.getActivity().getString(R.string.error_input_begin_time));
                return;
            }
            if (!validateText(end_time)) {
                view.showShortToast(-1, view.getActivity().getString(R.string.error_input_end_time));
                return;
            }
            if (!validateText(time_alive)) {
                view.showShortToast(-1, view.getActivity().getString(R.string.error_input_alive_time));
                return;
            }
            if (validateDouble(point) <= 0) {
                view.showShortToast(3, view.getActivity().getString(R.string.error_input_exchange_point));
                return;
            }
            if (validateInteger(number) <= 0) {
                view.showShortToast(1, view.getActivity().getString(R.string.error_input_number_of_voucher));
                return;
            }
            if (validateDouble(sale) <= 0) {
                view.showShortToast(2, view.getActivity().getString(R.string.error_input_sale));
                return;
            }
        }

        resp_news.setId(news.getId());
        resp_news.setNews_type((news_type + 1));
        resp_news.setBanner(URL_BANNER);
        resp_news.setDescription(des);
        resp_news.setTitle(title);
        resp_news.setIs_public(isPublic);

        if (isVoucher) {
            Voucher voucher = new Voucher();
            voucher.setBegin_time(Constants.convertDataToLong(begin_time));
            voucher.setFinish_time(Constants.convertDataToLong(end_time));
            voucher.setTime_alive(((long) (Integer.parseInt(time_alive) * 60)));
            voucher.setNumber_of_voucher(Integer.parseInt(number));
            voucher.setSales(Double.parseDouble(sale));
            voucher.setSales_type((sale_type + 1));
            voucher.setPoint(Integer.parseInt(point));

            resp_news.setVoucher(voucher);
        }

        view.showProgressBar(false, false, null, view.getActivity().getString(R.string.doing_add_news));
        iCmd.execute(2, JsonHelper.toJson(resp_news));
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