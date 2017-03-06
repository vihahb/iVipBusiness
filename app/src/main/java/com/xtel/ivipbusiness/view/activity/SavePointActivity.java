package com.xtel.ivipbusiness.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.presenter.SavePointPresenter;
import com.xtel.ivipbusiness.view.activity.inf.ISavePointView;
import com.xtel.ivipbusiness.view.activity.inf.RESP_Member_Info;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.xtel.sdk.callback.DialogListener;
import com.xtel.sdk.utils.NetWorkInfo;
import com.xtel.sdk.utils.WidgetHelper;

import java.io.File;
import java.io.IOException;

public class SavePointActivity extends BasicActivity implements ISavePointView {
    private SavePointPresenter presenter;
    private CallbackManager callbackManager;

    private ImageView img_avatar, img_bill;
    private TextView txt_fullname, txt_level;
    private EditText edt_total_money, edt_bill_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_point);
        callbackManager = CallbackManager.create(this);

        presenter = new SavePointPresenter(this);
        initToolbar(R.id.save_point_toolbar, null);
        initView();
        initListener();
        presenter.getData();
    }

    protected void initView() {
        img_avatar = findImageView(R.id.save_point_img_avatar);
        img_bill = findImageView(R.id.save_point_img_bill);

        txt_fullname = findTextView(R.id.save_point_txt_fullname);
        txt_level = findTextView(R.id.save_point_txt_level);
        edt_total_money = findEditText(R.id.save_point_edt_total_money);
        edt_bill_code = findEditText(R.id.save_point_edt_code_bill);
    }

    protected void initListener() {
        img_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.takePicture();
            }
        });
    }









    @Override
    public void onGetDataSuccess(RESP_Member_Info resp_member_info) {
        WidgetHelper.getInstance().setAvatarImageURL(img_avatar, resp_member_info.getAvatar());
        WidgetHelper.getInstance().setTextViewWithResult(txt_fullname, resp_member_info.getFull_name(), getString(R.string.updating));
        WidgetHelper.getInstance().setTextViewWithResult(txt_level, resp_member_info.getLevel_name(), getString(R.string.updating));
    }

    @Override
    public void onGetDataError() {
        showMaterialDialog(false, false, null, getString(R.string.error_try_again), null, getString(R.string.back), new DialogListener() {
            @Override
            public void negativeClicked() {

            }

            @Override
            public void positiveClicked() {
                closeDialog();
                finish();
            }
        });
    }

    @Override
    public void onTakePictureGallary(Uri uri) {
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
            presenter.postImage(bitmap);
        }
    }

    @Override
    public void onTakePictureCamera(Bitmap bitmap) {
        if (!NetWorkInfo.isOnline(this)) {
            showShortToast(getString(R.string.error_no_internet));
            return;
        } else if (bitmap == null) {
            showShortToast(getString(R.string.error_get_image));
            return;
        }

        showProgressBar(false, false, null, getString(R.string.uploading_file));
        presenter.postImage(bitmap);
    }

    @Override
    public void onLoadPicture(File url) {
        closeProgressBar();
        WidgetHelper.getInstance().setAvatarImageFile(img_bill, url);
    }

    @Override
    public void onSavePointSuccess(String member_name, int total_point) {
        closeProgressBar();
        String noti = getString(R.string.success_save_point_name) + " " + member_name + " " + getString(R.string.success_save_point_addition) + " " + total_point + " " + getString(R.string.success_save_point_point);
        showMaterialDialog(false, false, null, noti, null, getString(R.string.ok), new DialogListener() {
            @Override
            public void negativeClicked() {

            }

            @Override
            public void positiveClicked() {
                closeDialog();
                finish();
            }
        });
    }

    @Override
    public void getNewSession(final ICmd iCmd, final Object... params) {
        callbackManager.getNewSesion(new CallbacListener() {
            @Override
            public void onSuccess(RESP_Login success) {
                iCmd.execute(params);
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
    public void onRequestError(Error error) {
        closeProgressBar();

        if (error.getCode() == 401)
            showShortToast(getString(R.string.error_store_do_not_setting_point));
        else
            showShortToast(JsonParse.getCodeMessage(error.getCode(), getString(R.string.error_try_again)));
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
    public void showShortToast(int type, String message) {
        showShortToast(message);
        if (type == 1)
            edt_total_money.requestFocus();
        else if (type == 2)
            edt_bill_code.requestFocus();
    }

    @Override
    public Activity getActivity() {
        return this;
    }








    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save_point, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home)
            finish();
        else if (id == R.id.action_save_point_done)
            presenter.savePoint(edt_total_money.getText().toString(), edt_bill_code.getText().toString());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callbackManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }
}