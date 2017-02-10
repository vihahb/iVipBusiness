package com.xtel.ivipbusiness.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.PlaceModel;
import com.xtel.ivipbusiness.model.entity.Type;
import com.xtel.ivipbusiness.presenter.AddStorePresenter;
import com.xtel.ivipbusiness.view.activity.inf.IAddStoreView;
import com.xtel.ivipbusiness.view.adapter.TypeAdapter;
import com.xtel.nipservicesdk.utils.PermissionHelper;
import com.xtel.sdk.callback.DialogListener;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.NetWorkInfo;
import com.xtel.sdk.utils.WidgetHelper;

import java.io.IOException;
import java.util.ArrayList;

public class AddStoreActivity extends BasicActivity implements View.OnClickListener, IAddStoreView {
    private AddStorePresenter presenter;

    private ImageView img_banner, img_logo;
    private Spinner sp_type;
    private EditText edt_name, edt_address, edt_phone, edt_des;

    private final int REQUEST_LOCATION = 99;
    private PlaceModel placeModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_store);

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

        if (type == 0)
            WidgetHelper.getInstance().setImageURL(img_banner, url);
        else
            WidgetHelper.getInstance().setImageURL(img_logo, url);
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
        else if (id == R.id.add_store_edt_address) {
            if (PermissionHelper.checkOnlyPermission(Manifest.permission.ACCESS_FINE_LOCATION, this, REQUEST_LOCATION))
                startActivityForResult(ChooseMapsActivity.class, Constants.MODEL, placeModel, REQUEST_LOCATION);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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