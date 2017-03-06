package com.xtel.ivipbusiness.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.LevelObject;
import com.xtel.ivipbusiness.model.entity.RESP_Setting;
import com.xtel.ivipbusiness.presenter.SettingPresenter;
import com.xtel.ivipbusiness.view.activity.inf.ISettingView;
import com.xtel.ivipbusiness.view.adapter.LevelAdapter;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.xtel.sdk.callback.DialogListener;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.WidgetHelper;

import java.util.ArrayList;

public class SettingActivity extends BasicActivity implements ISettingView {
    protected SettingPresenter presenter;
    protected CallbackManager callbackManager;

    protected SwipeRefreshLayout swipeRefreshLayout;
    protected ActionBar actionBar;
    protected EditText edt_money_to_point, edt_money_from_point;
    private Button btn_add;

    protected LevelAdapter levelAdapter;
    protected ArrayList<LevelObject> arrayList;

    protected MenuItem menuItem;
    protected final int REQUEST_ADD_LEVEL = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        presenter = new SettingPresenter(this);
        callbackManager = CallbackManager.create(this);

        initToolbar();
        initSwwipe();
        initView();
        initRecyclerview();
        initListener();
        setEnableWidget(false);
        presenter.getData();
    }

    //    Khởi tạo toolbar
    protected void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    //    Khởi tạo swipeRefreshLayout để hiển thị load thông tin
    protected void initSwwipe() {
        swipeRefreshLayout = findSwipeRefreshLayout(R.id.update_news_swipe);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
    }

    protected void initView() {
        edt_money_to_point = findEditText(R.id.setting_edt_money_to_point);
        edt_money_from_point = findEditText(R.id.setting_edt_money_from_point);
    }

    protected void initRecyclerview() {
        RecyclerView recyclerView = findRecyclerView(R.id.setting_recyclerview);
        recyclerView.setHasFixedSize(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        arrayList = new ArrayList<>();
        levelAdapter = new LevelAdapter(SettingActivity.this, arrayList);
        recyclerView.setAdapter(levelAdapter);
    }

    protected void initListener() {
        btn_add = findButton(R.id.setting_btn_add_level);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!swipeRefreshLayout.isRefreshing()) {
                    Log.e("level_send", "level " + (arrayList.size() + 1));
                    startActivityForResult(AddLevelActivity.class, Constants.MODEL, (arrayList.size() + 1), REQUEST_ADD_LEVEL);
                }
            }
        });
    }

    protected void setEnableWidget(boolean isEnable) {
        edt_money_from_point.setEnabled(isEnable);
        edt_money_to_point.setEnabled(isEnable);
        btn_add.setEnabled(isEnable);
        levelAdapter.setEnable(isEnable);
    }

    protected void onAddLevelSuccess(Intent intent) {
        LevelObject levelObject = null;

        try {
            levelObject = (LevelObject) intent.getSerializableExtra(Constants.MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (levelObject != null) {
            levelAdapter.addLevel(levelObject);
            Log.e("SettingActivity", "add_ok");
        } else {
            Log.e("SettingActivity", "add_not_ok");
        }
    }


















    @Override
    public void onGetDataSuccess(boolean isChain) {
        if (isChain)
            actionBar.setTitle(getString(R.string.title_activity_setting_chain));
    }

    @Override
    public void onGetSettingSuccess(RESP_Setting resp_setting) {
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(false);

        if (resp_setting != null) {
            WidgetHelper.getInstance().setEditTextNoResult(edt_money_to_point, resp_setting.getMoney2Points().get(0).getValue());
            WidgetHelper.getInstance().setEditTextNoResult(edt_money_from_point, resp_setting.getPoint2Moneys().get(0).getValue());

            arrayList.addAll(resp_setting.getLevels());
            levelAdapter.notifyDataSetChanged();
        } else {
            menuItem.setIcon(R.drawable.ic_action_done_2);
            setEnableWidget(true);
        }
    }

    @Override
    public void getNewSession(final ICmd iCmd, final Object... params) {
        callbackManager.getNewSesion(new CallbacListener() {
            @Override
            public void onSuccess(RESP_Login success) {
                iCmd.execute(params);
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
    public void onAddSettingSuccess() {
        menuItem.setIcon(R.drawable.ic_action_edit_line);
        setEnableWidget(false);

        closeProgressBar();
        showMaterialDialog(true, true, null, getString(R.string.success_update_setting), null, getString(R.string.ok), new DialogListener() {
            @Override
            public void negativeClicked() {

            }

            @Override
            public void positiveClicked() {
                closeDialog();
            }
        });
    }

    @Override
    public void onGetDataError() {
        showMaterialDialog(false, false, null, getString(R.string.error_try_again), null, getString(R.string.back), new DialogListener() {
            @Override
            public void negativeClicked() {
                closeDialog();
                finish();
            }

            @Override
            public void positiveClicked() {
                closeDialog();
                finish();
            }
        });
    }

    @Override
    public void onRequestError(Error error) {
        closeProgressBar();
        showShortToast(JsonParse.getCodeMessage(error.getCode(), getString(R.string.error_try_again)));
    }

    @Override
    public void showShortToast(int type, String message) {
        showShortToast(message);

        if (type == 1)
            edt_money_to_point.requestFocus();
        else if (type == 2)
            edt_money_from_point.requestFocus();
    }

    @Override
    public void showProgressBar(boolean isTouchOutside, boolean isCancel, String title, String message) {
        super.showProgressBar(isTouchOutside, isCancel, title, message);
    }

    @Override
    public Activity getActivity() {
        return this;
    }























    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        menuItem = menu.findItem(R.id.action_setting_done);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home)
            finish();
        else if (id == R.id.action_setting_done) {
            if (!swipeRefreshLayout.isRefreshing()) {
                if (menuItem.getIcon().getConstantState() == (getResources().getDrawable(R.drawable.ic_action_edit_line).getConstantState())) {
                    menuItem.setIcon(R.drawable.ic_action_done_2);
                    setEnableWidget(true);
                } else {
                    presenter.addSetting(edt_money_to_point.getText().toString(), edt_money_from_point.getText().toString(), arrayList);
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callbackManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD_LEVEL && resultCode == RESULT_OK)
            onAddLevelSuccess(data);
    }
}