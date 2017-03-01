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
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class AddStoreActivity extends BasicActivity implements View.OnClickListener, IAddStoreView {
    private AddStorePresenter presenter;
    private CallbackManager callbackManager;

    private ImageView img_banner, img_logo;
    private ImageButton img_camera, img_location;
    private Spinner sp_type;
    private EditText edt_begin_time, edt_end_time, edt_name, edt_address, edt_phone, edt_des;

    private ActionBar actionBar;
    private final int REQUEST_LOCATION = 99;
    private PlaceModel placeModel;
//    private String BEGIN_TIME, END_TIME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_store);
        callbackManager = CallbackManager.create(this);

        presenter = new AddStorePresenter(this);
//        initToolbar(R.id.add_store_toolbar, null);
        initToolbar();
        initView();
        initType();
        initListener();
        presenter.getData();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_store_toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        img_banner = findImageView(R.id.add_store_img_banner);
        img_logo = findImageView(R.id.add_store_img_avatar);

        img_camera = findImageButton(R.id.add_store_img_camera);
        img_location = findImageButton(R.id.add_store_img_location);

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
        img_camera.setOnClickListener(this);
        img_logo.setOnClickListener(this);
        img_location.setOnClickListener(this);
        edt_begin_time.setOnClickListener(this);
        edt_end_time.setOnClickListener(this);
    }

    private void selectTime(final int type) {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this, R.style.AppCompatAlertDialogStyle, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (type == 0) {
                    WidgetHelper.getInstance().setEditTextTime(edt_begin_time, hourOfDay, minute);
//                    BEGIN_TIME = hourOfDay + ":" + minute;
                } else {
                    WidgetHelper.getInstance().setEditTextTime(edt_end_time, hourOfDay, minute);
//                    END_TIME = hourOfDay + ":" + minute;
                }
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    @Override
    public void onGetDataChain() {
        WidgetHelper.getInstance().setImageResource(img_logo, R.mipmap.ic_logo_chain);
        actionBar.setTitle(getString(R.string.title_activity_add_chain));
        edt_name.setHint(getString(R.string.chain_name));
        edt_address.setHint(getString(R.string.chain_address));
        edt_des.setHint(getString(R.string.chain_des));
    }

    @Override
    public void onGetDataError() {
        closeProgressBar();
        showMaterialDialog(false, false, null, getString(R.string.error_try_again), null, getString(R.string.back), new DialogListener() {
            @Override
            public void negativeClicked() {
                closeDialog();
                finish();
            }

            @Override
            public void positiveClicked() {
                closeDialog();
                finish();
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
    public void onLoadPicture(File file, int type) {
        closeProgressBar();

        if (type == 0)
            WidgetHelper.getInstance().setImageFile(img_banner, file);
        else
            WidgetHelper.getInstance().setAvatarImageFile(img_logo, file);
    }

    @Override
    public void onAddChainSuccess() {
        showMaterialDialog(false, false, null, getString(R.string.success_add_chain), null, getString(R.string.back), new DialogListener() {
            @Override
            public void negativeClicked() {
                closeDialog();
                finish();
            }

            @Override
            public void positiveClicked() {
                closeDialog();
                finish();
            }
        });
    }

    @Override
    public void onAddStoreSuccess() {
//        showShortToast(getString(R.string.success_add_store));
        showMaterialDialog(false, false, null, getString(R.string.success_add_store), null, getString(R.string.back), new DialogListener() {
            @Override
            public void negativeClicked() {
                closeDialog();
                finish();
            }

            @Override
            public void positiveClicked() {
                closeDialog();
                finish();
            }
        });
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

        if (id == R.id.add_store_img_camera)
            presenter.takePicture(0);
        else if (id == R.id.add_store_img_avatar)
            presenter.takePicture(1);
        else if (id == R.id.add_store_edt_begin_time)
            selectTime(0);
        else if (id == R.id.add_store_edt_end_time)
            selectTime(1);
        else if (id == R.id.add_store_img_location) {
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
            presenter.addStore(edt_name.getText().toString(), placeModel, edt_phone.getText().toString(), edt_des.getText().toString(), edt_begin_time.getText().toString(),
                    edt_end_time.getText().toString(), (sp_type.getSelectedItemPosition() + 2));
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