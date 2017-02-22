package com.xtel.ivipbusiness.view.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.presenter.ListFcmPresenter;
import com.xtel.ivipbusiness.view.activity.inf.IListFcmView;
import com.xtel.ivipbusiness.view.widget.ProgressView;

public class ListFcmActivity extends BasicActivity implements IListFcmView {
    private ListFcmPresenter presenter;
    private ProgressView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_fcm);

        presenter = new ListFcmPresenter(this);
        initToolbar(R.id.list_fcm_toolbar, null);
    }






    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
