package com.xtel.ivipbusiness.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.presenter.EnterPassPresenter;
import com.xtel.ivipbusiness.view.activity.inf.IEnterPassView;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.callback.CallbackListenerReset;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.xtel.sdk.callback.DialogListener;

public class EnterPasswordActivity extends BasicActivity implements View.OnClickListener, IEnterPassView {
    private EditText edt_new_pass, edt_re_passs;
    private EnterPassPresenter presenter;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);
        callbackManager = CallbackManager.create(this);

        presenter = new EnterPassPresenter(this);
//        initToolbar(R.id.enter_pass_toolbar, null);
        initView();
        presenter.getData();
    }

    /*
    * Khởi tạo các view trong layout
    * */
    private void initView() {
        edt_new_pass = findEditText(R.id.enter_pass_edt_new_pass);
        edt_re_passs = findEditText(R.id.enter_pass_edt_re_pass);

        Button btn_reset = findButton(R.id.enter_pass_btn_login);
        btn_reset.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.enter_pass_btn_login) {
            presenter.resetPassword(edt_new_pass.getText().toString(), edt_re_passs.getText().toString());
        }
    }

    /*
    * Thông báo lỗi khi nhận data hoặc không có data truyền sang
    * */
    @Override
    public void onGetDataError() {
        showShortToast(getString(R.string.error_can_not_resett));
        showMaterialDialog(false, false, null, getString(R.string.error_can_not_resett), null, getString(R.string.back), new DialogListener() {
            @Override
            public void negativeClicked() {

            }

            @Override
            public void positiveClicked() {
                closeDialog();
                startActivityAndFinish(LoginActivity.class);
            }
        });
    }

    /*
    * Thông báo lỗi khi kiểm tra dữ liệu
    * */
    @Override
    public void onValidateError(String error) {
        showShortToast(error);
    }

    /*
    * Lấy lại mật khẩu
    * */
    @Override
    public void onResetPassWord(String auth_id, String password) {
        showProgressBar(false, false, null, getString(R.string.doing_reset));
        callbackManager.AdapterReset(null, password, auth_id, new CallbackListenerReset() {
            @Override
            public void onSuccess() {
                closeProgressBar();
                showShortToast(getString(R.string.success_reset));
                startActivityAndFinish(LoginActivity.class);
            }

            @Override
            public void onError(Error error) {
                closeProgressBar();
                showShortToast(JsonParse.getCodeMessage(error.getCode(), getString(R.string.have_error)));
                startActivityAndFinish(LoginActivity.class);
            }
        });
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callbackManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            startActivityAndFinish(LoginActivity.class);
        return super.onOptionsItemSelected(item);
    }
}