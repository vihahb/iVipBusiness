package com.xtel.ivipbusiness.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.RESP_Short_Profile;
import com.xtel.ivipbusiness.presenter.HomePresenter;
import com.xtel.ivipbusiness.view.activity.inf.IHomeView;
import com.xtel.ivipbusiness.view.fragment.ChainsFragment;
import com.xtel.ivipbusiness.view.widget.CircleTransform;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.NetWorkInfo;

/**
 * Created by Lê Công Long Vũ on 12/2/2016
 */

public class HomeActivity extends BasicActivity implements NavigationView.OnNavigationItemSelectedListener, IHomeView {
    private HomePresenter presenter;

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBar actionBar;
    private MenuItem menu_avatar, menu_create_store, menu_create_chain;

    private final int REQUEST_ADD_STORE = 11;
    private final String CHAIN_TYPE = "CHAIN", STORE_TYPE = "STORE";
    private final String LIST_STORE = "list_store", STATISTIC = "statistic", POLICY = "policy", APP_INFO = "app_info", FAQ = "faq";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        presenter = new HomePresenter(this);
        initView();
        initNavigationView();
        replaceListStore();
//        presenter.getShortUserData();
    }

    //     Khởi tạo view
    private void initView() {
        drawer = findDrawerLayout(R.id.drawer_layout);
        navigationView = findNavigationView(R.id.nav_view);
    }

    //    Khởi tạo navigation
    @SuppressWarnings("deprecation")
    private void initNavigationView() {
        toolbar = findToolbar(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    //    Hiển thị danh sách cửa hàng
    private void replaceListStore() {
        actionBar.setTitle(getString(R.string.title_activity_list_chain));
        replaceFragment(R.id.home_container, ChainsFragment.newInstance(), LIST_STORE);
    }

    //    Hiển thị thống kê
    private void replaceStatistic() {
        actionBar.setTitle(getString(R.string.title_activity_statistic));
        replaceFragment(R.id.home_container, ChainsFragment.newInstance(), STATISTIC);
    }

    //    Hiển thị chính sách
    private void replacePolicy() {
        actionBar.setTitle(getString(R.string.title_activity_policy));
        replaceFragment(R.id.home_container, ChainsFragment.newInstance(), POLICY);
    }

    //    Hiển thị thông tin ứng dụng
    private void replaceAppInfo() {
        actionBar.setTitle(getString(R.string.title_activity_app_info));
        replaceFragment(R.id.home_container, ChainsFragment.newInstance(), APP_INFO);
    }

    //    Hiển thị faq
    private void replaceFaq() {
        actionBar.setTitle(getString(R.string.title_activity_faq));
        replaceFragment(R.id.home_container, ChainsFragment.newInstance(), FAQ);
    }














    @Override
    public void onGetShortUserDataSuccess(RESP_Short_Profile obj) {
        final ImageView imageView = new ImageView(this);
        imageView.setVisibility(View.GONE);

        Picasso.with(getApplicationContext())
                .load(obj.getAvatar())
                .noPlaceholder()
                .transform(new CircleTransform())
                .error(R.mipmap.ic_launcher)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        menu_avatar.setIcon(imageView.getDrawable());
                        imageView.destroyDrawingCache();
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    @Override
    public void onGetShortUserDataError(Error error) {

    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            showConfirmExitApp();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);

        menu_avatar = menu.findItem(R.id.action_home_user_info);
        menu_create_store = menu.findItem(R.id.action_home_create_store);
        menu_create_chain = menu.findItem(R.id.action_home_create_chain);

        presenter.getShortUserData();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_home_user_info)
            startActivity(ProfileActivity.class);
        else if (id == R.id.action_home_create_store) {
            if (!NetWorkInfo.isOnline(getApplicationContext())) {
                showShortToast(getString(R.string.error_no_internet));
            } else
            startActivityForResult(AddStoreActivity.class, Constants.MODEL, STORE_TYPE, REQUEST_ADD_STORE);
        } else if (id == R.id.action_home_create_chain) {
            if (!NetWorkInfo.isOnline(getApplicationContext())) {
                showShortToast(getString(R.string.error_no_internet));
            } else
                startActivityForResult(AddStoreActivity.class, Constants.MODEL, CHAIN_TYPE, REQUEST_ADD_STORE);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home_store) {
            replaceListStore();
        } else if (id == R.id.nav_home_statistic) {
            replaceStatistic();
        } else if (id == R.id.nav_home_policy) {
            replacePolicy();
        } else if (id == R.id.nav_home_app_info) {
            replaceAppInfo();
        } else if (id == R.id.nav_home_faq) {
            replaceFaq();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}