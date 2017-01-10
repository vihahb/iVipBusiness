package com.xtel.ivipbusiness.presenter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.view.activity.RegisterActivity;
import com.xtel.ivipbusiness.view.activity.inf.ILoginView;
import com.xtel.sdk.utils.PermissionHelper;

/**
 * Created by VULCL on 1/10/2017
 */

public class LoginPresenter extends BasicPresenter {
    private ILoginView view;
//    private final int ACCOUNT_KIT_REQUEST_CODE = 99, PERMISSION_REQUEST_CODE = 10001;
//    private String[] PermissionListAccKit = {Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_STATE};
//
    public LoginPresenter(ILoginView view) {
        this.view = view;
    }

    public void registerAccount() {
        view.startActivityAndFinish(RegisterActivity.class);
    }

    public void loginAccount(String phone, String password) {
        debug("1");
        if (!validateData(phone, password))
            return;

        debug("2");
        if (isPhone(phone) == -1) {
            debug("6");
            view.onValidateError(view.getActivity().getString(R.string.error_validate_phone));
            return;
        }

        debug("3");
        if (!validatePhone(phone)) {
            debug("4");
            view.onValidateError(view.getActivity().getString(R.string.error_validate_phone));
            return;
        }

        debug("5");
        view.loginAccount(phone, password);
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

    public void reactiveAccount(String phone, String password) {

    }

//    public void loginByPhone() {
//        if (!PermissionHelper.checkListPermission(PermissionListAccKit, view.getActivity(), PERMISSION_REQUEST_CODE)) {
//            return;
//        }
//
//        Intent intent = new Intent(view.getActivity(), AccountKitActivity.class);
//        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder = new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE, AccountKitActivity.ResponseType.CODE);
//        configurationBuilder.setDefaultCountryCode("VN");
//        configurationBuilder.setTitleType(AccountKitActivity.TitleType.LOGIN);
//        configurationBuilder.setReadPhoneStateEnabled(true);
//        configurationBuilder.setReceiveSMS(true);
//        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configurationBuilder.build());
//        view.startLogin(intent, ACCOUNT_KIT_REQUEST_CODE);
//    }

//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == PERMISSION_REQUEST_CODE) {
//            boolean check = true;
//
//            for (int grand : grantResults) {
//                if (grand == PackageManager.PERMISSION_DENIED) {
//                    check = false;
//                    break;
//                }
//            }
//
//            if (check)
//                loginByPhone();
//        }
//    }

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == ACCOUNT_KIT_REQUEST_CODE) { // confirm that this response matches your request
//            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
//            if (loginResult.getError() != null) {
//                debug(loginResult.getError().getErrorType().getMessage());
//                debug(loginResult.getError().toString());
//            } else if (loginResult.wasCancelled()) {
//                debug("Login Cancelled");
//            } else {
//                if (loginResult.getAccessToken() != null) {
//                    debug("Success 1:" + loginResult.getAccessToken().getAccountId());
//
//                    AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
//                        @Override
//                        public void onSuccess(Account account) {
//                            debug("phone: " + String.valueOf(account.getPhoneNumber()));
//                        }
//
//                        @Override
//                        public void onError(AccountKitError accountKitError) {
//                            Log.e("Eror acc: ", accountKitError.toString());
//                        }
//                    });
//                } else {
//                    debug("Success 2: " + loginResult.getAuthorizationCode().substring(0, 10));
//
//                    try {
//                        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
//                            @Override
//                            public void onSuccess(Account account) {
//                                debug("phone: " + String.valueOf(account.getPhoneNumber()));
//                            }
//
//                            @Override
//                            public void onError(AccountKitError accountKitError) {
//                                Log.e("Eror acc: ", accountKitError.toString());
//                            }
//                        });
//                    } catch (Exception e) {
//                        debug("error: " + e.getMessage());
//                    }
//                }
//
//                // Success! Start your next activity...
//            }
//        }
//    }
}
