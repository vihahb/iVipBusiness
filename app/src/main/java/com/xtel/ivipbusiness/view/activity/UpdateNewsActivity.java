package com.xtel.ivipbusiness.view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.RESP_News;
import com.xtel.ivipbusiness.model.entity.Voucher;
import com.xtel.ivipbusiness.presenter.UpdateNewsPresenter;
import com.xtel.ivipbusiness.view.activity.inf.IUpdateNewsView;
import com.xtel.ivipbusiness.view.adapter.SpinnerOneIconAdapter;
import com.xtel.ivipbusiness.view.adapter.TypeAdapter;
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
import java.util.Calendar;

public class UpdateNewsActivity extends BasicActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, IUpdateNewsView {
    private UpdateNewsPresenter presenter;
    private CallbackManager callbackManager;

    private ImageView img_banner, img_camera;
    private EditText edt_title, edt_des, edt_number_voucher, edt_sale, edt_begin_time, edt_end_time, edt_alive, edt_point;
    private TextView txt_public;
    private CheckBox chk_voucher;
    private Spinner sp_news_type, sp_type_sale;
    private LinearLayout layout_voucher;

    private boolean isPublic = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);
        callbackManager = CallbackManager.create(this);

        presenter = new UpdateNewsPresenter(this);
        initToolbar(R.id.add_news_toolbar, null);
        initView();
        initType();
        initTypeSale();
        initListener();
        presenter.getData();
        hideLayout();
    }

    //    Lấy toàn bộ view
    private void initView() {
        img_banner = findImageView(R.id.add_news_img_banner);
        img_camera = findImageButton(R.id.add_news_img_camera);

        txt_public = findTextView(R.id.add_news_txt_public);

        edt_title = findEditText(R.id.add_news_edt_title);
        edt_number_voucher = findEditText(R.id.add_news_edt_number_of_voucher);
        edt_sale = findEditText(R.id.add_news_edt_sale);
        edt_begin_time = findEditText(R.id.add_news_edt_begin_time);
        edt_end_time = findEditText(R.id.add_news_edt_end_time);
        edt_alive = findEditText(R.id.add_news_edt_alive_time);
        edt_point = findEditText(R.id.add_news_edt_exchange_point);
        edt_des = findEditText(R.id.add_news_edt_des);

        chk_voucher = findCheckBox(R.id.add_news_chk_create_news);
        layout_voucher = findLinearLayout(R.id.add_news_layout_voucher);
    }

    //    Khởi tạo spinner chọn type
    private void initType() {
        sp_news_type = findSpinner(R.id.add_news_sp_type);
        TypeAdapter typeAdapter = new TypeAdapter(this);
        sp_news_type.setAdapter(typeAdapter);
    }

    //    Khởi tạo spinner chọn loại giảm giá
    private void initTypeSale() {
        String[] arraylist = getResources().getStringArray(R.array.type_sale);

        sp_type_sale = findSpinner(R.id.add_news_sp_type_salse);
        SpinnerOneIconAdapter typeAdapter = new SpinnerOneIconAdapter(this, R.drawable.ic_action_gender, arraylist);
        sp_type_sale.setAdapter(typeAdapter);
    }

    private void initListener() {
        img_camera.setOnClickListener(this);
        chk_voucher.setOnCheckedChangeListener(this);

        txt_public.setOnClickListener(this);
        edt_begin_time.setOnClickListener(this);
        edt_end_time.setOnClickListener(this);
    }

    private void selectDate(final int type) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, R.style.AppCompatAlertDialogStyle, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (type == 0)
                    WidgetHelper.getInstance().setEditTextDate(edt_begin_time, dayOfMonth, month, year);
                else
                    WidgetHelper.getInstance().setEditTextDate(edt_end_time, dayOfMonth, month, year);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void selectTime(final int type) {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this, R.style.AppCompatAlertDialogStyle, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (type == 0) {
                    WidgetHelper.getInstance().setEditTextTime(edt_begin_time, getString(R.string.start_time) + ": ", hourOfDay, minute);
                } else if (type == 1) {
                    WidgetHelper.getInstance().setEditTextTime(edt_end_time, getString(R.string.end_time) + ": ", hourOfDay, minute);
//                    END_TIME = hourOfDay + ":" + minute;
                } else {
                    WidgetHelper.getInstance().setEditTextTime(edt_alive, "", hourOfDay, minute);
                }
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    private void setPublic() {
        isPublic = !isPublic;

        if (isPublic)
            WidgetHelper.getInstance().setTextViewDrawable(txt_public, 0, R.mipmap.ic_world_white_18);
        else
            WidgetHelper.getInstance().setTextViewDrawable(txt_public, 0, R.mipmap.ic_private_gray_18);
    }

    private void showLayout() {
//             Prepare the View for the animation
        layout_voucher.setVisibility(View.VISIBLE);
        layout_voucher.setAlpha(0.0f);

//             Start the animation
        layout_voucher.animate()
                .translationY(0)
                .alpha(1.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                });
    }

    private void hideLayout() {
        layout_voucher.animate()
                .translationY(-layout_voucher.getHeight())
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        layout_voucher.setVisibility(View.GONE);
                    }
                });
    }



























    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.add_news_edt_begin_time:
                selectDate(0);
                break;
            case R.id.add_news_edt_end_time:
                selectDate(1);
                break;
            case R.id.add_news_txt_public:
                setPublic();
                break;
            case R.id.add_news_img_camera:
                presenter.takePicture();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            showLayout();
        } else {
            hideLayout();
        }
    }

    @Override
    public void onGetDataError() {
        closeProgressBar();
        showMaterialDialog(false, false, null, getString(R.string.error_try_again), null, getString(R.string.back), new DialogListener() {
            @Override
            public void onClicked(Object object) {
                closeDialog();
                finish();
            }

            @Override
            public void onCancel() {
                closeDialog();
                finish();
            }
        });
    }

    @Override
    public void onGetNewsInfoSuccess(RESP_News obj) {
        WidgetHelper.getInstance().setImageURL(img_banner, obj.getBanner());
        WidgetHelper.getInstance().setEditTextWithResult(edt_title, obj.getTitle(), getString(R.string.not_update_title));
        WidgetHelper.getInstance().setSpinnerNewsType(sp_news_type, obj.getNews_type());
        WidgetHelper.getInstance().setEditTextNoResult(edt_des, obj.getDescription());

        if (obj.getVoucher() != null) {
            chk_voucher.setChecked(true);

            Voucher voucher = obj.getVoucher();

            WidgetHelper.getInstance().setEditTextNoResult(edt_number_voucher, String.valueOf(voucher.getNumber_of_voucher()));
            WidgetHelper.getInstance().setEditTextNoResult(edt_sale, String.valueOf(voucher.getSales()));
            WidgetHelper.getInstance().setSpinnerVoucherType(sp_type_sale, voucher.getSales_type());
            WidgetHelper.getInstance().setEditTextDate(edt_begin_time, (voucher.getBegin_time() * 1000));
            WidgetHelper.getInstance().setEditTextDate(edt_end_time, (voucher.getFinish_time() * 1000));
            WidgetHelper.getInstance().setEditTextNoResult(edt_alive, String.valueOf((voucher.getTime_alive() / 60)));
            WidgetHelper.getInstance().setEditTextNoResult(edt_point, String.valueOf(voucher.getPoint()));
        }
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
    public void onLoadPicture(File file) {
        closeProgressBar();
        WidgetHelper.getInstance().setImageFile(img_banner, file);
    }

    @Override
    public void onUpdateSuccess() {
        closeProgressBar();
        showMaterialDialog(false, false, null, getString(R.string.success_update_news), null, getString(R.string.back), new DialogListener() {
            @Override
            public void onClicked(Object object) {
                closeDialog();
            }

            @Override
            public void onCancel() {
                closeDialog();
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
        showShortToast(JsonParse.getCodeMessage(error.getCode(), getString(R.string.error_no_internet)));
    }

    @Override
    public void showShortToast(int type, String message) {
        switch (type) {
            case 0:
                edt_title.requestFocus();
                break;
            case 1:
                edt_number_voucher.requestFocus();
                break;
            case 2:
                edt_sale.requestFocus();
                break;
            case 3:
                edt_point.requestFocus();
                break;
            default:
                break;
        }

        showShortToast(message);
    }

    @Override
    public void closeProgressBar() {
        super.closeProgressBar();
    }

    @Override
    public void showProgressBar(boolean isTouchOutside, boolean isCancel, String title, String message) {
        super.showProgressBar(isTouchOutside, isCancel, title, message);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_add_news_done:
                presenter.updateNews(edt_title.getText().toString(), (sp_news_type.getSelectedItemPosition() + 2), edt_des.getText().toString(), isPublic, chk_voucher.isChecked(), edt_begin_time.getText().toString(),
                        edt_end_time.getText().toString(), edt_alive.getText().toString(), edt_point.getText().toString(), edt_number_voucher.getText().toString(),
                        edt_sale.getText().toString(), sp_type_sale.getSelectedItemPosition());
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
        presenter.onActivityResult(requestCode, resultCode, data);
    }
}