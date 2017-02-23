package com.xtel.ivipbusiness.view.activity;

import android.os.Bundle;
import android.widget.Spinner;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.Area;
import com.xtel.ivipbusiness.view.adapter.AreaAdapter;
import com.xtel.ivipbusiness.view.adapter.GenderAdapter;

import java.util.ArrayList;

public class ChooseOptionFcmActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_option_fcm);

        getData();
        initView();
        initGender();
        initSpinnerLevel();
        initSpinnerArea();
    }

    private void getData() {

    }

    private void initView() {

    }

    //    Khởi tạo spinner để chọn giới tính
    private void initGender() {
        String[] arrayList = getResources().getStringArray(R.array.gender);
        Spinner sp_gender = findSpinner(R.id.option_sp_gender);
        GenderAdapter genderAdapter = new GenderAdapter(this, arrayList);
        sp_gender.setAdapter(genderAdapter);
    }

    //    Khởi tạo spinner để chọn level
    private void initSpinnerLevel() {
        ArrayList<Area> arrayList = new ArrayList<>();
        String[] list = getResources().getStringArray(R.array.level);
        for (String aList : list) {
            arrayList.add(new Area(false, aList));
        }

        Spinner spinner = findSpinner(R.id.option_sp_level);
        AreaAdapter areaAdapter = new AreaAdapter(getApplicationContext(), arrayList);
        spinner.setAdapter(areaAdapter);
    }

    //    Khởi tạo spinner để chọn khu vực
    private void initSpinnerArea() {
        ArrayList<Area> arrayList = new ArrayList<>();
        String[] list = getResources().getStringArray(R.array.area);
        for (String aList : list) {
            arrayList.add(new Area(false, aList));
        }

        Spinner spinner = findSpinner(R.id.option_sp_area);
        AreaAdapter areaAdapter = new AreaAdapter(getApplicationContext(), arrayList);
        spinner.setAdapter(areaAdapter);
    }
}