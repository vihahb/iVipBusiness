package com.xtel.ivipbusiness.view.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.RESP_Full_Profile;
import com.xtel.ivipbusiness.presenter.ProfilePresenter;
import com.xtel.ivipbusiness.view.activity.inf.IProfileView;
import com.xtel.nipservicesdk.LoginManager;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.sdk.callback.DialogListener;
import com.xtel.sdk.utils.WidgetHelper;

import java.io.IOException;
import java.util.Calendar;

public class ProfileActivity extends BasicActivity implements View.OnClickListener, IProfileView {
    private ProfilePresenter presenter;

    private ImageView img_avatar, img_banner;
    private TextView txt_total_stores, txt_date_create;
    private EditText edt_fullname, edt_birthday, edt_phone, edt_email, edt_address;
    private Spinner sp_gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        presenter = new ProfilePresenter(this);
        initToolbar(R.id.profile_toolbar, null);

        initView();
        initSpinner();
        initListener();
        initLogout();

        presenter.getProfile();
    }

//    Khởi tạo view
    private void initView() {
        img_avatar = findImageView(R.id.profile_img_avatar);
        img_banner = findImageView(R.id.profile_img_banner);
        txt_total_stores = findTextView(R.id.profile_txt_total_store);
        txt_date_create = findTextView(R.id.profile_txt_date_create);

        edt_fullname = findEditText(R.id.profile_edt_fullname);
        edt_birthday = findEditText(R.id.profile_edt_birth_day);
        edt_phone = findEditText(R.id.profile_edt_phone);
        edt_email = findEditText(R.id.profile_edt_email);
        edt_address = findEditText(R.id.profile_edt_address);
    }

    private void initSpinner() {
        sp_gender = findSpinner(R.id.profile_sp_gender);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.item_spinner_normal, getResources().getStringArray(R.array.gender));
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown_item);
        sp_gender.setAdapter(adapter);
    }

    private void initListener() {
        edt_birthday.setOnClickListener(this);
        img_banner.setOnClickListener(this);
        img_avatar.setOnClickListener(this);
    }

//    Khởi tạo sự kiện logout
    private void initLogout() {
        Button btn_logout = (findButton(R.id.profile_btn_logout));
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.logOut();
                finishAffinity();
                startActivityAndFinish(LoginActivity.class);
            }
        });
    }

    private void selectDate() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, R.style.AppCompatAlertDialogStyle, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                WidgetHelper.getInstance().setEditTextBirthday(edt_birthday, dayOfMonth, month, year);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_edt_birth_day:
                selectDate();
                break;
            case R.id.profile_img_banner:
                presenter.takePicture(0);
                break;
            case R.id.profile_img_avatar:
                presenter.takePicture(1);
                break;
            default:
                break;
        }
    }

//    Hiển thị thông tin của người dùng khi load dữ liệu thành công
    @Override
    public void onGetProfileSuccess(RESP_Full_Profile obj) {
        WidgetHelper.getInstance().setImageURL(img_avatar, obj.getAvatar());

        WidgetHelper.getInstance().setTextViewNoResult(txt_total_stores, getString(R.string.store_number) + " " + obj.getStore_number());
        WidgetHelper.getInstance().setTextViewBirthday(txt_date_create, getString(R.string.day_create), obj.getJoin_date());

        WidgetHelper.getInstance().setEditTextNoResult(edt_fullname, obj.getFullname());
        WidgetHelper.getInstance().setEditTextBirthday(edt_birthday, obj.getBirthday());
        WidgetHelper.getInstance().setEditTextNoResult(edt_phone, obj.getPhonenumber());
        WidgetHelper.getInstance().setEditTextNoResult(edt_email, obj.getEmail());
        WidgetHelper.getInstance().setEditTextNoResult(edt_address, obj.getAddress());
        WidgetHelper.getInstance().setSpinnerGender(sp_gender, obj.getGender());

    }

    @Override
    public void onGetProfileError(Error error) {
//        showMaterialDialog(false, false, null, getString(R.string.can_not_load_data), null, getString(R.string.back), new DialogListener() {
//            @Override
//            public void onClicked(Object object) {
//                closeDialog();
//                finish();
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//        });
    }

    @Override
    public void onTakePictureGallary(int type, Uri uri) {
        if (uri == null) {
            showShortToast(getString(R.string.error_get_image));
            return;
        }

        Bitmap bitmap = null;

        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bitmap != null) {
            if (type == 0)
                img_banner.setImageBitmap(bitmap);
            else
                img_avatar.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onTakePictureCamera(int type, Bitmap bitmap) {
        if (bitmap == null) {
            showShortToast(getString(R.string.error_get_image));
            return;
        }

        if (type == 0)
            img_banner.setImageBitmap(bitmap);
        else
            img_avatar.setImageBitmap(bitmap);
    }

    @Override
    public void onValidateError(String error) {
        showShortToast(error);
    }

    @Override
    public void onNoInternet() {
        showShortToast(getString(R.string.error_no_internet));
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.profile_action_edit:
                presenter.updateUser("abc", edt_fullname.getText().toString(), sp_gender.getSelectedItemPosition(), edt_birthday.getText().toString(),
                        edt_phone.getText().toString(), edt_email.getText().toString(), edt_address.getText().toString());
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }
}
