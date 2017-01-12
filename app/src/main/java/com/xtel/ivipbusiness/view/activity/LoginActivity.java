package com.xtel.ivipbusiness.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.accountkit.AccountKit;
import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.presenter.LoginPresenter;
import com.xtel.ivipbusiness.view.activity.inf.ILoginView;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.CallbackListenerActive;
import com.xtel.nipservicesdk.callback.CallbackListenerReactive;
import com.xtel.nipservicesdk.callback.CallbackListenerReset;
import com.xtel.nipservicesdk.commons.Constants;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;
import com.xtel.nipservicesdk.model.entity.RESP_Reactive;
import com.xtel.nipservicesdk.utils.JsonHelper;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.xtel.sdk.callback.DialogListener;

/**
 * Created by Lê Công Long Vũ on 12/2/2016
 */

public class LoginActivity extends BasicActivity implements View.OnClickListener, ILoginView {
    private LoginPresenter presenter;
    private CallbackManager callbackManager;
    private EditText edt_username, edt_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AccountKit.initialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.create(this);

        presenter = new LoginPresenter(this);
        initToolbar(R.id.login_toolbar, null);
        initView();
    }

    private void initView() {
        edt_username = findEditText(R.id.login_edt_username);
        edt_password = findEditText(R.id.login_edt_pass);

        Button btn_login = findButton(R.id.login_btn_login);
        Button btn_active = findButton(R.id.login_btn_active);
        Button btn_register = findButton(R.id.login_btn_register);
        Button btn_forget = findButton(R.id.login_btn_forget);

        btn_login.setOnClickListener(this);
        btn_active.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_forget.setOnClickListener(this);
    }

    private void reActiveAccount(final String auth_id) {
        debug("reactive");
        callbackManager.reactiveNipAccount(edt_username.getText().toString(), true, new CallbackListenerReactive() {
            @Override
            public void onSuccess(RESP_Reactive reactive) {
                activeAccount(auth_id);
            }

            @Override
            public void onError(Error error) {
                closeProgressBar();
                showShortToast(JsonParse.getCodeMessage(error.getCode(), getString(R.string.error)));
            }
        });
    }

    private void activeAccount(String auth_id) {
        debug("active now");
        callbackManager.activeNipAccount(auth_id, getString(R.string.type_phone), new CallbackListenerActive() {
            @Override
            public void onSuccess() {
                closeProgressBar();
                showShortToast(getString(R.string.success_active));
            }

            @Override
            public void onError(Error error) {
                closeProgressBar();
                showShortToast(getString(R.string.error_active_account));
            }
        });
    }

    @Override
    public void onValidateError(String error) {
        showShortToast(error);
    }

    @Override
    public void loginAccount(final String phone, final String password) {
        showProgressBar(false, false, null, getString(R.string.doing_login));
        debug("login");
        callbackManager.LoginNipAcc(phone, password, true, new CallbacListener() {
            @Override
            public void onSuccess(RESP_Login success) {
                closeProgressBar();
                debug(JsonHelper.toJson(success));
                startActivityAndFinish(HomeActivity.class);
            }

            @Override
            public void onError(Error error) {
                closeProgressBar();
                debug(JsonHelper.toJson(error));
                showShortToast(JsonParse.getCodeMessage(error.getCode(), getString(R.string.error)));
            }
        });
    }

    @Override
    public void onValidatePhoneToActiveSuccess(String auth_id) {
        showProgressBar(false, false, null, getString(R.string.doing_active));
        reActiveAccount(auth_id);
    }

    @Override
    public void onValidatePhoneToResetSuccess(String auth_id) {
        Intent intent = new Intent(this, EnterPasswordActivity.class);
        intent.putExtra(Constants.USER_AUTH_ID, auth_id);
        startActivity(intent);
        finish();
    }

    @Override
    public void startActivityAndFinish(Class clazz) {
        super.startActivityAndFinish(clazz);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.login_btn_login) {
            presenter.loginAccount(edt_username.getText().toString(), edt_password.getText().toString());
        } else if (id == R.id.login_btn_register) {
            presenter.registerAccount();
        } else if (id == R.id.login_btn_active) {
            presenter.validatePhoneToActive(edt_username.getText().toString());
        } else if (id == R.id.login_btn_forget) {
            presenter.validatePhoneToReset();
        }
    }

    @Override
    public void onBackPressed() {
        startActivityAndFinish(ChooseLoginActivity.class);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivityAndFinish(ChooseLoginActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callbackManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }
}