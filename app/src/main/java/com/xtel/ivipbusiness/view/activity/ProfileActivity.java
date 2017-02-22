package com.xtel.ivipbusiness.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.PlaceModel;
import com.xtel.ivipbusiness.model.entity.RESP_Full_Profile;
import com.xtel.ivipbusiness.presenter.ProfilePresenter;
import com.xtel.ivipbusiness.view.activity.inf.IProfileView;
import com.xtel.ivipbusiness.view.adapter.SpinnerOneIconAdapter;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.LoginManager;
import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.xtel.nipservicesdk.utils.PermissionHelper;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.WidgetHelper;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class ProfileActivity extends BasicActivity implements View.OnClickListener, IProfileView {
    private ProfilePresenter presenter;
    private CallbackManager callbackManager;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView img_avatar, img_banner;
    private TextView txt_total_stores, txt_date_create;
    private EditText edt_fullname, edt_email, edt_birthday, edt_phone, edt_address;
    private Spinner sp_gender;
    private MenuItem menuItem;

    private final int REQUEST_LOCATION = 99;
    private PlaceModel placeModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        callbackManager = CallbackManager.create(this);

        presenter = new ProfilePresenter(this);
        initToolbar(R.id.profile_toolbar, null);

        initSwwipe();
        initView();
        initGender();
        initListener();
        initLogout();
        setEnableView(false);

        presenter.getProfile();
    }

    //    Khởi tạo swipeRefreshLayout để hiển thị load thông tin
    private void initSwwipe() {
        swipeRefreshLayout = findSwipeRefreshLayout(R.id.profile_view_swipe);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
    }

    //    Khởi tạo view
    private void initView() {
        img_avatar = findImageView(R.id.profile_img_avatar);
        img_banner = findImageView(R.id.profile_img_banner);

        txt_total_stores = findTextView(R.id.profile_txt_total_store);
        txt_date_create = findTextView(R.id.profile_txt_date_create);

        edt_fullname = findEditText(R.id.profile_txt_fullname);
        edt_email = findEditText(R.id.profile_txt_email);
        edt_birthday = findEditText(R.id.profile_edt_birth_day);
        edt_phone = findEditText(R.id.profile_edt_phone);
        edt_address = findEditText(R.id.profile_edt_address);
    }

    //    Khởi tạo spinner để chọn giới tính
    private void initGender() {
        String[] arrayList = getResources().getStringArray(R.array.gender);
        sp_gender = findSpinner(R.id.profile_sp_gender);
        SpinnerOneIconAdapter typeAdapter = new SpinnerOneIconAdapter(this, R.drawable.ic_action_gender, arrayList);
        sp_gender.setAdapter(typeAdapter);
    }

    private void initListener() {
        edt_birthday.setOnClickListener(this);
        edt_address.setOnClickListener(this);

        img_avatar.setOnClickListener(this);
    }

    //    Khởi tạo sự kiện logout
    private void initLogout() {
        Button btn_logout = (findButton(R.id.profile_btn_logout));
        btn_logout.setOnClickListener(this);
    }

    private void selectDate() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, R.style.AppCompatAlertDialogStyle, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                WidgetHelper.getInstance().setEditTextDate(edt_birthday, dayOfMonth, month, year);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void setEnableView(boolean isEnable) {
        if (isEnable && swipeRefreshLayout.isRefreshing())
            return;

        img_avatar.setEnabled(isEnable);
        img_banner.setEnabled(isEnable);

        txt_total_stores.setEnabled(isEnable);
        txt_date_create.setEnabled(isEnable);

        edt_fullname.setEnabled(isEnable);
        edt_email.setEnabled(isEnable);
        edt_birthday.setEnabled(isEnable);
        edt_phone.setEnabled(isEnable);
        edt_address.setEnabled(isEnable);

        sp_gender.setEnabled(isEnable);
    }



























    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_edt_birth_day:
                selectDate();
                break;
            case R.id.profile_img_avatar:
                presenter.takePicture(1);
                break;
            case R.id.profile_btn_logout:
                LoginManager.logOut();
                finishAffinity();
                startActivityAndFinish(LoginActivity.class);
                break;
            case R.id.profile_edt_address:
                if (PermissionHelper.checkOnlyPermission(Manifest.permission.ACCESS_FINE_LOCATION, this, REQUEST_LOCATION))
                    startActivityForResult(ChooseMapsActivity.class, Constants.MODEL, placeModel, REQUEST_LOCATION);
                break;
            default:
                break;
        }
    }

    //    Hiển thị thông tin của người dùng khi load dữ liệu thành công
    @Override
    public void onGetProfileSuccess(RESP_Full_Profile obj) {
        WidgetHelper.getInstance().setAvatarImageURL(img_avatar, obj.getAvatar());
        WidgetHelper.getInstance().setImageURL(img_banner, obj.getBanner());

        WidgetHelper.getInstance().setTextViewNoResult(txt_total_stores, getString(R.string.store_number) + " " + obj.getStore_number());
        WidgetHelper.getInstance().setTextViewDate(txt_date_create, getString(R.string.day_create) + ": ", obj.getJoin_date());

        WidgetHelper.getInstance().setEditTextWithResult(edt_fullname, obj.getFullname(), getString(R.string.not_update_name));
        WidgetHelper.getInstance().setEditTextWithResult(edt_email, obj.getEmail(), getString(R.string.not_update_namemaile));
        WidgetHelper.getInstance().setEditTextDateWithResult(edt_birthday, obj.getBirthday(), getString(R.string.not_update_birthday));
        WidgetHelper.getInstance().setEditTextWithResult(edt_phone, obj.getPhonenumber(), getString(R.string.not_update_phone));
        WidgetHelper.getInstance().setEditTextWithResult(edt_address, obj.getAddress(), getString(R.string.not_update_address));
        WidgetHelper.getInstance().setSpinnerGender(sp_gender, obj.getGender());

        if (placeModel == null)
            placeModel = new PlaceModel();
        placeModel.setAddress(obj.getAddress());

        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(false);
    }

    @Override
    public void onRequestError(Error error) {
        closeProgressBar();
        menuItem.setIcon(R.drawable.ic_action_edit_line);
        showShortToast(JsonParse.getCodeMessage(error.getCode(), getString(R.string.can_not_load_data)));
    }

    @Override
    public void onUpdateProfileSuccess() {
        closeProgressBar();
        showShortToast(getString(R.string.success_update_user));
        menuItem.setIcon(R.drawable.ic_action_edit_line);
    }

    @Override
    public void getNewSession(final ICmd iCmd, final int type) {
        callbackManager.getNewSesion(new CallbacListener() {
            @Override
            public void onSuccess(RESP_Login success) {
                iCmd.execute(type);
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
    public void onTakePictureGallary(int type, Uri uri) {
        if (uri == null) {
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
            WidgetHelper.getInstance().setAvatarImageFile(img_avatar, file);
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

        menuItem = menu.findItem(R.id.action_profile_edit);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_profile_edit:
                if (menuItem.getIcon().getConstantState() == (getResources().getDrawable(R.drawable.ic_action_edit_line).getConstantState())) {
                    menuItem.setIcon(R.drawable.ic_action_done);
                    setEnableView(true);
                } else {
                    presenter.updateUser(edt_fullname.getText().toString(), sp_gender.getSelectedItemPosition(), edt_birthday.getText().toString(),
                            edt_email.getText().toString(), placeModel);
                }
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callbackManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);

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
