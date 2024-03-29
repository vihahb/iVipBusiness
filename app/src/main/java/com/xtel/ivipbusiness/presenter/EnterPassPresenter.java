package com.xtel.ivipbusiness.presenter;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.view.activity.inf.IEnterPassView;
import com.xtel.nipservicesdk.commons.Cts;
import com.xtel.sdk.utils.TextUnit;

/**
 * Created by Mr. M.2 on 1/12/2017
 */

public class EnterPassPresenter extends BasicPresenter {
    private IEnterPassView view;
    private String auth_id;

    public EnterPassPresenter(IEnterPassView view) {
        this.view = view;
    }

    public void getData() {
        try {
            auth_id = view.getActivity().getIntent().getExtras().getString(Cts.USER_AUTH_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (auth_id == null) {
            view.onGetDataError();
        }
    }

    public void resetPassword(String new_pass, String re_pass) {
        if (validateData(new_pass, re_pass)){
            view.onResetPassWord(auth_id, new_pass);
        }
    }

    private boolean validateData(String password, String rePassword) {
        if (!TextUnit.getInstance().validateText(password)) {
            view.onValidateError(view.getActivity().getString(R.string.error_input_new_password));
            return false;
        }
        if (!TextUnit.getInstance().validateLengthText(password, 6)) {
            view.onValidateError(view.getActivity().getString(R.string.error_input_length_password));
            return false;
        }
        if (!TextUnit.getInstance().validateText(rePassword)) {
            view.onValidateError(view.getActivity().getString(R.string.error_input_re_password));
            return false;
        }
        if (!password.equals(rePassword)) {
            view.onValidateError(view.getActivity().getString(R.string.error_invalidate_passwordd));
            return false;
        }

        return true;
    }
}
