package com.xtel.ivipbusiness.view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.presenter.AddNewsPresenter;
import com.xtel.ivipbusiness.view.activity.inf.IAddNewsView;
import com.xtel.ivipbusiness.view.adapter.TypeAdapter;
import com.xtel.ivipbusiness.view.adapter.TypeSaleAdapter;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;
import com.xtel.sdk.callback.DialogListener;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.NetWorkInfo;
import com.xtel.sdk.utils.WidgetHelper;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.io.File;
import java.util.Calendar;

public class AddNewsActivity extends BasicActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, ViewTreeObserver.OnGlobalLayoutListener, IAddNewsView {
    protected AddNewsPresenter presenter;
    protected CallbackManager callbackManager;

    protected ImageView img_banner, img_camera;
    protected EditText edt_title, edt_des, edt_number_voucher, edt_sale, edt_begin_time, edt_end_time, edt_alive, edt_point;
    protected TextView txt_public;
    protected CheckBox chk_voucher;
    protected Spinner sp_news_type, sp_type_sale;
    protected LinearLayout layout_voucher;

    protected final int REQUEST_RESIZE_IMAGE = 8;
    protected boolean isPublic = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);
        callbackManager = CallbackManager.create(this);

        presenter = new AddNewsPresenter(this);
        initToolbar(R.id.add_news_toolbar, null);
        initView();
        initType();
        initTypeSale();
        initListener();
        presenter.getData();
        hideLayout();
    }

    /*
    * Khởi tạo các view trong layout
    * */
    protected void initView() {
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

    /*
    * Khởi tạo spinner chọn type của bản tin
    * */
    protected void initType() {
        sp_news_type = findSpinner(R.id.add_news_sp_type);
        TypeAdapter typeAdapter = new TypeAdapter(this);
        sp_news_type.setAdapter(typeAdapter);
    }

    /*
    * Khởi tạo spinner chọn loại giảm giá
    * */
    protected void initTypeSale() {
        sp_type_sale = findSpinner(R.id.add_news_sp_type_salse);
        TypeSaleAdapter typeAdapter = new TypeSaleAdapter(this);
        sp_type_sale.setAdapter(typeAdapter);
    }

    /*
    * Lắng nghe sự kiện khi view được click
    * */
    protected void initListener() {
//        Lắng nghe khi key board hiển thị hay ẩn đi
        KeyboardVisibilityEvent.setEventListener(
                getActivity(),
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        // some code depending on keyboard visiblity status
                        if (isOpen)
                            showShortToast("show");
                        else
                            showShortToast("hide");
                    }
                });

        img_camera.setOnClickListener(this);
        chk_voucher.setOnCheckedChangeListener(this);

        txt_public.setOnClickListener(this);
        edt_begin_time.setOnClickListener(this);
        edt_end_time.setOnClickListener(this);
    }

    /*
    * Chọn ngày
    * */
    protected void selectDate(final int type) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, R.style.AppCompatAlertDialogStyle, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (type == 0)
                    WidgetHelper.getInstance().setEditTextDate(edt_begin_time, dayOfMonth, (month + 1), year);
                else
                    WidgetHelper.getInstance().setEditTextDate(edt_end_time, dayOfMonth, (month + 1), year);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    /*
    * Lựa chọn để bản tin public hoặc private
    * */
    protected void setPublic(View view) {
        final PopupMenu popup = new PopupMenu(getApplicationContext(), view);
        popup.getMenuInflater().inflate(R.menu.menu_nav_add_news, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_add_news_private:
                        isPublic = false;
                        WidgetHelper.getInstance().setTextViewDrawable(txt_public, 0, R.mipmap.ic_private_gray_18);
                        break;
                    case R.id.nav_add_news_puplic:
                        isPublic = true;
                        WidgetHelper.getInstance().setTextViewDrawable(txt_public, 0, R.mipmap.ic_world_white_18);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    /*
    * Hiển thị layout thêm voucher
    * */
    protected void showLayout() {
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

    /*
    * Ẩn thị layout thêm voucher
    * */
    protected void hideLayout() {
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

    /*
    * Lấy ảnh đã đưuọc resize
    * */
    protected void getImageResize(Intent data) {
        try {
            int type = data.getIntExtra(Constants.TYPE, -1);
            String server_path = data.getStringExtra(Constants.SERVER_PATH);
            File file = new File(data.getStringExtra(Constants.FILE));

            if (type != -1 && server_path != null && file.exists()) {
                presenter.getImageResise(server_path);
                onLoadPicture(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * Lắng nghe khi có view được click
    * */
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
                setPublic(v);
                break;
            case R.id.add_news_img_camera:
                presenter.takePicture();
                break;
            default:
                break;
        }
    }

    /*
    * Sự kiện khi người dùng click vào "Tạo mã khuyến mãi"
    * */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            showLayout();
        } else {
            hideLayout();
        }
    }

    /*
    * Thông báo lỗi khi nhận data truyền sang hoặc không có data truyền sang
    * */
    @Override
    public void onGetDataError() {
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

    /*
    * Chọn ảnh từ Gallery thành công
    * Bắt đầu resize ảnh đã chọn
    * */
    @Override
    public void onTakePictureGallary(Uri uri) {
        if (!NetWorkInfo.isOnline(this)) {
            showShortToast(getString(R.string.error_no_internet));
            return;
        } else if (uri == null) {
            showShortToast(getString(R.string.error_get_image));
            return;
        }

        Intent intent = new Intent(this, ResizeImageActivity.class);
        intent.putExtra(Constants.URI, uri);
        intent.putExtra(Constants.TYPE, 0);

        startActivityForResult(intent, REQUEST_RESIZE_IMAGE);
    }

    /*
    * Chọn ảnh từ Camera thành công
    * Bắt đầu resize ảnh đã chọn
    * */
    @Override
    public void onTakePictureCamera(Bitmap bitmap) {
        if (!NetWorkInfo.isOnline(this)) {
            showShortToast(getString(R.string.error_no_internet));
            return;
        } else if (bitmap == null) {
            showShortToast(getString(R.string.error_get_image));
            return;
        }

        Intent intent = new Intent(this, ResizeImageActivity.class);
        intent.putExtra(Constants.BITMAP, bitmap);
        intent.putExtra(Constants.TYPE, 0);

        startActivityForResult(intent, REQUEST_RESIZE_IMAGE);
    }

    /*
    * Chọn ảnh làm banner thành công
    * Hiển thị ảnh lên banner
    * */
    public void onLoadPicture(File file) {
        closeProgressBar();
        WidgetHelper.getInstance().setImageFile(img_banner, file);
    }

    /*
    * Thêm bản tin thành công
    * */
    @Override
    public void onAddNewsSuccess() {
        showMaterialDialog(false, false, null, getString(R.string.success_add_news), null, getString(R.string.back), new DialogListener() {
            @Override
            public void negativeClicked() {
                setResult(RESULT_OK);
                closeDialog();
                finish();
            }

            @Override
            public void positiveClicked() {
                setResult(RESULT_OK);
                closeDialog();
                finish();
            }
        });
    }

    /*
    * Lấy session mới khi session cũ hết hạn
    * */
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

    /*
    * Hiển thị thông báo
    * Nếu có type truyền vào sẽ check type để focus đén edittext tương ứng
    * */
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
    public void onConfigurationChanged(Configuration newConfig) {
        Log.e("onConfigurationChanged", "change 1");
        super.onConfigurationChanged(newConfig);
        Log.e("onConfigurationChanged", "change 2");
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.e("onConfigurationChanged", "show");
            showShortToast("show");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            showShortToast("hide");
            Log.e("onConfigurationChanged", "hide");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_news, menu);
        menu.findItem(R.id.action_add_news_send_fcm).setVisible(false);
        menu.findItem(R.id.action_add_news_done).setIcon(R.drawable.ic_action_done);
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
                presenter.addNews(edt_title.getText().toString(), sp_news_type.getSelectedItemPosition(), edt_des.getText().toString(), isPublic, chk_voucher.isChecked(), edt_begin_time.getText().toString(),
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
        if (requestCode == REQUEST_RESIZE_IMAGE && resultCode == RESULT_OK) {
            getImageResize(data);
        } else
            presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onGlobalLayout() {
        int heightDiff = edt_des.getRootView().getHeight() - edt_des.getHeight();
        int contentViewTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();

        if(heightDiff <= contentViewTop){
            showShortToast("KeyboardWillHide");
        } else {
            showShortToast("KeyboardWillShow");
        }
    }
}