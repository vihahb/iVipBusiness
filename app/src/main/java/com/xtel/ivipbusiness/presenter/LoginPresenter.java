package com.xtel.ivipbusiness.presenter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.view.activity.RegisterActivity;
import com.xtel.ivipbusiness.view.activity.inf.ILoginView;
import com.xtel.nipservice.utils.PermissionHelper;
import com.xtel.sdk.utils.NetWorkInfo;

/**
 * Created by VULCL on 1/10/2017
 */

public class LoginPresenter extends BasicPresenter {
    private ILoginView view;
    private final int ACCOUNT_KIT_REQUEST_CODE = 99, PERMISSION_REQUEST_CODE = 10001, LOGIN_REQUEST_CODE = 111;
    private String[] PermissionListAccKit = {Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_STATE};

    public LoginPresenter(ILoginView view) {
        this.view = view;
    }

    public void registerAccount() {
        view.startActivity(RegisterActivity.class);
    }

    public void loginAccount(String phone, String password) {
        if (!validateData(phone, password))
            return;

        if (isPhone(phone) == -1) {
            debug("6");
            view.onValidateError(view.getActivity().getString(R.string.error_validate_phone));
            return;
        }

        if (!validatePhone(phone)) {
            view.onValidateError(view.getActivity().getString(R.string.error_validate_phone));
            return;
        }

        if (!PermissionHelper.checkOnlyPermission(Manifest.permission.READ_PHONE_STATE, view.getActivity(), LOGIN_REQUEST_CODE))
            return;

        if (!NetWorkInfo.isOnline(view.getActivity())) {
            view.onNoInternet();
            return;
        }

        view.loginAccount();
    }

    private boolean validateData(String username, String password) {
        if (!validateText(username)) {
            view.onValidateError(view.getActivity().getString(R.string.error_input_username));
            return false;
        } else if (!validateText(password)) {
            view.onValidateError(view.getActivity().getString(R.string.error_input_password));
            return false;
        }
        return true;
    }

    public void startValidatePhone() {
        if (!PermissionHelper.checkListPermission(PermissionListAccKit, view.getActivity(), PERMISSION_REQUEST_CODE)) {
            return;
        }

        if (!NetWorkInfo.isOnline(view.getActivity())) {
            view.onNoInternet();
            return;
        }

        resetPhone();
    }

    private void resetPhone() {
        Intent intent = new Intent(view.getActivity(), AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder = new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE, AccountKitActivity.ResponseType.CODE);
        configurationBuilder.setDefaultCountryCode("VN");
        configurationBuilder.setTitleType(AccountKitActivity.TitleType.LOGIN);
        configurationBuilder.setReadPhoneStateEnabled(true);
        configurationBuilder.setReceiveSMS(true);
//        configurationBuilder.setInitialPhoneNumber(new PhoneNumber())
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configurationBuilder.build());
        view.startActivityForResult(intent, ACCOUNT_KIT_REQUEST_CODE);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (!NetWorkInfo.isOnline(view.getActivity())) {
                view.onNoInternet();
                return;
            }

            resetPhone();
        } else if (requestCode == LOGIN_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!NetWorkInfo.isOnline(view.getActivity())) {
                    view.onNoInternet();
                    return;
                }

                view.loginAccount();
            } else
                view.onValidateError(view.getActivity().getString(R.string.error_not_alllow_permission));
        }
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

                    view.onValidatePhoneToResetSuccess(authorization_code);
                }
                // Success! Start your next activity...
            }
        }
    }
}
