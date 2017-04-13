package com.xtel.ivipbusiness.presenter;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.view.activity.inf.IActiveView;
import com.xtel.nipservicesdk.utils.PermissionHelper;
import com.xtel.sdk.utils.NetWorkInfo;
import com.xtel.sdk.utils.TextUnit;

/**
 * Created by Mr. M.2 on 1/12/2017
 */

public class ActivePresenter extends BasicPresenter {
    private IActiveView view;
    private final int ACCOUNT_KIT_REQUEST_CODE = 99, PERMISSION_REQUEST_CODE = 10001;
    private String[] PermissionListAccKit = {Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_STATE};

    private String phone;

    public ActivePresenter(IActiveView view) {
        this.view = view;
    }

    /*
    * Kiểm tra số điện và kiểm tra cấp quyền trước khi validate bằng account kit
    * */
    public void validatePhoneToActive(String phone) {
        if (!TextUnit.getInstance().validateText(phone)) {
            view.onValidateError(view.getActivity().getString(R.string.error_input_username));
            return;
        }

        if (TextUnit.getInstance().validateLong(phone) == -1) {
            view.onValidateError(view.getActivity().getString(R.string.error_validate_phone));
            return;
        }

        if (!TextUnit.getInstance().validatePhone(phone)) {
            view.onValidateError(view.getActivity().getString(R.string.error_validate_phone));
            return;
        }

        if (!PermissionHelper.checkListPermission(PermissionListAccKit, view.getActivity(), PERMISSION_REQUEST_CODE)) {
            return;
        }

        if (!NetWorkInfo.isOnline(view.getActivity())) {
            view.onNoInternet();
            return;
        }

        this.phone = phone;
        startValidatePhone();
    }

    /*
    * Bắt đầu validate bằng account kit
    * */
    private void startValidatePhone() {
        Intent intent = new Intent(view.getActivity(), AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder = new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE, AccountKitActivity.ResponseType.CODE);
        configurationBuilder.setDefaultCountryCode("VN");
        configurationBuilder.setTitleType(AccountKitActivity.TitleType.LOGIN);
        configurationBuilder.setReadPhoneStateEnabled(true);
        configurationBuilder.setReceiveSMS(true);
        configurationBuilder.setInitialPhoneNumber(new PhoneNumber("84", phone));
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configurationBuilder.build());
        view.startActivityForResult(intent, ACCOUNT_KIT_REQUEST_CODE);
    }

    /*
    * Lắng nghe sự kiện khi người dùng cho phép sử dụng quyền để tiếp tục validate số điện thoại
    * */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (!NetWorkInfo.isOnline(view.getActivity())) {
            view.onNoInternet();
            return;
        }

        startValidatePhone();
    }

    /*
    * Lằng nghe sự kiện khi validate bằng account kit
    * */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACCOUNT_KIT_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (loginResult.getError() != null) {
                debug(loginResult.getError().getErrorType().getMessage());
                debug(loginResult.getError().toString());
            } else if (loginResult.wasCancelled()) {
                debug("Login Cancelled");
            } else {
                if (loginResult.getAccessToken() == null) {
                    String authorization_code = loginResult.getAuthorizationCode();
//                    Log.e("Authorization Id: ", authorization_code);

                    view.onValidatePhoneToActiveSuccess(authorization_code);
                }
                // Success! Start your next activity...
            }
        }
    }
}