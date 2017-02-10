package com.xtel.ivipbusiness.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.PlaceModel;
import com.xtel.ivipbusiness.presenter.AddStorePresenter;
import com.xtel.ivipbusiness.view.activity.inf.IAddStoreView;
import com.xtel.ivipbusiness.view.adapter.TypeAdapter;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;
import com.xtel.nipservicesdk.utils.PermissionHelper;
import com.xtel.sdk.callback.DialogListener;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.NetWorkInfo;
import com.xtel.sdk.utils.WidgetHelper;

import java.io.IOException;
import java.util.Calendar;

public class AddStoreActivity extends BasicActivity implements View.OnClickListener, IAddStoreView {
    private AddStorePresenter presenter;

    private ImageView img_banner, img_logo;
    private Spinner sp_type;
    private EditText edt_begin_time, edt_end_time, edt_name, edt_address, edt_phone, edt_des;

    private final int REQUEST_LOCATION = 99;
    private PlaceModel placeModel;
    private String BEGIN_TIME, END_TIME;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_store);
        callbackManager = CallbackManager.create(this);

        presenter = new AddStorePresenter(this);
        presenter.getData();

        initToolbar(R.id.add_store_toolbar, null);
        initView();
        initType();
        initListener();
    }

    private void initView() {
        img_banner = findImageView(R.id.add_store_img_banner);
        img_logo = findImageView(R.id.add_store_img_avatar);
        edt_begin_time = findEditText(R.id.add_store_edt_begin_time);
        edt_end_time = findEditText(R.id.add_store_edt_end_time);
        edt_name = findEditText(R.id.add_store_edt_name);
        edt_address = findEditText(R.id.add_store_edt_address);
        edt_phone = findEditText(R.id.add_store_edt_phone);
        edt_des = findEditText(R.id.add_store_edt_des);
    }

    private void initType() {
        sp_type = findSpinner(R.id.add_store_sp_type);
        TypeAdapter typeAdapter = new TypeAdapter(this);
        sp_type.setAdapter(typeAdapter);
    }

    private void initListener() {
        img_banner.setOnClickListener(this);
        img_logo.setOnClickListener(this);
        edt_address.setOnClickListener(this);
        edt_begin_time.setOnClickListener(this);
        edt_end_time.setOnClickListener(this);
    }

    private void selectBeginTime() {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this, R.style.AppCompatAlertDialogStyle, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                WidgetHelper.getInstance().setEditTextTime(edt_begin_time, getString(R.string.begin_time) + ": ", hourOfDay, minute);
                BEGIN_TIME = hourOfDay + ":" + minute;
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    private void selectEndTime() {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this, R.style.AppCompatAlertDialogStyle, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                WidgetHelper.getInstance().setEditTextTime(edt_end_time, getString(R.string.end_time) + ": ", hourOfDay, minute);
                END_TIME = hourOfDay + ":" + minute;
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }















    @Override
    public void onGetDataError() {
        showMaterialDialog(false, false, null, getString(R.string.error_try_again), null, getString(R.string.back), new DialogListener() {
            @Override
            public void onClicked(Object object) {
                closeDialog();
                finish();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public void onTakePictureGallary(int type, Uri uri) {
        if (!NetWorkInfo.isOnline(this)) {
            showShortToast(getString(R.string.error_no_internet));
            return;
        } else if (uri == null) {
            showShortToast(getString(R.string.error_get_image));
            return;
        }

        showProgressBar(false, false, null, getString(R.string.uploading_file));

        Bitmap bitmap = null;

        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bitmap != null) {
            presenter.postImage(bitmap, type);
        }
    }

    @Override
    public void onTakePictureCamera(int type, Bitmap bitmap) {
        if (!NetWorkInfo.isOnline(this)) {
            showShortToast(getString(R.string.error_no_internet));
            return;
        } else if (bitmap == null) {
            showShortToast(getString(R.string.error_get_image));
            return;
        }

        showProgressBar(false, false, null, getString(R.string.uploading_file));
        presenter.postImage(bitmap, type);
    }

    @Override
    public void onLoadPicture(String url, int type) {
        closeProgressBar();

        Log.e("url_success_3", url);
        if (type == 0)
            WidgetHelper.getInstance().setImageURL(img_banner, url);
        else
            WidgetHelper.getInstance().setSmallImageURL(img_logo, url);
        Log.e("url_success_4", url);
    }

    @Override
    public void onAddStoreSuccess() {
        showShortToast(getString(R.string.success_add_store));
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
    public void getNewSession(final ICmd iCmd) {
        callbackManager.getNewSesion(new CallbacListener() {
            @Override
            public void onSuccess(RESP_Login success) {
                iCmd.execute();
            }

            @Override
            public void onError(Error error) {
                closeProgressBar();
                showShortToast(getString(R.string.error_end_of_session));
                getActivity().finishAffinity();
                startActivity(LoginActivity.class);
            }
        });
    }

    @Override
    public void showShortToast(String message) {
        closeProgressBar();
        super.showShortToast(message);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.add_store_img_banner)
            presenter.takePicture(0);
        else if (id == R.id.add_store_img_avatar)
            presenter.takePicture(1);
        else if (id == R.id.add_store_edt_begin_time)
            selectBeginTime();
        else if (id == R.id.add_store_edt_end_time)
            selectEndTime();
        else if (id == R.id.add_store_edt_address) {
            if (PermissionHelper.checkOnlyPermission(Manifest.permission.ACCESS_FINE_LOCATION, this, REQUEST_LOCATION))
                startActivityForResult(ChooseMapsActivity.class, Constants.MODEL, placeModel, REQUEST_LOCATION);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_store, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home)
            finish();
        else if (id == R.id.action_add_store_done)
            presenter.addStore(edt_name.getText().toString(), placeModel, edt_phone.getText().toString(), edt_des.getText().toString(), BEGIN_TIME, END_TIME, sp_type.getSelectedItemPosition());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callbackManager.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startActivityForResult(ChooseMapsActivity.class, Constants.MODEL, placeModel, REQUEST_LOCATION);
            else
                showShortToast(getString(R.string.error_permission));
        } else
            presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOCATION && resultCode == RESULT_OK) {
            if (data != null) {
                placeModel = (PlaceModel) data.getSerializableExtra(Constants.MODEL);
                edt_address.setText(placeModel.getAddress());
            }
        } else
            presenter.onActivityResult(requestCode, resultCode, data);
    }
}