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
import com.xtel.nipservice.CallbackManager;
import com.xtel.nipservice.callback.CallbacListener;
import com.xtel.nipservice.commons.Cts;
import com.xtel.nipservice.model.entity.Error;
import com.xtel.nipservice.model.entity.RESP_Login;
import com.xtel.nipservice.utils.JsonHelper;
import com.xtel.nipservice.utils.JsonParse;

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

    @Override
    public void onValidateError(String error) {
        showShortToast(error);
    }

    @Override
    public void loginAccount() {
        showProgressBar(false, false, null, getString(R.string.doing_login));
        debug("login");
        callbackManager.LoginNipAcc(edt_username.getText().toString(), edt_password.getText().toString(), true, new CallbacListener() {
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
    public void onValidatePhoneToResetSuccess(String auth_id) {
        Intent intent = new Intent(this, EnterPasswordActivity.class);
        intent.putExtra(Cts.USER_AUTH_ID, auth_id);
        startActivity(intent);
    }

    @Override
    public void onNoInternet() {
        showShortToast(getString(R.string.error_no_internet));
    }

    @Override
    public void startActivity(Class clazz) {
        super.startActivity(clazz);
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
            startActivity(ActiveActivity.class);
        } else if (id == R.id.login_btn_forget) {
            presenter.startValidatePhone();
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