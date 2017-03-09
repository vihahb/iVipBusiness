package com.xtel.ivipbusiness.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.PlaceModel;
import com.xtel.ivipbusiness.model.entity.RESP_Store;
import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.ivipbusiness.presenter.StoreInfoPresenter;
import com.xtel.ivipbusiness.view.activity.ChooseMapsActivity;
import com.xtel.ivipbusiness.view.activity.LoginActivity;
import com.xtel.ivipbusiness.view.activity.ViewStoreActivity;
import com.xtel.ivipbusiness.view.fragment.inf.IStoreInfoView;
import com.xtel.ivipbusiness.view.widget.AppBarStateChangeListener;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;
import com.xtel.nipservicesdk.utils.PermissionHelper;
import com.xtel.sdk.callback.DialogListener;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.WidgetHelper;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by Vulcl on 1/16/2017
 */

public class StoreInfoFragment extends BasicFragment implements View.OnClickListener, IStoreInfoView {
    private StoreInfoPresenter presenter;

    private ImageView img_banner, img_logo, img_qr_code, img_bar_code;
    private ImageButton img_camera, img_location;
    private EditText edt_begin_time, edt_end_time, edt_name, edt_address, edt_phone, edt_des;

    private SwipeRefreshLayout swipeRefreshLayout;
    private CallbackManager callbackManager;

    private RESP_Store resp_store;
    //    private PlaceModel placeModel;
    private final int REQUEST_LOCATION = 99;
    private boolean isShow = true;

    public static StoreInfoFragment newInstance() {
        return new StoreInfoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callbackManager = CallbackManager.create(getActivity());

        presenter = new StoreInfoPresenter(this);
        initSwwipe();
        initView(view);
        initListener();
        setEnableView(false);
//        initAnimationHideImage(view);
        presenter.getData();
    }

    //    Khởi tạo swipeRefreshLayout để hiển thị load thông tin
    private void initSwwipe() {
        swipeRefreshLayout = findSwipeRefreshLayout(R.id.store_info_swipe);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
    }

    private void initView(View view) {
        img_banner = findImageView(R.id.store_info_img_banner);
        img_logo = findImageView(R.id.store_info_img_logo);
        img_qr_code = findImageView(R.id.store_info_img_qrCode);
        img_bar_code = findImageView(R.id.store_info_img_bar_code);

        img_camera = findImageButton(R.id.store_info_img_camera);
        img_location = findImageButton(R.id.store_info_img_location);

        edt_begin_time = findEditText(R.id.store_info_edt_begin_time);
        edt_end_time = findEditText(R.id.store_info_edt_end_time);
        edt_name = findEditText(R.id.store_info_edt_fullname);
        edt_address = findEditText(R.id.store_info_edt_address);
        edt_phone = findEditText(R.id.store_info_edt_phone);
        edt_des = findEditText(R.id.store_info_edt_des);
    }

    private void initListener() {
        img_qr_code.setOnClickListener(this);
        img_bar_code.setOnClickListener(this);
        img_camera.setOnClickListener(this);
        img_logo.setOnClickListener(this);

        edt_begin_time.setOnClickListener(this);
        edt_end_time.setOnClickListener(this);
        img_location.setOnClickListener(this);
    }

    public void setEnableView(boolean isEnable) {
        edt_begin_time.setEnabled(isEnable);
        edt_end_time.setEnabled(isEnable);
        edt_name.setEnabled(isEnable);
        edt_address.setEnabled(isEnable);
        edt_phone.setEnabled(isEnable);
        edt_des.setEnabled(isEnable);

        img_camera.setEnabled(isEnable);
        img_location.setEnabled(isEnable);
        img_banner.setEnabled(isEnable);
        img_logo.setEnabled(isEnable);

        if (isEnable)
            edt_des.setBackground(getActivity().getResources().getDrawable(R.drawable.edittext_des));
        else
            edt_des.setBackground(getActivity().getResources().getDrawable(R.drawable.edittext_des_disable));
    }

//    private void initAnimationHideImage(View view) {
//        AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.store_info_app_bar);
//        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
//            @Override
//            public void onStateEXPANDED() {
//                isShow = true;
//                showFloatingActionButton(layout_logo);
//            }
//
//            @Override
//            public void onStateIDLE() {
//                isShow = false;
//                hideFloatingActionButton(layout_logo);
//            }
//        });
//    }

    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();

    private void hideFloatingActionButton(View view) {
        debug("hide");
        ViewCompat.animate(view).scaleX(0.0F).scaleY(0.0F).alpha(0.0F).setInterpolator(INTERPOLATOR).withLayer()
                .setListener(new ViewPropertyAnimatorListener() {
                    public void onAnimationStart(View view) {
                    }

                    public void onAnimationCancel(View view) {
                    }

                    public void onAnimationEnd(View view) {
                        if (!isShow)
                            view.setVisibility(View.GONE);
                    }
                }).start();
    }

    private void showFloatingActionButton(View view) {
        debug("show");
        view.setVisibility(View.VISIBLE);
        ViewCompat.animate(view).scaleX(1.0F).scaleY(1.0F).alpha(1.0F).setInterpolator(INTERPOLATOR).withLayer().setListener(null).start();
    }

    private void selectBeginTime() {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(getContext(), R.style.AppCompatAlertDialogStyle, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                WidgetHelper.getInstance().setEditTextTime(edt_begin_time, getString(R.string.open_time) + ": ", hourOfDay, minute);
//                String BEGIN_TIME = hourOfDay + ":" + minute;
                resp_store.setBegin_time(Constants.convertTimeToLong(hourOfDay, minute));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    private void selectEndTime() {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(getContext(), R.style.AppCompatAlertDialogStyle, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                WidgetHelper.getInstance().setEditTextTime(edt_end_time, getString(R.string.close_time) + ": ", hourOfDay, minute);
//                String END_TIME = hourOfDay + ":" + minute;
                resp_store.setEnd_time(Constants.convertTimeToLong(hourOfDay, minute));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    @SuppressWarnings("ConstantConditions")
    private void showQrCode() {
        if (resp_store.getQr_code() == null || resp_store.getQr_code().isEmpty()) {
            showShortToast(getString(R.string.error_view_code));
            return;
        }

        final Dialog bottomSheetDialog = new Dialog(getContext(), R.style.MaterialDialogSheet);
        bottomSheetDialog.setContentView(R.layout.qr_code_bottom_sheet);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bottomSheetDialog.getWindow().setGravity(Gravity.CENTER);

        Button txt_close = (Button) bottomSheetDialog.findViewById(R.id.dialog_txt_close);
        ImageView img_qr_code = (ImageView) bottomSheetDialog.findViewById(R.id.dialog_qr_code);

        WidgetHelper.getInstance().setImageURL(img_qr_code, resp_store.getQr_code());

        if (txt_close != null)
            txt_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                }
            });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bottomSheetDialog.show();
            }
        }, 200);
    }

    @SuppressWarnings("ConstantConditions")
    private void showBarCode() {
        if (resp_store.getBar_code() == null || resp_store.getBar_code().isEmpty()) {
            showShortToast(getString(R.string.error_view_code));
            return;
        }

        final Dialog bottomSheetDialog = new Dialog(getContext(), R.style.MaterialDialogSheet);
        bottomSheetDialog.setContentView(R.layout.qr_code_bottom_sheet);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bottomSheetDialog.getWindow().setGravity(Gravity.CENTER);

        Button txt_close = (Button) bottomSheetDialog.findViewById(R.id.dialog_txt_close);
        ImageView img_qr_code = (ImageView) bottomSheetDialog.findViewById(R.id.dialog_qr_code);

        WidgetHelper.getInstance().setImageURL(img_qr_code, resp_store.getBar_code());

        if (txt_close != null)
            txt_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                }
            });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bottomSheetDialog.show();
            }
        }, 200);
    }

    public void updateStore() {
        resp_store.setName(edt_name.getText().toString());
        resp_store.setPhonenumber(edt_phone.getText().toString());
        resp_store.setDescription(edt_des.getText().toString());

        presenter.updateStore(resp_store);
    }

    private void setAddress(PlaceModel placeModel) {
        edt_address.setText(placeModel.getAddress());

        resp_store.setAddress(placeModel.getAddress());
        resp_store.setLocation_lat(placeModel.getLatitude());
        resp_store.setLocation_lng(placeModel.getLongtitude());
    }

    public void enableToEdit() {
        if (swipeRefreshLayout.isRefreshing())
            return;

        setEnableView(true);
        ((ViewStoreActivity) getActivity()).changeMenuIcon(R.drawable.ic_action_done_2);
    }














    @Override
    public void onGetDataError() {
        showMaterialDialog(false, false, null, getString(R.string.error_try_again), null, getString(R.string.back), new DialogListener() {
            @Override
            public void negativeClicked() {
                closeDialog();
                getActivity().finish();
            }

            @Override
            public void positiveClicked() {
                closeDialog();
            }
        });
    }

    @Override
    public void onGetStoreInfoSuccess(RESP_Store resp_store) {
        this.resp_store = resp_store;
        WidgetHelper.getInstance().setImageURL(img_banner, resp_store.getBanner());
        WidgetHelper.getInstance().setAvatarImageURL(img_logo, resp_store.getLogo());
        WidgetHelper.getInstance().setImageURL(img_qr_code, resp_store.getQr_code());
        WidgetHelper.getInstance().setImageURL(img_bar_code, resp_store.getBar_code());

        WidgetHelper.getInstance().setEditTextTime(edt_begin_time, (getString(R.string.open_time) + ": "), (resp_store.getBegin_time() * 1000));
        WidgetHelper.getInstance().setEditTextTime(edt_end_time, (getString(R.string.close_time) + ": "), (resp_store.getEnd_time() * 1000));
        WidgetHelper.getInstance().setEditTextNoResult(edt_name, resp_store.getName());
        WidgetHelper.getInstance().setEditTextNoResult(edt_address, resp_store.getAddress());
        WidgetHelper.getInstance().setEditTextNoResult(edt_phone, resp_store.getPhonenumber());
        WidgetHelper.getInstance().setEditTextNoResult(edt_des, resp_store.getDescription());

        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(false);
    }

    @Override
    public void onUpdateStoreInfoSuccess() {
        ((ViewStoreActivity) getActivity()).changeMenuIcon(R.drawable.ic_action_edit_line);
        setEnableView(false);

        ((ViewStoreActivity) getActivity()).setResp_store(resp_store);
        ((ViewStoreActivity) getActivity()).onUpdateStoreSuccess();
        onGetStoreInfoSuccess(resp_store);
        closeProgressBar();

        showMaterialDialog(false, false, null, getString(R.string.success_update_store), null, getString(R.string.back), new DialogListener() {
            @Override
            public void negativeClicked() {
                closeDialog();
            }

            @Override
            public void positiveClicked() {
                closeDialog();
            }
        });
    }

    @Override
    public void onGetStoreInfoError() {
        showMaterialDialog(false, false, null, getString(R.string.can_not_load_data), null, getString(R.string.back), new DialogListener() {
            @Override
            public void negativeClicked() {
                closeDialog();
                getActivity().finish();
            }

            @Override
            public void positiveClicked() {
                closeDialog();
                getActivity().finish();
            }
        });
    }

    @Override
    public void startActivityForResult(Class clazz, String key, Object object, int requestCode) {
        super.startActivityForResult(clazz, key, object, requestCode);
    }


    @Override
    public void onTakePictureGallary(int type, Uri uri) {
        if (uri == null) {
            showShortToast(getString(R.string.error_get_image));
            return;
        }

        Log.e("111111111", "11111");
        showProgressBar(false, false, null, getString(R.string.uploading_file));

        Bitmap bitmap = null;

        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bitmap != null) {
            presenter.postImage(bitmap, type);
        } else
            closeProgressBar();
    }

    @Override
    public void onTakePictureCamera(int type, Bitmap bitmap) {
        if (bitmap == null) {
            showShortToast(getString(R.string.error_get_image));
            return;
        }

        showProgressBar(false, false, null, getString(R.string.uploading_file));
        presenter.postImage(bitmap, type);
    }

    @Override
    public void onLoadPicture(File file, int type) {
        closeProgressBar();

        if (type == 0)
            WidgetHelper.getInstance().setImageFile(img_banner, file);
        else
            WidgetHelper.getInstance().setAvatarImageFile(img_logo, file);
    }

    @Override
    public void getNewSession(final ICmd iCmd) {
        callbackManager.getNewSesion(new CallbacListener() {
            @Override
            public void onSuccess(RESP_Login success) {
                iCmd.execute();
            }

            @Override
            public void onError(Error error) {
                showShortToast(getString(R.string.error_end_of_session));
                getActivity().finishAffinity();
                startActivity(LoginActivity.class);
            }
        });
    }

    @Override
    public void onValidateError(String error) {
        showShortToast(error);
    }

    @Override
    public void showProgressBar(boolean isTouchOutside, boolean isCancel, String title, String message) {
        super.showProgressBar(isTouchOutside, isCancel, title, message);
    }

    @Override
    public void closeProgressBar() {
        super.closeProgressBar();
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.store_info_img_qrCode)
            showQrCode();
        else if (id == R.id.store_info_img_bar_code)
            showBarCode();
        else if (id == R.id.store_info_img_camera) {
            if (!swipeRefreshLayout.isRefreshing())
                presenter.takePicture(0);
        } else if (id == R.id.store_info_img_logo) {
            if (!swipeRefreshLayout.isRefreshing())
                presenter.takePicture(1);
        } else if (id == R.id.store_info_edt_begin_time)
            selectBeginTime();
        else if (id == R.id.store_info_edt_end_time)
            selectEndTime();
        else if (id == R.id.store_info_img_location) {
            if (PermissionHelper.checkOnlyPermission(Manifest.permission.ACCESS_FINE_LOCATION, getActivity(), REQUEST_LOCATION)) {
                PlaceModel placeModel = new PlaceModel();
                placeModel.setAddress(resp_store.getAddress());
                placeModel.setLatitude(resp_store.getLocation_lat());
                placeModel.setLongtitude(resp_store.getLocation_lng());

                startActivityForResult(ChooseMapsActivity.class, Constants.MODEL, placeModel, REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onDestroy() {
        presenter.setExists(false);
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callbackManager.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PlaceModel placeModel = new PlaceModel();
                placeModel.setAddress(resp_store.getAddress());
                placeModel.setLatitude(resp_store.getLocation_lat());
                placeModel.setLongtitude(resp_store.getLocation_lng());

                startActivityForResult(ChooseMapsActivity.class, Constants.MODEL, placeModel, REQUEST_LOCATION);
            } else
                showShortToast(getString(R.string.error_permission));
        } else
            presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        debug(requestCode + "   " + resultCode);

        if (requestCode == REQUEST_LOCATION && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                PlaceModel placeModel = (PlaceModel) data.getSerializableExtra(Constants.MODEL);
                setAddress(placeModel);
            }
        } else
            presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}