package com.xtel.ivipbusiness.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.PlaceModel;
import com.xtel.ivipbusiness.model.entity.RESP_Store;
import com.xtel.ivipbusiness.presenter.StoreInfoPresenter;
import com.xtel.ivipbusiness.view.activity.ChooseMapsActivity;
import com.xtel.ivipbusiness.view.activity.LoginActivity;
import com.xtel.ivipbusiness.view.activity.ResizeImageActivity;
import com.xtel.ivipbusiness.view.activity.ViewStoreActivity;
import com.xtel.ivipbusiness.view.fragment.inf.IStoreInfoView;
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

/**
 * Created by Vulcl on 1/16/2017
 */

public class StoreInfoFragment extends BasicFragment implements View.OnClickListener, IStoreInfoView {
    protected StoreInfoPresenter presenter;

    protected ImageView img_banner, img_logo, img_qr_code, img_bar_code;
    protected ImageButton img_camera, img_location;
    protected EditText edt_begin_time, edt_end_time, edt_name, edt_address, edt_phone;

    protected RichEditor editor_des;
    protected ImageButton img_undo, img_redo, img_bold, img_italic, img_subscript, img_superscript, img_strikethrough, img_underline,
            img_heading1, img_heading2, img_heading3, img_heading4, img_heading5, img_heading6, img_txt_color, img_bg_color,
            img_indent, img_outdent, img_align_left, img_align_center, img_align_right, img_blockquote, img_insertbullet,
            img_insertnumber, img_insertimage, img_insertlink, img_insertcheckbox;

    protected SwipeRefreshLayout swipeRefreshLayout;
    protected CallbackManager callbackManager;

    protected RESP_Store resp_store;
    protected final int REQUEST_LOCATION = 99, REQUEST_RESIZE_IMAGE = 9;

    public static StoreInfoFragment newInstance() {
        return new StoreInfoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callbackManager = CallbackManager.create(getActivity());

        presenter = new StoreInfoPresenter(this);
        initSwwipe();
        initView();
        initListener();
        initInputDescription(view);
        setEnableView(false);
//        initAnimationHideImage(view);
        presenter.getData();
    }

    //    Khởi tạo swipeRefreshLayout để hiển thị load thông tin
    protected void initSwwipe() {
        swipeRefreshLayout = findSwipeRefreshLayout(R.id.store_info_swipe);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
    }

    protected void initView() {
        img_banner = findImageView(R.id.store_info_img_banner);
        img_logo = findImageView(R.id.store_info_img_logo);
        img_qr_code = findImageView(R.id.store_info_img_qrCode);
        img_bar_code = findImageView(R.id.store_info_img_bar_code);

        img_camera = findImageButton(R.id.store_info_img_camera);
        img_location = findImageButton(R.id.store_info_img_location);

        edt_begin_time = findEditText(R.id.store_info_edt_begin_time);
        edt_end_time = findEditText(R.id.store_info_edt_end_time);
        edt_name = findEditText(R.id.store_info_edt_fullname);
        edt_address = findEditText(R.id.store_info_edt_address);
        edt_phone = findEditText(R.id.store_info_edt_phone);
//        edt_des = findEditText(R.id.store_info_edt_des);
    }

    protected void initListener() {
        img_qr_code.setOnClickListener(this);
        img_bar_code.setOnClickListener(this);
        img_camera.setOnClickListener(this);
        img_logo.setOnClickListener(this);

        edt_begin_time.setOnClickListener(this);
        edt_end_time.setOnClickListener(this);
        img_location.setOnClickListener(this);
    }

    /*
    * Khởi tạo view nhập vào miêu tả
    * */
    protected void initInputDescription(View view) {
        editor_des = (RichEditor) view.findViewById(R.id.add_news_editor_des);
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

    public void setEnableView(boolean isEnable) {
        edt_begin_time.setEnabled(isEnable);
        edt_end_time.setEnabled(isEnable);
        edt_name.setEnabled(isEnable);
        edt_address.setEnabled(isEnable);
        edt_phone.setEnabled(isEnable);
        editor_des.setInputEnabled(isEnable);

        img_camera.setEnabled(isEnable);
        img_location.setEnabled(isEnable);
        img_banner.setEnabled(isEnable);
        img_logo.setEnabled(isEnable);

//        if (isEnable)
//            edt_des.setBackground(getActivity().getResources().getDrawable(R.drawable.edittext_des));
//        else
//            edt_des.setBackground(getActivity().getResources().getDrawable(R.drawable.edittext_des_disable));
    }

//    protected void initAnimationHideImage(View view) {
//        AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.store_info_app_bar);
//        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
//            @Override
//            public void onStateEXPANDED() {
//                isShow = true;
//                showFloatingActionButton(layout_logo);
//            }
//
//            @Override
//            public void onStateIDLE() {
//                isShow = false;
//                hideFloatingActionButton(layout_logo);
//            }
//        });
//    }

//    protected static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();

//    protected void hideFloatingActionButton(View view) {
//        debug("hide");
//        ViewCompat.animate(view).scaleX(0.0F).scaleY(0.0F).alpha(0.0F).setInterpolator(INTERPOLATOR).withLayer()
//                .setListener(new ViewPropertyAnimatorListener() {
//                    public void onAnimationStart(View view) {
//                    }
//
//                    public void onAnimationCancel(View view) {
//                    }
//
//                    public void onAnimationEnd(View view) {
//                        if (!isShow)
//                            view.setVisibility(View.GONE);
//                    }
//                }).start();
//    }

//    protected void showFloatingActionButton(View view) {
//        debug("show");
//        view.setVisibility(View.VISIBLE);
//        ViewCompat.animate(view).scaleX(1.0F).scaleY(1.0F).alpha(1.0F).setInterpolator(INTERPOLATOR).withLayer().setListener(null).start();
//    }

    protected void selectBeginTime() {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(getContext(), R.style.AppCompatAlertDialogStyle, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                WidgetHelper.getInstance().setEditTextTime(edt_begin_time, getString(R.string.open_time) + ": ", hourOfDay, minute);
//                String BEGIN_TIME = hourOfDay + ":" + minute;
                resp_store.setBegin_time(Constants.convertTimeToLong(hourOfDay, minute));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    protected void selectEndTime() {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(getContext(), R.style.AppCompatAlertDialogStyle, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                WidgetHelper.getInstance().setEditTextTime(edt_end_time, getString(R.string.close_time) + ": ", hourOfDay, minute);
//                String END_TIME = hourOfDay + ":" + minute;
                resp_store.setEnd_time(Constants.convertTimeToLong(hourOfDay, minute));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    @SuppressWarnings("ConstantConditions")
    protected void showQrCode() {
        if (resp_store.getQr_code() == null || resp_store.getQr_code().isEmpty()) {
            showShortToast(getString(R.string.error_view_code));
            return;
        }

        final Dialog bottomSheetDialog = new Dialog(getContext(), R.style.MaterialDialogSheet);
        bottomSheetDialog.setContentView(R.layout.qr_code_bottom_sheet);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bottomSheetDialog.getWindow().setGravity(Gravity.CENTER);

        Button txt_close = (Button) bottomSheetDialog.findViewById(R.id.dialog_txt_close);
        ImageView img_qr_code = (ImageView) bottomSheetDialog.findViewById(R.id.dialog_qr_code);

        WidgetHelper.getInstance().setImageURL(img_qr_code, resp_store.getQr_code());

        if (txt_close != null)
            txt_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                }
            });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bottomSheetDialog.show();
            }
        }, 200);
    }

    @SuppressWarnings("ConstantConditions")
    protected void showBarCode() {
        if (resp_store.getBar_code() == null || resp_store.getBar_code().isEmpty()) {
            showShortToast(getString(R.string.error_view_code));
            return;
        }

        final Dialog bottomSheetDialog = new Dialog(getContext(), R.style.MaterialDialogSheet);
        bottomSheetDialog.setContentView(R.layout.qr_code_bottom_sheet);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bottomSheetDialog.getWindow().setGravity(Gravity.CENTER);

        Button txt_close = (Button) bottomSheetDialog.findViewById(R.id.dialog_txt_close);
        ImageView img_qr_code = (ImageView) bottomSheetDialog.findViewById(R.id.dialog_qr_code);

        WidgetHelper.getInstance().setImageURL(img_qr_code, resp_store.getBar_code());

        if (txt_close != null)
            txt_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                }
            });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bottomSheetDialog.show();
            }
        }, 200);
    }

    public void updateStore() {
        resp_store.setName(edt_name.getText().toString());
        resp_store.setPhonenumber(edt_phone.getText().toString());
        resp_store.setDescription(editor_des.getHtml());

        presenter.updateStore(resp_store);
    }

    protected void setAddress(PlaceModel placeModel) {
        edt_address.setText(placeModel.getAddress());

        resp_store.setAddress(placeModel.getAddress());
        resp_store.setLocation_lat(placeModel.getLatitude());
        resp_store.setLocation_lng(placeModel.getLongtitude());
    }

    public void enableToEdit() {
        if (swipeRefreshLayout.isRefreshing())
            return;

        setEnableView(true);
        ((ViewStoreActivity) getActivity()).changeMenuIcon(R.drawable.ic_action_done_2);
    }

    protected void getImageResize(Intent data) {
        try {
            int type = data.getIntExtra(Constants.TYPE, -1);
            String server_path = data.getStringExtra(Constants.SERVER_PATH);
            String server_uri = data.getStringExtra(Constants.URI);
            File file = new File(data.getStringExtra(Constants.FILE));

            Log.e("getImageResize", "file path " + file.getAbsolutePath());
            Log.e("getImageResize", "type " + type);
            Log.e("getImageResize", "server_path " + server_path);

            if (type != -1 && server_path != null && file.exists()) {
                if (type != 2) {
                    presenter.getImageResise(server_path, server_uri, type);
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
            Log.e("getImageResize", "error " + e.getMessage());
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
        DialogManager.getInstance().chooseColor(getActivity(), view, new CallbackIntListener() {
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
        DialogManager.getInstance().chooseColor(getActivity(), view, new CallbackIntListener() {
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










    @Override
    public void onGetDataError() {
        showMaterialDialog(false, false, null, getString(R.string.error_try_again), null, getString(R.string.back), new DialogListener() {
            @Override
            public void negativeClicked() {
                closeDialog();
                getActivity().finish();
            }

            @Override
            public void positiveClicked() {
                closeDialog();
            }
        });
    }

    @Override
    public void onGetStoreInfoSuccess(RESP_Store resp_store) {
        this.resp_store = resp_store;
        WidgetHelper.getInstance().setImageURL(img_banner, resp_store.getBanner());
        WidgetHelper.getInstance().setAvatarImageURL(img_logo, resp_store.getLogo());
        WidgetHelper.getInstance().setImageURL(img_qr_code, resp_store.getQr_code());
        WidgetHelper.getInstance().setImageURL(img_bar_code, resp_store.getBar_code());

        WidgetHelper.getInstance().setEditTextTime(edt_begin_time, (getString(R.string.open_time) + ": "), resp_store.getBegin_time());
        WidgetHelper.getInstance().setEditTextTime(edt_end_time, (getString(R.string.close_time) + ": "), resp_store.getEnd_time());
        WidgetHelper.getInstance().setEditTextNoResult(edt_name, resp_store.getName());
        WidgetHelper.getInstance().setEditTextNoResult(edt_address, resp_store.getAddress());
        WidgetHelper.getInstance().setEditTextNoResult(edt_phone, resp_store.getPhonenumber());

        editor_des.setHtml(resp_store.getDescription());
//        WidgetHelper.getInstance().setEditTextNoResult(edt_des, resp_store.getDescription());

        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(false);
    }

    @Override
    public void onUpdateStoreInfoSuccess() {
        ((ViewStoreActivity) getActivity()).changeMenuIcon(R.drawable.ic_action_edit_line);
        ((ViewStoreActivity) getActivity()).setResp_store(resp_store);

        setEnableView(false);
        onGetStoreInfoSuccess(resp_store);
        closeProgressBar();

        getActivity().setResult(Activity.RESULT_OK);

        showMaterialDialog(false, false, null, getString(R.string.success_update_store), null, getString(R.string.back), new DialogListener() {
            @Override
            public void negativeClicked() {
                closeDialog();
            }

            @Override
            public void positiveClicked() {
                closeDialog();
            }
        });
    }

    @Override
    public void onGetStoreInfoError() {
        showMaterialDialog(false, false, null, getString(R.string.can_not_load_data), null, getString(R.string.back), new DialogListener() {
            @Override
            public void negativeClicked() {
                closeDialog();
                getActivity().finish();
            }

            @Override
            public void positiveClicked() {
                closeDialog();
                getActivity().finish();
            }
        });
    }

    @Override
    public void startActivityForResult(Class clazz, String key, Object object, int requestCode) {
        super.startActivityForResult(clazz, key, object, requestCode);
    }


    @Override
    public void onTakePictureGallary(int type, Uri uri) {
        if (uri == null) {
            showShortToast(getString(R.string.error_get_image));
            return;
        }

        Intent intent = new Intent(getActivity(), ResizeImageActivity.class);
        intent.putExtra(Constants.URI, uri);
        intent.putExtra(Constants.TYPE, type);

        startActivityForResult(intent, REQUEST_RESIZE_IMAGE);

//        Log.e("111111111", "11111");
//        showProgressBar(false, false, null, getString(R.string.uploading_file));
//
//        Bitmap bitmap = null;
//
//        try {
//            bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        if (bitmap != null) {
//            presenter.postImage(bitmap, type);
//        } else
//            closeProgressBar();
    }

    @Override
    public void onTakePictureCamera(int type, Bitmap bitmap) {
        if (bitmap == null) {
            showShortToast(getString(R.string.error_get_image));
            return;
        }

        Intent intent = new Intent(getActivity(), ResizeImageActivity.class);
        intent.putExtra(Constants.BITMAP, bitmap);
        intent.putExtra(Constants.TYPE, type);

        startActivityForResult(intent, REQUEST_RESIZE_IMAGE);

//        showProgressBar(false, false, null, getString(R.string.uploading_file));
//        presenter.postImage(bitmap, type);
    }

    @Override
    public void onLoadPicture(File file, int type) {
        closeProgressBar();

        if (type == 0)
            WidgetHelper.getInstance().setImageFile(img_banner, file);
        else
            WidgetHelper.getInstance().setAvatarImageFile(img_logo, file);
    }

    @Override
    public void getNewSession(final ICmd iCmd) {
        callbackManager.getNewSesion(new CallbacListener() {
            @Override
            public void onSuccess(RESP_Login success) {
                iCmd.execute();
            }

            @Override
            public void onError(Error error) {
                showShortToast(getString(R.string.error_end_of_session));
                getActivity().finishAffinity();
                startActivity(LoginActivity.class);
            }
        });
    }

    @Override
    public void onValidateError(String error) {
        showShortToast(error);
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
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.store_info_img_qrCode)
            showQrCode();
        else if (id == R.id.store_info_img_bar_code)
            showBarCode();
        else if (id == R.id.store_info_img_camera) {
            if (!swipeRefreshLayout.isRefreshing())
                presenter.takePicture(0);
        } else if (id == R.id.store_info_img_logo) {
            if (!swipeRefreshLayout.isRefreshing())
                presenter.takePicture(1);
        } else if (id == R.id.store_info_edt_begin_time)
            selectBeginTime();
        else if (id == R.id.store_info_edt_end_time)
            selectEndTime();
        else if (id == R.id.store_info_img_location) {
            if (PermissionHelper.checkOnlyPermission(Manifest.permission.ACCESS_FINE_LOCATION, getActivity(), REQUEST_LOCATION)) {
                PlaceModel placeModel = new PlaceModel();
                placeModel.setAddress(resp_store.getAddress());
                placeModel.setLatitude(resp_store.getLocation_lat());
                placeModel.setLongtitude(resp_store.getLocation_lng());

                startActivityForResult(ChooseMapsActivity.class, Constants.MODEL, placeModel, REQUEST_LOCATION);
            }
        }

        switch (id) {

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
                DialogManager.getInstance().enterUrl(getContext(), new CallbackStringListener() {
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
    public void onDestroy() {
        presenter.setExists(false);
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callbackManager.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PlaceModel placeModel = new PlaceModel();
                placeModel.setAddress(resp_store.getAddress());
                placeModel.setLatitude(resp_store.getLocation_lat());
                placeModel.setLongtitude(resp_store.getLocation_lng());

                startActivityForResult(ChooseMapsActivity.class, Constants.MODEL, placeModel, REQUEST_LOCATION);
            } else
                showShortToast(getString(R.string.error_permission));
        } else
            presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        debug(requestCode + "   " + resultCode);
        debug("ù á ó ào ồi " + requestCode + "   " + resultCode);

        if (requestCode == REQUEST_LOCATION && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                PlaceModel placeModel = (PlaceModel) data.getSerializableExtra(Constants.MODEL);
                setAddress(placeModel);
            }
        } else if (requestCode == REQUEST_RESIZE_IMAGE && resultCode == Activity.RESULT_OK) {
            debug("ù á ó ạy ồi " + requestCode + "   " + resultCode);
            getImageResize(data);
        }  else
            presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}