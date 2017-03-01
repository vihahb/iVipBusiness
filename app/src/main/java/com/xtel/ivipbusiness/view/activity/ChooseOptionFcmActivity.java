package com.xtel.ivipbusiness.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.Area;
import com.xtel.ivipbusiness.model.entity.NotifyCodition;
import com.xtel.ivipbusiness.view.adapter.AreaAdapter;
import com.xtel.ivipbusiness.view.adapter.GenderAdapter;
import com.xtel.nipservicesdk.utils.JsonHelper;
import com.xtel.sdk.callback.DialogListener;
import com.xtel.sdk.commons.Constants;

import java.util.ArrayList;

import jp.wasabeef.blurry.Blurry;

public class ChooseOptionFcmActivity extends BasicActivity implements View.OnClickListener {
    private ActionBar actionBar;

    private TextView txt_level;
    private Spinner sp_gender, sp_level;
    private EditText edt_from, edt_to;

    private ArrayList<Area> arrayList_area, arrayList_level;
    private int news_type = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_option_fcm);

        initToolbar();
        initView();
        initGender();
        initSpinnerLevel();
        initSpinnerArea();

        getData();
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

    //    Khởi tạo spinner để chọn level
    private void initSpinnerLevel() {
        arrayList_level = new ArrayList<>();
        String[] list = getResources().getStringArray(R.array.level);
        for (String aList : list) {
            arrayList_level.add(new Area(false, aList));
        }

        sp_level = findSpinner(R.id.option_sp_level);
        AreaAdapter areaAdapter = new AreaAdapter(getApplicationContext(), false, arrayList_level);
        sp_level.setAdapter(areaAdapter);
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

    private void getData() {
        try {
            news_type = getIntent().getIntExtra(Constants.MODEL, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (news_type == -1)
            onGetDataError();
        else if (news_type == 1) {
            actionBar.setTitle(R.string.title_activity_send_group);
            txt_level.setVisibility(View.GONE);
            sp_level.setVisibility(View.GONE);
        } else if (news_type == 2)
            actionBar.setTitle(R.string.title_activity_send_member);
    }

    private void onGetDataError() {
        showMaterialDialog(false, false, null, getString(R.string.error_try_again), null, getString(R.string.back), new DialogListener() {
            @Override
            public void onClicked(Object object) {
                closeOption();
            }

            @Override
            public void onCancel() {
                closeOption();
            }
        });
    }

    private void closeOption() {
        closeDialog();
        setResult(RESULT_CANCELED);
        finish();
    }

    private void doneOption() {
        Integer[] area = checkArea();
        int gender = sp_gender.getSelectedItemPosition();
        Integer[] level = checkLevel();
        int from_age = validateInteger(edt_from.getText().toString());
        int to_age = validateInteger(edt_to.getText().toString());

        if (!checkData(area, gender, level, from_age, to_age))
            return;

        NotifyCodition notifyCodition = new NotifyCodition();
        notifyCodition.setAreas(area);
        notifyCodition.setGender(gender);
        notifyCodition.setLevel(level);
        notifyCodition.setFrom_age(from_age);
        notifyCodition.setTo_age(to_age);

        Log.e("NotifyCodition", JsonHelper.toJson(notifyCodition));

        Intent intent = new Intent();
        intent.putExtra(Constants.MODEL, notifyCodition);
        setResult(RESULT_OK, intent);
        finish();
    }

    private boolean checkData(Integer[] area, int gender, Integer[] level, int from_age, int to_age) {
        if (area.length == 0) {
            showShortToast(getString(R.string.error_input_area));
            return false;
        }
        if (gender == 0) {
            showShortToast(getString(R.string.error_input_gender));
            return false;
        }

        if (news_type == 2)
            if (level.length == 0) {
                showShortToast(getString(R.string.error_input_level));
                return false;
            }

        if (from_age == -1) {
            showShortToast(getString(R.string.error_input_age));
            edt_from.requestFocus();
            return false;
        }
        if (to_age == -1) {
            showShortToast(getString(R.string.error_input_age));
            edt_to.requestFocus();
            return false;
        }

        return true;
    }

    protected int validateInteger(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    private Integer[] checkArea() {
        int total = 0;

        for (int i = arrayList_area.size() - 1; i >= 0; i--) {
            if (arrayList_area.get(i).isSelected())
                total++;
        }

        Integer[] area = new Integer[total];

        if (area.length == 0)
            return area;

        total = 0;
        for (int i = 0; i < arrayList_area.size(); i++) {
            if (arrayList_area.get(i).isSelected()) {
                area[total] = (i + 1);
                total++;
            }
        }

        return area;
    }

    private Integer[] checkLevel() {
        int total = 0;

        for (int i = arrayList_level.size() - 1; i >= 0; i--) {
            if (arrayList_level.get(i).isSelected())
                total++;
        }

        Integer[] level = new Integer[total];

        if (level.length == 0)
            return level;

        total = 0;
        for (int i = 0; i < arrayList_level.size(); i++) {
            if (arrayList_level.get(i).isSelected()) {
                level[total] = (i + 1);
                total++;
            }
        }

        return level;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.option_btn_done:
                doneOption();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            closeOption();
        return super.onOptionsItemSelected(item);
    }
}