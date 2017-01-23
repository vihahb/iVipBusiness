package com.xtel.ivipbusiness.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.RESP_Store;
import com.xtel.ivipbusiness.presenter.StoreInfoPresenter;
import com.xtel.ivipbusiness.view.fragment.inf.IStoreInfoView;
import com.xtel.nipservice.model.entity.Error;
import com.xtel.sdk.callback.DialogListener;
import com.xtel.sdk.utils.WidgetHelper;

/**
 * Created by Vulcl on 1/16/2017
 */

public class StoreInfoFragment extends BasicFragment implements IStoreInfoView {
    private StoreInfoPresenter presenter;

    private ImageView img_banner, img_logo, img_qr_code, img_bar_code;
    private TextView txt_name, txt_address, txt_phone, txt_des;

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
        presenter.getStoreInfo();
    }

    private void initView() {
        img_banner = findImageView(R.id.store_info_img_banner);
        img_logo = findImageView(R.id.store_info_img_logo);
        img_qr_code = findImageView(R.id.store_info_img_qrCode);
        img_bar_code = findImageView(R.id.store_info_img_bar_code);

        txt_name = findTextView(R.id.store_info_txt_fullname);
        txt_address = findTextView(R.id.store_info_txt_address);
        txt_phone = findTextView(R.id.store_info_txt_phone);
        txt_des = findTextView(R.id.store_info_txt_des);
    }

    @Override
    public void onGetStoreInfoSuccess(RESP_Store resp_store) {
        WidgetHelper.getInstance().setImageURL(img_banner, resp_store.getBanner());
        WidgetHelper.getInstance().setImageURL(img_logo, resp_store.getLogo());
        WidgetHelper.getInstance().setImageURL(img_qr_code, resp_store.getQr_code());
        WidgetHelper.getInstance().setImageURL(img_bar_code, resp_store.getBar_code());

        WidgetHelper.getInstance().setTextViewWithResult(txt_name, resp_store.getName(), getString(R.string.not_update_name));
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