package com.xtel.ivipbusiness.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.xtel.sdk.callback.CallbackIntListener;
import com.xtel.sdk.callback.CallbackStringListener;
import com.xtel.sdk.callback.DialogListener;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.DialogManager;
import com.xtel.sdk.utils.WidgetHelper;

import java.io.File;
import java.util.Calendar;

import jp.wasabeef.richeditor.RichEditor;

public class AddStoreActivity extends BasicActivity implements View.OnClickListener, IAddStoreView {
    protected AddStorePresenter presenter;
    protected CallbackManager callbackManager;

    protected ImageView img_banner, img_logo;
    protected ImageButton img_camera, img_location;
    protected Spinner sp_type;
    protected EditText edt_begin_time, edt_end_time, edt_name, edt_address, edt_phone;

    protected RichEditor editor_des;
    protected ImageButton img_undo, img_redo, img_bold, img_italic, img_subscript, img_superscript, img_strikethrough, img_underline,
            img_heading1, img_heading2, img_heading3, img_heading4, img_heading5, img_heading6, img_txt_color, img_bg_color,
            img_indent, img_outdent, img_align_left, img_align_center, img_align_right, img_blockquote, img_insertbullet,
            img_insertnumber, img_insertimage, img_insertlink, img_insertcheckbox;

    protected ActionBar actionBar;
    protected final int REQUEST_LOCATION = 99;
    protected PlaceModel placeModel;

    protected final int REQUEST_RESIZE_IMAGE = 8;

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
        initInputDescription();
        presenter.getData();
    }

    /*
    * Khởi tạo toolbar
    * */
    protected void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_store_toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    /*
    * Khởi tạo view
    * */
    protected void initView() {
        img_banner = findImageView(R.id.add_store_img_banner);
        img_logo = findImageView(R.id.add_store_img_avatar);

        img_camera = findImageButton(R.id.add_store_img_camera);
        img_location = findImageButton(R.id.add_store_img_location);

        edt_begin_time = findEditText(R.id.add_store_edt_begin_time);
        edt_end_time = findEditText(R.id.add_store_edt_end_time);
        edt_name = findEditText(R.id.add_store_edt_name);
        edt_address = findEditText(R.id.add_store_edt_address);
        edt_phone = findEditText(R.id.add_store_edt_phone);
    }

    /*
    * Khởi tạo spinner chọn loại cửa hàng và chuỗi cửa hàng
    * */
    protected void initType() {
        sp_type = findSpinner(R.id.add_store_sp_type);
        TypeAdapter typeAdapter = new TypeAdapter(this);
        sp_type.setAdapter(typeAdapter);
    }

    /*
    * Lắng nghe sự kiện thi click vào view
    * */
    protected void initListener() {
        img_camera.setOnClickListener(this);
        img_logo.setOnClickListener(this);
        img_location.setOnClickListener(this);
        edt_begin_time.setOnClickListener(this);
        edt_end_time.setOnClickListener(this);
    }

    /*
    * Khởi tạo view nhập vào miêu tả
    * */
    protected void initInputDescription() {
        editor_des = (RichEditor) findViewById(R.id.add_news_editor_des);
        editor_des.setPadding(8, 8, 8, 8);
        editor_des.setEditorFontSize(13);
        editor_des.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                Log.e("onTextChange", "text " + text);
            }
        });


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
        img_align_left.setSelected(true);
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
    * Chọn thời gian mở cửa và đóng cửa
    * */
    protected void selectTime(final int type) {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this, R.style.AppCompatAlertDialogStyle, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (type == 0) {
                    WidgetHelper.getInstance().setEditTextTime(edt_begin_time, hourOfDay, minute);
                } else {
                    WidgetHelper.getInstance().setEditTextTime(edt_end_time, hourOfDay, minute);
                }
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
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
                if (type != 2) {
                    presenter.getImageResise(server_path, type);
                    onLoadPicture(file, type);
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

                    deleteImageFile(server_path);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
   * Xóa file trong bộ nhớ để tránh tạo ra nhiều ảnh
   * */
    protected boolean deleteImageFile(String file_path) {
        try {
            File file = new File(file_path);
            return file.delete();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
    * Kiểm tra xem editor nhập mô tả có được focus
    * Nếu chưa thì focus vào editor
    * */
    protected void checkFocus() {
        if (!editor_des.isFocused())
            editor_des.focusEditor();
    }

    /*
    * Chọn màu của text
    * */
    protected void chooseTextColor(View view) {
        DialogManager.getInstance().chooseColor(AddStoreActivity.this, view, new CallbackIntListener() {
            @Override
            public void negativeClicked(int resource) {
                //noinspection deprecation
                editor_des.setTextColor(getResources().getColor(resource));
            }

            @Override
            public void positiveClicked() {

            }
        });
    }

    /*
    * Chọn màu background của text
    * */
    protected void chooseBackgroundColor(View view) {
        DialogManager.getInstance().chooseColor(AddStoreActivity.this, view, new CallbackIntListener() {
            @Override
            public void negativeClicked(int resource) {
                //noinspection deprecation
                editor_des.setTextBackgroundColor(getResources().getColor(resource));
            }

            @Override
            public void positiveClicked() {

            }
        });
    }

    /*
   * Chọn 1 trong 3 kiểu sắp xếp text
   * */
    protected void selectAlign(int position) {
        if (position == 1) {
            img_align_center.setSelected(false);
            img_align_right.setSelected(false);
            img_align_left.setSelected(true);
        } else if (position == 2) {
            img_align_left.setSelected(false);
            img_align_right.setSelected(false);
            img_align_center.setSelected(true);
        } else {
            img_align_left.setSelected(false);
            img_align_center.setSelected(false);
            img_align_right.setSelected(true);
        }
    }

    /*
    * Chọn 1 trong 2 kiểu gạch đầu dòng
    * */
    protected void selectBulleted(int position) {
        if (position == 1) {
            img_insertnumber.setSelected(false);
            img_insertbullet.setSelected(!img_insertbullet.isSelected());
        } else if (position == 2) {
            img_insertbullet.setSelected(false);
            img_insertnumber.setSelected(!img_insertnumber.isSelected());
        }
    }




    /*
    * Set thông tin cho phù hợp thêm cửa hàng hay thêm chuỗi cửa hàng
    * */
    @Override
    public void onGetDataChain() {
        WidgetHelper.getInstance().setImageResource(img_logo, R.mipmap.ic_logo_chain);
        actionBar.setTitle(getString(R.string.title_activity_add_chain));
        edt_name.setHint(getString(R.string.chain_name));
        edt_address.setHint(getString(R.string.chain_address));
//        editor_des.setHint(getString(R.string.chain_des));
    }

    /*
    * Không có data truyền sang
    * */
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

    /*
    * Chọn ảnh từ gallery thành công
    * Resize ảnh để up lên server
    * */
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

    /*
    * Chọn ảnh bằng camera thành công
    * Resize ảnh để up lên server
    * */
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

    /*
    * Set lại ảnh khi người dùng chọn ảnh mới
    * */
    public void onLoadPicture(File file, int type) {
        if (type == 0)
            WidgetHelper.getInstance().setImageFile(img_banner, file);
        else
            WidgetHelper.getInstance().setAvatarImageFile(img_logo, file);
    }

    /*
    * Tạo cửa hàng hoặc chuỗi cửa hàng thành công
    * */
    @Override
    public void onAddStoreSuccess(String message) {
        showMaterialDialog(false, false, null, message, null, getString(R.string.ok), new DialogListener() {
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

    @Override
    public void showProgressBar(boolean isTouchOutside, boolean isCancel, String title, String message) {
        super.showProgressBar(isTouchOutside, isCancel, title, message);
    }

    @Override
    public void closeProgressBar() {
        super.closeProgressBar();
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
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.add_store_img_camera:
                presenter.takePicture(0);
                break;
            case R.id.add_store_img_avatar:
                presenter.takePicture(1);
                break;
            case R.id.add_store_edt_begin_time:
                selectTime(0);
                break;
            case R.id.add_store_edt_end_time:
                selectTime(1);
                break;
            case R.id.add_store_img_location:
                if (PermissionHelper.checkOnlyPermission(Manifest.permission.ACCESS_FINE_LOCATION, this, REQUEST_LOCATION))
                    startActivityForResult(ChooseMapsActivity.class, Constants.MODEL, placeModel, REQUEST_LOCATION);
                break;

            case R.id.action_undo:
                editor_des.undo();
                break;
            case R.id.action_redo:
                editor_des.redo();
                break;
            case R.id.action_bold:
                checkFocus();
                img_bold.setSelected(!img_bold.isSelected());
                editor_des.setBold();
                break;
            case R.id.action_italic:
                checkFocus();
                img_italic.setSelected(!img_italic.isSelected());
                editor_des.setItalic();
                break;
            case R.id.action_subscript:
                checkFocus();
                img_subscript.setSelected(!img_redo.isSelected());
                editor_des.setSubscript();
                break;
            case R.id.action_superscript:
                checkFocus();
                img_superscript.setSelected(!img_superscript.isSelected());
                editor_des.setSuperscript();
                break;
            case R.id.action_strikethrough:
                checkFocus();
                img_strikethrough.setSelected(!img_strikethrough.isSelected());
                editor_des.setStrikeThrough();
                break;
            case R.id.action_underline:
                checkFocus();
                img_underline.setSelected(!img_underline.isSelected());
                editor_des.setUnderline();
                break;
            case R.id.action_heading1:
                checkFocus();
                img_heading1.setSelected(!img_heading1.isSelected());

                if (img_heading1.isSelected())
                    editor_des.setHeading(0);
                else
                    editor_des.setHeading(1);
                break;
            case R.id.action_heading2:
                checkFocus();
                img_heading2.setSelected(!img_heading2.isSelected());
                editor_des.setHeading(2);
                break;
            case R.id.action_heading3:
                checkFocus();
                img_heading3.setSelected(!img_heading3.isSelected());
                editor_des.setHeading(3);
                break;
            case R.id.action_heading4:
                checkFocus();
                img_heading4.setSelected(!img_heading4.isSelected());
                editor_des.setHeading(4);
                break;
            case R.id.action_heading5:
                checkFocus();
                img_heading5.setSelected(!img_heading5.isSelected());
                editor_des.setHeading(5);
                break;
            case R.id.action_heading6:
                checkFocus();
                img_heading6.setSelected(!img_heading6.isSelected());
                editor_des.setHeading(6);
                break;
            case R.id.action_txt_color:
                checkFocus();
                chooseTextColor(view);
                break;
            case R.id.action_bg_color:
                checkFocus();
                chooseBackgroundColor(view);
                break;
            case R.id.action_indent:
                checkFocus();
                editor_des.setIndent();
                break;
            case R.id.action_outdent:
                checkFocus();
                editor_des.setOutdent();
                break;
            case R.id.action_align_left:
                checkFocus();
                selectAlign(1);
                editor_des.setAlignLeft();
                break;
            case R.id.action_align_center:
                checkFocus();
                selectAlign(2);
                editor_des.setAlignCenter();
                break;
            case R.id.action_align_right:
                checkFocus();
                selectAlign(3);
                editor_des.setAlignRight();
                break;
            case R.id.action_blockquote:
                checkFocus();
                editor_des.setBlockquote();
                break;
            case R.id.action_insert_bullets:
                checkFocus();
                selectBulleted(1);
                editor_des.setBullets();
                break;
            case R.id.action_insert_numbers:
                checkFocus();
                selectBulleted(2);
                editor_des.setNumbers();
                break;
            case R.id.action_insert_image:
                checkFocus();
                presenter.takePicture(2);
                break;
            case R.id.action_insert_link:
                DialogManager.getInstance().enterUrl(AddStoreActivity.this, new CallbackStringListener() {
                    @Override
                    public void negativeClicked(String url) {
                        checkFocus();
                        editor_des.insertLink(url, url);
                    }

                    @Override
                    public void positiveClicked() {

                    }
                });
                break;
            case R.id.action_insert_checkbox:
                checkFocus();
                editor_des.insertTodo();
                break;
            default:
                break;
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
            presenter.addStore(edt_name.getText().toString(), placeModel, edt_phone.getText().toString(), editor_des.getHtml(), edt_begin_time.getText().toString(),
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
        } else if (requestCode == REQUEST_RESIZE_IMAGE && resultCode == RESULT_OK) {
            getImageResize(data);
        } else
            presenter.onActivityResult(requestCode, resultCode, data);
    }
}