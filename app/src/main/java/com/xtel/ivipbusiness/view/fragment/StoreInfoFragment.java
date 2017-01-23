package com.xtel.ivipbusiness.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

public class StoreInfoFragment extends BasicFragment implements IStoreInfoView {
    private StoreInfoPresenter presenter;

    private ImageView img_banner, img_logo, img_qr_code, img_bar_code;
    private TextView txt_address, txt_phone, txt_des;
    private EditText edt_name;

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
        initAnimationHideImage(view);
        presenter.getStoreInfo();
    }

    private void initView() {
        img_banner = findImageView(R.id.store_info_img_banner);
        img_logo = findImageView(R.id.store_info_img_logo);
        img_qr_code = findImageView(R.id.store_info_img_qrCode);
        img_bar_code = findImageView(R.id.store_info_img_bar_code);

        edt_name = findEditText(R.id.store_info_edt_fullname);
        txt_address = findTextView(R.id.store_info_txt_address);
        txt_phone = findTextView(R.id.store_info_txt_phone);
        txt_des = findTextView(R.id.store_info_txt_des);
    }

    private boolean isShow = true;

    private void initAnimationHideImage(View view) {
//        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.store_info_collapsing);
//        collapsingToolbarLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                if (top <= 0) {
//                    hideFloatingActionButton(img_logo);
//                } else
//                    showFloatingActionButton(img_logo);
//            }
//        });

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
        WidgetHelper.getInstance().setImageURL(img_banner, resp_store.getBanner());
        WidgetHelper.getInstance().setImageURL(img_logo, resp_store.getLogo());
        WidgetHelper.getInstance().setImageURL(img_qr_code, resp_store.getQr_code());
        WidgetHelper.getInstance().setImageURL(img_bar_code, resp_store.getBar_code());

        WidgetHelper.getInstance().setEditTextWithResult(edt_name, resp_store.getName(), getString(R.string.not_update_store_name));
        WidgetHelper.getInstance().setTextViewWithResult(txt_address, resp_store.getAddress(), getString(R.string.not_update_address));
        WidgetHelper.getInstance().setTextViewWithResult(txt_phone, resp_store.getPhonenumber(), getString(R.string.not_update_phone));
        WidgetHelper.getInstance().setTextViewWithResult(txt_des, resp_store.getDescription(), getString(R.string.not_update_des));
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
}