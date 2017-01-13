package com.xtel.ivipbusiness.view.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xtel.ivipbusiness.R;
import com.xtel.nipservicesdk.LoginManager;

public class ProfileActivity extends BasicActivity {
    private TextView txt_total_stores, txt_name, txt_date_create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initToolbar(R.id.profile_toolbar, null);
        initView();
        initLogout();
    }

    private void initView() {

    }

    private void initLogout() {
        Button btn_logout = (findButton(R.id.profile_btn_logout));
        btn_logout.setOnClickListener(view -> {
            LoginManager.logOut();
            finishAffinity();
            startActivityAndFinish(LoginActivity.class);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.profile_action_edit:

                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
