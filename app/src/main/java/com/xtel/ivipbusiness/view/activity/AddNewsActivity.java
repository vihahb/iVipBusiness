package com.xtel.ivipbusiness.view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.io.File;
import java.util.Calendar;

import jp.wasabeef.richeditor.RichEditor;

public class AddNewsActivity extends BasicActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, IAddNewsView {
    protected AddNewsPresenter presenter;
    protected CallbackManager callbackManager;

    protected ImageView img_banner, img_camera;
    protected EditText edt_title, edt_number_voucher, edt_sale, edt_begin_time, edt_end_time, edt_alive, edt_point;
    protected TextView txt_public;
    protected CheckBox chk_voucher;
    protected Spinner sp_news_type, sp_type_sale;
    protected LinearLayout layout_voucher;

    protected RichEditor editor_des;
    protected ImageButton img_undo, img_redo, img_bold, img_italic, img_subscript, img_superscript, img_strikethrough, img_underline,
            img_heading1, img_heading2, img_heading3, img_heading4, img_heading5, img_heading6, img_txt_color, img_bg_color,
            img_indent, img_outdent, img_align_left, img_align_center, img_align_right, img_blockquote, img_insertbullet,
            img_insertnumber, img_insertimage, img_insertlink, img_insertcheckbox;
    protected boolean isTxtChanged = false, isBgChanged = false;

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
        initInputDescription();
        presenter.getData();
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
//        edt_des = findEditText(R.id.add_news_edt_des);

        chk_voucher = findCheckBox(R.id.add_news_chk_create_news);
        layout_voucher = findLinearLayout(R.id.add_news_layout_voucher);

//        layout_style = findView(R.id.add_news_layout_style);
//        hideView(layout_style);
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
    * Khởi tạo view nhập vào miêu tả
    * */
    protected void initInputDescription() {
        editor_des = (RichEditor) findViewById(R.id.add_news_editor_des);
        editor_des.setPadding(8, 0, 8, 8);
        editor_des.setEditorFontSize(13);

        img_undo = findImageButton(R.id.action_undo);
        img_undo.setOnClickListener(this);
        img_redo = findImageButton(R.id.action_redo);
        img_redo.setOnClickListener(this);
        img_bold = findImageButton(R.id.action_bold);
        img_bold.setOnClickListener(this);
        img_italic = findImageButton(R.id.action_italic);
        img_italic.setOnClickListener(this);
        img_subscript = findImageButton(R.id.action_subscript);
        img_subscript.setOnClickListener(this);
        img_superscript = findImageButton(R.id.action_superscript);
        img_superscript.setOnClickListener(this);
        img_strikethrough = findImageButton(R.id.action_strikethrough);
        img_strikethrough.setOnClickListener(this);
        img_underline = findImageButton(R.id.action_underline);
        img_underline.setOnClickListener(this);
        img_heading1 = findImageButton(R.id.action_heading1);
        img_heading1.setOnClickListener(this);
        img_heading2 = findImageButton(R.id.action_heading2);
        img_heading2.setOnClickListener(this);
        img_heading3 = findImageButton(R.id.action_heading3);
        img_heading3.setOnClickListener(this);
        img_heading4 = findImageButton(R.id.action_heading4);
        img_heading4.setOnClickListener(this);
        img_heading5 = findImageButton(R.id.action_heading5);
        img_heading5.setOnClickListener(this);
        img_heading6 = findImageButton(R.id.action_heading6);
        img_heading6.setOnClickListener(this);
        img_txt_color = findImageButton(R.id.action_txt_color);
        img_txt_color.setOnClickListener(this);
        img_bg_color = findImageButton(R.id.action_bg_color);
        img_bg_color.setOnClickListener(this);
        img_indent = findImageButton(R.id.action_indent);
        img_indent.setOnClickListener(this);
        img_outdent = findImageButton(R.id.action_outdent);
        img_outdent.setOnClickListener(this);
        img_align_left = findImageButton(R.id.action_align_left);
        img_align_left.setOnClickListener(this);
        img_align_center = findImageButton(R.id.action_align_center);
        img_align_center.setOnClickListener(this);
        img_align_right = findImageButton(R.id.action_align_right);
        img_align_right.setOnClickListener(this);
        img_blockquote = findImageButton(R.id.action_blockquote);
        img_blockquote.setOnClickListener(this);
        img_insertbullet = findImageButton(R.id.action_insert_bullets);
        img_insertbullet.setOnClickListener(this);
        img_insertnumber = findImageButton(R.id.action_insert_numbers);
        img_insertnumber.setOnClickListener(this);
        img_insertimage = findImageButton(R.id.action_insert_image);
        img_insertimage.setOnClickListener(this);
        img_insertlink = findImageButton(R.id.action_insert_link);
        img_insertlink.setOnClickListener(this);
        img_insertcheckbox = findImageButton(R.id.action_insert_checkbox);
        img_insertcheckbox.setOnClickListener(this);
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
            String server_uri = data.getStringExtra(Constants.URI);
            File file = new File(data.getStringExtra(Constants.FILE));

            if (type != -1 && server_path != null && file.exists()) {
                if (type == 0) {
                    presenter.getImageResise(server_path);
                    onLoadPicture(file);
                } else {
                    server_uri = server_uri.replace("https", "http").replace("9191", "9190");
                    Log.e("getImageResize", "uri " + server_uri);
                    if (!editor_des.isFocused()) {
                        String content;
                        if (editor_des.getHtml() == null)
                            content = "";
                        else
                            content = editor_des.getHtml();
                        content += "<img src=\"" + server_uri + "\" alt=\"" + getString(R.string.ivip_business) + "\">";
                        editor_des.setHtml(content);
                        Log.e("getImageResize", "content " + content);
                    } else
                        editor_des.insertImage(server_uri, getString(R.string.ivip_business));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void selectAlign(int position) {
        img_align_left.setSelected(false);
        img_align_center.setSelected(false);
        img_align_right.setSelected(false);

        if (position == 1)
            img_align_left.setSelected(true);
        else if (position == 2)
            img_align_center.setSelected(true);
        else
            img_align_right.setSelected(true);
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
                presenter.takePicture(0);
                break;

            case R.id.action_undo:
                img_undo.setSelected(!img_undo.isSelected());
                editor_des.undo();
                break;
            case R.id.action_redo:
                img_redo.setSelected(!img_redo.isSelected());
                editor_des.redo();
                break;
            case R.id.action_bold:
                img_bold.setSelected(!img_bold.isSelected());
                editor_des.setBold();
                break;
            case R.id.action_italic:
                img_italic.setSelected(!img_italic.isSelected());
                editor_des.setItalic();
                break;
            case R.id.action_subscript:
                img_redo.setSelected(!img_redo.isSelected());
                editor_des.setSubscript();
                break;
            case R.id.action_superscript:
                img_superscript.setSelected(!img_superscript.isSelected());
                editor_des.setSuperscript();
                break;
            case R.id.action_strikethrough:
                img_strikethrough.setSelected(!img_strikethrough.isSelected());
                editor_des.setStrikeThrough();
                break;
            case R.id.action_underline:
                img_underline.setSelected(!img_underline.isSelected());
                editor_des.setUnderline();
                break;
            case R.id.action_heading1:
                img_heading1.setSelected(!img_heading1.isSelected());
                editor_des.setHeading(1);
                break;
            case R.id.action_heading2:
                img_heading2.setSelected(!img_heading2.isSelected());
                editor_des.setHeading(2);
                break;
            case R.id.action_heading3:
                img_heading3.setSelected(!img_heading3.isSelected());
                editor_des.setHeading(3);
                break;
            case R.id.action_heading4:
                img_heading4.setSelected(!img_heading4.isSelected());
                editor_des.setHeading(4);
                break;
            case R.id.action_heading5:
                img_heading5.setSelected(!img_heading5.isSelected());
                editor_des.setHeading(5);
                break;
            case R.id.action_heading6:
                img_heading6.setSelected(!img_heading6.isSelected());
                editor_des.setHeading(6);
                break;
            case R.id.action_txt_color:
                editor_des.setTextColor(isTxtChanged ? Color.BLACK : Color.RED);
                isTxtChanged = !isTxtChanged;
                img_txt_color.setSelected(isTxtChanged);
                break;
            case R.id.action_bg_color:
                editor_des.setTextBackgroundColor(isBgChanged ? Color.TRANSPARENT : Color.YELLOW);
                isBgChanged = !isBgChanged;
                img_bg_color.setSelected(isBgChanged);
                break;
            case R.id.action_indent:
                img_indent.setSelected(!img_indent.isSelected());
                editor_des.setIndent();
                break;
            case R.id.action_outdent:
                img_outdent.setSelected(!img_outdent.isSelected());
                editor_des.setOutdent();
                break;
            case R.id.action_align_left:
                selectAlign(1);
                editor_des.setAlignLeft();
                break;
            case R.id.action_align_center:
                selectAlign(2);
                editor_des.setAlignCenter();
                break;
            case R.id.action_align_right:
                selectAlign(2);
                editor_des.setAlignRight();
                break;
            case R.id.action_blockquote:
                editor_des.setBlockquote();
                break;
            case R.id.action_insert_bullets:
                editor_des.setBullets();
                break;
            case R.id.action_insert_numbers:
                editor_des.setNumbers();
                break;
            case R.id.action_insert_image:
                presenter.takePicture(2);
                break;
            case R.id.action_insert_link:
                editor_des.insertLink("https://github.com/wasabeef", "wasabeef");
                break;
            case R.id.action_insert_checkbox:
                editor_des.insertTodo();
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
    public void onTakePictureGallary(int type, Uri uri) {
        if (!NetWorkInfo.isOnline(this)) {
            showShortToast(getString(R.string.error_no_internet));
            return;
        } else if (uri == null) {
            showShortToast(getString(R.string.error_get_image));
            return;
        }

        Intent intent = new Intent(this, ResizeImageActivity.class);
        intent.putExtra(Constants.URI, uri);
        intent.putExtra(Constants.TYPE, type);

        startActivityForResult(intent, REQUEST_RESIZE_IMAGE);
    }

    /*
    * Chọn ảnh từ Camera thành công
    * Bắt đầu resize ảnh đã chọn
    * */
    @Override
    public void onTakePictureCamera(int type, Bitmap bitmap) {
        if (!NetWorkInfo.isOnline(this)) {
            showShortToast(getString(R.string.error_no_internet));
            return;
        } else if (bitmap == null) {
            showShortToast(getString(R.string.error_get_image));
            return;
        }

        Intent intent = new Intent(this, ResizeImageActivity.class);
        intent.putExtra(Constants.BITMAP, bitmap);
        intent.putExtra(Constants.TYPE, type);

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
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
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
                presenter.addNews(edt_title.getText().toString(), sp_news_type.getSelectedItemPosition(), editor_des.getHtml(), isPublic, chk_voucher.isChecked(), edt_begin_time.getText().toString(),
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
}