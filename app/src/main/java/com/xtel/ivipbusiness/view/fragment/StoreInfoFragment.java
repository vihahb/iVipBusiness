package com.xtel.ivipbusiness.view.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.RESP_Store;
import com.xtel.ivipbusiness.presenter.StoreInfoPresenter;
import com.xtel.ivipbusiness.view.fragment.inf.IStoreInfoView;
import com.xtel.ivipbusiness.view.widget.AppBarStateChangeListener;
import com.xtel.sdk.callback.DialogListener;
import com.xtel.sdk.utils.WidgetHelper;

/**
 * Created by Vulcl on 1/16/2017
 */

public class StoreInfoFragment extends BasicFragment implements View.OnClickListener, IStoreInfoView {
    private StoreInfoPresenter presenter;

    private ImageView img_banner, img_logo, img_qr_code, img_bar_code;
    private EditText edt_name, edt_address, edt_phone, edt_des;

    private RESP_Store resp_store;
    private boolean isShow = true;

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

        presenter = new StoreInfoPresenter(this);
        initView();
        initListener();
        initAnimationHideImage(view);
        presenter.getStoreInfo();
    }

    private void initView() {
        img_banner = findImageView(R.id.store_info_img_banner);
        img_logo = findImageView(R.id.store_info_img_logo);
        img_qr_code = findImageView(R.id.store_info_img_qrCode);
        img_bar_code = findImageView(R.id.store_info_img_bar_code);

        edt_name = findEditText(R.id.store_info_edt_fullname);
        edt_address = findEditText(R.id.store_info_edt_address);
        edt_phone = findEditText(R.id.store_info_edt_phone);
        edt_des = findEditText(R.id.store_info_edt_des);
    }

    private void initListener() {
        img_qr_code.setOnClickListener(this);
        img_bar_code.setOnClickListener(this);
    }

    private void initAnimationHideImage(View view) {
        AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.store_info_app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateEXPANDED() {
                isShow = true;
                showFloatingActionButton(img_logo);
            }

            @Override
            public void onStateIDLE() {
                isShow = false;
                hideFloatingActionButton(img_logo);
            }
        });
    }

    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();

    private void hideFloatingActionButton(View view) {
        debug("hide");
        ViewCompat.animate(view).scaleX(0.0F).scaleY(0.0F).alpha(0.0F).setInterpolator(INTERPOLATOR).withLayer()
                .setListener(new ViewPropertyAnimatorListener() {
                    public void onAnimationStart(View view) {
                    }

                    public void onAnimationCancel(View view) {
                    }

                    public void onAnimationEnd(View view) {
                        if (!isShow)
                            view.setVisibility(View.GONE);
                    }
                }).start();
    }

    private void showFloatingActionButton(View view) {
        debug("show");
        view.setVisibility(View.VISIBLE);
        ViewCompat.animate(view).scaleX(1.0F).scaleY(1.0F).alpha(1.0F).setInterpolator(INTERPOLATOR).withLayer().setListener(null).start();
    }

    @Override
    public void onGetStoreInfoSuccess(RESP_Store resp_store) {
        this.resp_store = resp_store;
        WidgetHelper.getInstance().setImageURL(img_banner, resp_store.getBanner());
        WidgetHelper.getInstance().setImageURL(img_logo, resp_store.getLogo());
        WidgetHelper.getInstance().setImageURL(img_qr_code, resp_store.getQr_code());
        WidgetHelper.getInstance().setImageURL(img_bar_code, resp_store.getBar_code());

        WidgetHelper.getInstance().setEditTextNoResult(edt_name, resp_store.getName());
        WidgetHelper.getInstance().setEditTextNoResult(edt_address, resp_store.getAddress());
        WidgetHelper.getInstance().setEditTextNoResult(edt_phone, resp_store.getPhonenumber());
        WidgetHelper.getInstance().setEditTextNoResult(edt_des, resp_store.getDescription());
    }

    @Override
    public void onGetStoreInfoError() {
        showMaterialDialog(false, false, null, getString(R.string.can_not_load_data), null, getString(R.string.back), new DialogListener() {
            @Override
            public void onClicked(Object object) {
                closeDialog();
                getActivity().finish();
            }

            @Override
            public void onCancel() {

            }
        });

    }

    @SuppressWarnings("ConstantConditions")
    private void showQrCode() {
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

        Picasso.with(getContext())
                .load(resp_store.getQr_code())
                .noPlaceholder()
                .error(R.mipmap.ic_error)
                .into(img_qr_code);

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
    private void showBarCode() {
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

        Picasso.with(getContext())
                .load(resp_store.getBar_code())
                .noPlaceholder()
                .error(R.mipmap.ic_error)
                .into(img_qr_code);

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

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.store_info_img_qrCode)
            showQrCode();
        else if (id == R.id.store_info_img_bar_code)
            showBarCode();
    }
}