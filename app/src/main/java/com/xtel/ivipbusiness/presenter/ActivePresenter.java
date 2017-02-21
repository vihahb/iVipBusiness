package com.xtel.ivipbusiness.presenter;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.view.activity.inf.IActiveView;
import com.xtel.nipservicesdk.utils.PermissionHelper;
import com.xtel.sdk.utils.NetWorkInfo;

/**
 * Created by Mr. M.2 on 1/12/2017
 */

public class ActivePresenter extends BasicPresenter {
    private IActiveView view;
    private final int ACCOUNT_KIT_REQUEST_CODE = 99, PERMISSION_REQUEST_CODE = 10001;
    private String[] PermissionListAccKit = {Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_STATE};

    public ActivePresenter(IActiveView view) {
        this.view = view;
    }

    public void validatePhoneToActive(String phone) {
        if (!validateText(phone)) {
            view.onValidateError(view.getActivity().getString(R.string.error_input_username));
            return;
        }

        if (validateLong(phone) == -1) {
            view.onValidateError(view.getActivity().getString(R.string.error_validate_phone));
            return;
        }

        if (!validatePhone(phone)) {
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

        startValidatePhone();
    }

    private void startValidatePhone() {
        Intent intent = new Intent(view.getActivity(), AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder = new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE, AccountKitActivity.ResponseType.CODE);
        configurationBuilder.setDefaultCountryCode("VN");
        configurationBuilder.setTitleType(AccountKitActivity.TitleType.LOGIN);
        configurationBuilder.setReadPhoneStateEnabled(true);
        configurationBuilder.setReceiveSMS(true);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configurationBuilder.build());
        view.startActivityForResult(intent, ACCOUNT_KIT_REQUEST_CODE);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (!NetWorkInfo.isOnline(view.getActivity())) {
            view.onNoInternet();
            return;
        }

        startValidatePhone();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACCOUNT_KIT_REQUEST_CODE) { // confirm that this response matches your request
            debug("mịa nó chạy rồi mà");
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (loginResult.getError() != null) {
                debug(loginResult.getError().getErrorType().getMessage());
                debug(loginResult.getError().toString());
            } else if (loginResult.wasCancelled()) {
                debug("Login Cancelled");
            } else {
                debug("vao chưa");
                if (loginResult.getAccessToken() == null) {
                    String authorization_code = loginResult.getAuthorizationCode();
                    Log.e("Authorization Id: ", authorization_code);

                    view.onValidatePhoneToActiveSuccess(authorization_code);
                }
                // Success! Start your next activity...
            }
        }
    }
}
