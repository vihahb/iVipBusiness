package com.xtel.ivipbusiness.view.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.xtel.ivipbusiness.R;

public class ProfileActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initToolbar(R.id.profile_toolbar, null);
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
