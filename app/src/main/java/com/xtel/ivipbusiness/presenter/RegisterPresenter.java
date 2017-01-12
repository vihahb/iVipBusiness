package com.xtel.ivipbusiness.presenter;

import android.content.Intent;
import android.util.Log;

import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.view.activity.inf.IRegisterView;

/**
 * Created by VULCL on 1/10/2017
 */

public class RegisterPresenter extends BasicPresenter {
    private IRegisterView view;
    private final int ACCOUNT_KIT_REQUEST_CODE = 99;

    public RegisterPresenter(IRegisterView view) {
        this.view = view;
    }

    public void registerAccount(String username, String password, String rePassword) {
        if (!validateData(username, password, rePassword))
            return;

        if (isPhone(username) != -1) {
            if (!validatePhone(username)) {
                view.onValidateError(view.getActivity().getString(R.string.error_validate_phone));
                return;
            }

            view.onRegisterAccount(username, password);
        } else {
            view.onValidateError(view.getActivity().getString(R.string.error_validate_phone));
        }
    }

    private boolean validateData(String username, String password, String rePassword) {
        if (!validateText(username)) {
            view.onValidateError(view.getActivity().getString(R.string.error_input_username));
            return false;
        } else if (!validateText(password)) {
            view.onValidateError(view.getActivity().getString(R.string.error_input_password));
            return false;
        } else if (!validateText(rePassword)) {
            view.onValidateError(view.getActivity().getString(R.string.error_input_re_password));
            return false;
        } else if (!password.equals(rePassword)) {
            view.onValidateError(view.getActivity().getString(R.string.error_invalidate_passwordd));
            return false;
        }

        return true;
    }

    public void startValidatePhone() {
        Intent intent = new Intent(view.getActivity(), AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder = new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE, AccountKitActivity.ResponseType.CODE);
        configurationBuilder.setDefaultCountryCode("VN");
        configurationBuilder.setTitleType(AccountKitActivity.TitleType.LOGIN);
        configurationBuilder.setReadPhoneStateEnabled(true);
        configurationBuilder.setReceiveSMS(true);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configurationBuilder.build());
        view.startActivityForResult(intent, ACCOUNT_KIT_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACCOUNT_KIT_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (loginResult.getError() != null) {
                view.onRegisterAccountSuccess();
            } else if (loginResult.wasCancelled()) {
                view.onRegisterAccountSuccess();
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