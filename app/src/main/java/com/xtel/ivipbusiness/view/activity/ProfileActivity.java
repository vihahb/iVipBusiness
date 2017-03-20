package com.xtel.ivipbusiness.view.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
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
import com.xtel.ivipbusiness.model.entity.RESP_Full_Profile;
import com.xtel.ivipbusiness.presenter.ProfilePresenter;
import com.xtel.ivipbusiness.view.activity.inf.IProfileView;
import com.xtel.ivipbusiness.view.adapter.GenderAdapter;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.LoginManager;
import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.WidgetHelper;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class ProfileActivity extends BasicActivity implements View.OnClickListener, IProfileView {
    protected ProfilePresenter presenter;
    protected CallbackManager callbackManager;

    protected SwipeRefreshLayout swipeRefreshLayout;
    protected ImageView img_avatar, img_banner;
    protected TextView txt_total_stores, txt_date_create, txt_fullname, txt_email;
    protected EditText edt_fullname, edt_email, edt_birthday, edt_phone, edt_address;
    protected Spinner sp_gender;
    protected GenderAdapter typeAdapter;
    protected MenuItem menuItem;

    protected final int REQUEST_RESIZE_IMAGE = 8;

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
    protected void initSwwipe() {
        swipeRefreshLayout = findSwipeRefreshLayout(R.id.profile_view_swipe);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
    }

    //    Khởi tạo view
    protected void initView() {
        img_avatar = findImageView(R.id.profile_img_avatar);
        img_banner = findImageView(R.id.profile_img_banner);

        txt_total_stores = findTextView(R.id.profile_txt_total_store);
        txt_date_create = findTextView(R.id.profile_txt_date_create);
        txt_fullname = findTextView(R.id.profile_txt_fullname);
        txt_email = findTextView(R.id.profile_txt_email);

        edt_fullname = findEditText(R.id.profile_edt_fullname);
        edt_email = findEditText(R.id.profile_edt_email);
        edt_birthday = findEditText(R.id.profile_edt_birth_day);
        edt_phone = findEditText(R.id.profile_edt_phone);
        edt_address = findEditText(R.id.profile_edt_address);
    }

    //    Khởi tạo spinner để chọn giới tính
    protected void initGender() {
        String[] arrayList = getResources().getStringArray(R.array.gender);
        sp_gender = findSpinner(R.id.profile_sp_gender);
        typeAdapter = new GenderAdapter(this, arrayList);
        sp_gender.setAdapter(typeAdapter);
    }

    protected void initListener() {
        edt_birthday.setOnClickListener(this);
//        edt_address.setOnClickListener(this);

        img_avatar.setOnClickListener(this);
    }

    //    Khởi tạo sự kiện logout
    protected void initLogout() {
        Button btn_logout = (findButton(R.id.profile_btn_logout));
        btn_logout.setOnClickListener(this);
    }

    protected void selectDate() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, R.style.AppCompatAlertDialogStyle, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                WidgetHelper.getInstance().setEditTextDate(edt_birthday, dayOfMonth, month, year);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void setEnableView(boolean isEnable) {
        typeAdapter.setEnable(isEnable);

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

    /*
    * Lấy ảnh đã đưuọc resize
    * */
    protected void getImageResize(Intent data) {
        try {
            int type = data.getIntExtra(Constants.TYPE, -1);
            String server_path = data.getStringExtra(Constants.SERVER_PATH);
            String server_uri = data.getStringExtra(Constants.URI);
            File file = new File(data.getStringExtra(Constants.FILE));

            if (type != -1 && server_path != null && file.exists()) {
                presenter.getImageResise(server_path, server_uri);
                onLoadPicture(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
//            case R.id.profile_edt_address:
//                if (PermissionHelper.checkOnlyPermission(Manifest.permission.ACCESS_FINE_LOCATION, this, REQUEST_LOCATION))
//                    startActivityForResult(ChooseMapsActivity.class, Constants.MODEL, placeModel, REQUEST_LOCATION);
//                break;
            default:
                break;
        }
    }

    //    Hiển thị thông tin của người dùng khi load dữ liệu thành công
    @Override
    public void onGetProfileSuccess(RESP_Full_Profile obj) {
        WidgetHelper.getInstance().setAvatarImageURL(img_avatar, obj.getAvatar());
        WidgetHelper.getInstance().setImageBlurURL(img_banner, obj.getAvatar());

        WidgetHelper.getInstance().setTextViewNoResult(txt_total_stores, getString(R.string.store_number) + " " + obj.getStore_number());
        WidgetHelper.getInstance().setTextViewDate(txt_date_create, getString(R.string.day_create) + ": ", obj.getJoin_date());
        WidgetHelper.getInstance().setTextViewWithResult(txt_fullname, obj.getFullname(), getString(R.string.not_update_name));
        WidgetHelper.getInstance().setTextViewWithResult(txt_email, obj.getEmail(), getString(R.string.not_update_name));

        WidgetHelper.getInstance().setEditTextWithResult(edt_fullname, obj.getFullname(), getString(R.string.not_update_name));
        WidgetHelper.getInstance().setEditTextWithResult(edt_email, obj.getEmail(), getString(R.string.not_update_namemaile));
        WidgetHelper.getInstance().setEditTextDateWithResult(edt_birthday, obj.getBirthday(), getString(R.string.not_update_birthday));
        WidgetHelper.getInstance().setEditTextWithResult(edt_phone, obj.getPhonenumber(), getString(R.string.not_update_phone));
        WidgetHelper.getInstance().setEditTextWithResult(edt_address, obj.getAddress(), getString(R.string.not_update_address));
        WidgetHelper.getInstance().setSpinnerGender(sp_gender, obj.getGender());

//        if (placeModel == null)
//            placeModel = new PlaceModel();
//        placeModel.setAddress(obj.getAddress());

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
        setResult(RESULT_OK);
        setEnableView(false);
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

        Intent intent = new Intent(this, ResizeImageActivity.class);
        intent.putExtra(Constants.URI, uri);
        intent.putExtra(Constants.TYPE, type);

        startActivityForResult(intent, REQUEST_RESIZE_IMAGE);
    }

    @Override
    public void onTakePictureCamera(int type, Bitmap bitmap) {
        if (bitmap == null) {
            showShortToast(getString(R.string.error_get_image));
            return;
        }

        Intent intent = new Intent(this, ResizeImageActivity.class);
        intent.putExtra(Constants.BITMAP, bitmap);
        intent.putExtra(Constants.TYPE, type);

        startActivityForResult(intent, REQUEST_RESIZE_IMAGE);
    }

    public void onLoadPicture(File file) {
        closeProgressBar();
        WidgetHelper.getInstance().setAvatarImageFile(img_avatar, file);
        WidgetHelper.getInstance().setImageBlurFile(img_banner, file);
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
                if (swipeRefreshLayout.isRefreshing())
                    break;
                if (menuItem.getIcon().getConstantState() == (getResources().getDrawable(R.drawable.ic_action_edit_line).getConstantState())) {
                    menuItem.setIcon(R.drawable.ic_action_done_2);
                    setEnableView(true);
                } else {
                    presenter.updateUser(edt_fullname.getText().toString(), sp_gender.getSelectedItemPosition(), edt_birthday.getText().toString(),
                            edt_email.getText().toString(), edt_address.getText().toString());
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RESIZE_IMAGE && resultCode == RESULT_OK) {
            getImageResize(data);
        } else
            presenter.onActivityResult(requestCode, resultCode, data);
    }
}
