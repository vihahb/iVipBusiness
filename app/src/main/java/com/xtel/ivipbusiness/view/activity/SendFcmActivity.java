package com.xtel.ivipbusiness.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.Area;
import com.xtel.ivipbusiness.model.entity.LevelObject;
import com.xtel.ivipbusiness.presenter.SendFcmPresenter;
import com.xtel.ivipbusiness.view.activity.inf.ISendFcmView;
import com.xtel.ivipbusiness.view.adapter.AreaAdapter;
import com.xtel.ivipbusiness.view.adapter.GenderAdapter;
import com.xtel.ivipbusiness.view.adapter.LevelSpinnerAdapter;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.xtel.sdk.callback.DialogListener;
import com.xtel.sdk.utils.TextUnit;

import java.util.ArrayList;

public class SendFcmActivity extends BasicActivity implements View.OnClickListener, ISendFcmView {
    private SendFcmPresenter presenter;
    private CallbackManager callbackManager;

    private ActionBar actionBar;

    private TextView txt_level;
    private Spinner sp_gender, sp_level;
    private EditText edt_from, edt_to;

    private LevelSpinnerAdapter areaAdapter;
    private ArrayList<Area> arrayList_area;
    private ArrayList<LevelObject> arrayList_level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_fcm);
        callbackManager = CallbackManager.create(this);

        presenter = new SendFcmPresenter(this);
        initToolbar();
        initView();
        initGender();
        initSpinnerArea();
        initSpinnerLevel();
        presenter.getData();
    }

    protected void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.option_toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        edt_from = findEditText(R.id.option_edt_from);
        edt_to = findEditText(R.id.option_edt_to);
        txt_level = findTextView(R.id.option_txt_level);

//        Button btn_cancel = findButton(R.id.option_btn_cancel);
        Button btn_done = findButton(R.id.option_btn_done);

//        btn_cancel.setOnClickListener(this);
        btn_done.setOnClickListener(this);
    }

    //    Khởi tạo spinner để chọn giới tính
    private void initGender() {
        String[] arrayList = getResources().getStringArray(R.array.gender_notify);
        sp_gender = findSpinner(R.id.option_sp_gender);
        GenderAdapter genderAdapter = new GenderAdapter(this, arrayList);
        genderAdapter.setOutline(true);
        sp_gender.setAdapter(genderAdapter);
    }

    //    Khởi tạo spinner để chọn khu vực
    private void initSpinnerArea() {
        arrayList_area = new ArrayList<>();
        String[] list = getResources().getStringArray(R.array.area);
        for (String aList : list) {
            arrayList_area.add(new Area(false, aList));
        }

        Spinner sp_area = findSpinner(R.id.option_sp_area);
        AreaAdapter areaAdapter = new AreaAdapter(getApplicationContext(), true, arrayList_area);
        sp_area.setAdapter(areaAdapter);
    }

    //    Khởi tạo spinner chọn level
    private void initSpinnerLevel() {
        arrayList_level = new ArrayList<>();
        arrayList_level.add(new LevelObject(1, getString(R.string.updating), 1, null, null));

        sp_level = findSpinner(R.id.option_sp_level);
        areaAdapter = new LevelSpinnerAdapter(getApplicationContext(), false, arrayList_level);
        sp_level.setAdapter(areaAdapter);
        sp_level.setEnabled(false);
    }

    private void closeOption() {
        closeDialog();
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.option_btn_done:
                presenter.doneOption(arrayList_area, arrayList_level, sp_gender.getSelectedItemPosition(),
                        TextUnit.getInstance().validateInteger(edt_from.getText().toString()), TextUnit.getInstance().validateInteger(edt_to.getText().toString()));
                break;
            default:
                break;
        }
    }

    protected void showError(String message) {
        showMaterialDialog(false, false, null, message, null, getString(R.string.back), new DialogListener() {
            @Override
            public void negativeClicked() {

            }

            @Override
            public void positiveClicked() {
                closeDialog();
                finish();
            }
        });
    }










    @Override
    public void onGetDataSuccess(int type) {
        if (type == 1) {
            actionBar.setTitle(R.string.title_activity_send_group);
            txt_level.setVisibility(View.GONE);
            sp_level.setVisibility(View.GONE);
        } else if (type == 2)
            actionBar.setTitle(R.string.title_activity_send_member);
    }

    @Override
    public void onGetDataError() {
        showMaterialDialog(false, false, null, getString(R.string.error_try_again), null, getString(R.string.back), new DialogListener() {
            @Override
            public void negativeClicked() {
                closeOption();
            }

            @Override
            public void positiveClicked() {
                closeOption();
            }
        });
    }

    @Override
    public void onGetSettingSuccess(ArrayList<LevelObject> arrayList) {
        arrayList_level.clear();
        arrayList_level.addAll(arrayList);
        areaAdapter.notifyDataSetChanged();
        sp_level.setEnabled(true);
    }

    @Override
    public void onRequestError(Error error) {
        if (error.getCode() == 201) {
            showError(getString(R.string.error_not_have_level));
        } else {
            showError(JsonParse.getCodeMessage(error.getCode(), getString(R.string.error_try_again)));
        }
    }

    @Override
    public void getNewSession(final ICmd iCmd) {
        callbackManager.getNewSesion(new CallbacListener() {
            @Override
            public void onSuccess(RESP_Login success) {
                iCmd.execute();
            }

            @Override
            public void onError(Error error) {
                closeProgressBar();
                showShortToast(getString(R.string.error_end_of_session));
                getActivity().finishAffinity();
                startActivity(LoginActivity.class);
            }
        });
    }

    @Override
    public void showShortToast(int type, String message) {
        if (type == 1) {
            showShortToast(message);
            edt_from.requestFocus();
        } else  if (type == 2) {
            showShortToast(message);
            edt_to.requestFocus();
        } else
            showShortToast(message);
    }

    @Override
    public Activity getActivity() {
        return this;
    }










    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callbackManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            closeOption();
        return super.onOptionsItemSelected(item);
    }
}