package com.xtel.ivipbusiness.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.presenter.AddStorePresenter;
import com.xtel.ivipbusiness.view.activity.inf.IAddStoreView;

public class AddStoreActivity extends BasicActivity implements View.OnClickListener, IAddStoreView {
    private AddStorePresenter presenter;

    private ImageView img_banner, img_avatar;
    private EditText edt_name, edt_type, edt_address, edt_phone, edt_des;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_store);

        presenter = new AddStorePresenter(this);
        initToolbar(R.id.add_store_toolbar, null);
        initView();
        initListener();
    }

    private void initView() {
        img_banner = findImageView(R.id.add_store_img_banner);
        img_avatar = findImageView(R.id.add_store_img_avatar);
        edt_name = findEditText(R.id.add_store_edt_name);
        edt_type = findEditText(R.id.add_store_edt_type);
        edt_address = findEditText(R.id.add_store_edt_address);
        edt_phone = findEditText(R.id.add_store_edt_phone);
        edt_des = findEditText(R.id.add_store_edt_des);
    }

    private void initListener() {
        img_banner.setOnClickListener(this);
        img_avatar.setOnClickListener(this);
    }



    @Override
    public void onTakePictureGallary(int type, Uri uri) {
        if (type == 0)
            img_banner.setImageURI(uri);
        else
            img_avatar.setImageURI(uri);
    }

    @Override
    public void onTakePictureCamera(int type, Bitmap bitmap) {
        if (type == 0)
            img_banner.setImageBitmap(bitmap);
        else
            img_avatar.setImageBitmap(bitmap);
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }
}