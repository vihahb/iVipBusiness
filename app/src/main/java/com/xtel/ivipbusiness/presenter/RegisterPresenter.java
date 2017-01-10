package com.xtel.ivipbusiness.presenter;

import android.text.TextUtils;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.view.activity.inf.IRegisterView;

/**
 * Created by VULCL on 1/10/2017
 */

public class RegisterPresenter extends BasicPresenter {
    private IRegisterView view;

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
}