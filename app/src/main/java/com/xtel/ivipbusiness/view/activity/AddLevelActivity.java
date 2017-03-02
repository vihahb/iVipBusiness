package com.xtel.ivipbusiness.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.LevelObject;
import com.xtel.sdk.callback.DialogListener;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.TextUnit;

public class AddLevelActivity extends BasicActivity {

    protected EditText edt_limit, edt_name;
    protected int LEVEL = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_level);

        initToolbar(R.id.add_level_toolbar, null);
        getData();

    }

    protected void getData() {
        try {
            LEVEL = getIntent().getIntExtra(Constants.MODEL, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (LEVEL != -1) {
            initView();
            initListener();
        } else
            showMaterialDialog(false, false, null, getString(R.string.error_try_again), null, getString(R.string.back), new DialogListener() {
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

    protected void initView() {
        edt_limit = findEditText(R.id.add_level_edt_limit);
        edt_name = findEditText(R.id.add_level_edt_name);
    }

    protected void initListener() {
        Button btn_done = findButton(R.id.add_level_btn_done);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done();
            }
        });
    }

    protected boolean checkData() {
        if (TextUnit.getInstance().validateInteger(edt_limit.getText().toString()) == -1) {
            showShortToast(getString(R.string.error_input_min_point));
            return false;
        }
        if (!TextUnit.getInstance().validateText(edt_name.getText().toString())) {
            showShortToast(getString(R.string.error_input_level_name));
            return false;
        }

        return true;
    }

    protected void done() {
        if (checkData()) {
            LevelObject levelObject = new LevelObject();
            levelObject.setLevel_limit(Integer.parseInt(edt_limit.getText().toString()));
            levelObject.setLevel_name(edt_name.getText().toString());
            levelObject.setLevel(LEVEL);

            Intent intent = new Intent();
            intent.putExtra(Constants.MODEL, levelObject);
            setResult(RESULT_OK, intent);
            finish();
        }
    }











    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}