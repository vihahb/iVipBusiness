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

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.presenter.HomePresenter;
import com.xtel.ivipbusiness.view.activity.inf.IHomeView;
import com.xtel.ivipbusiness.view.fragment.ChainsFragment;

/**
 * Created by Lê Công Long Vũ on 12/2/2016
 */

public class HomeActivity extends BasicActivity implements NavigationView.OnNavigationItemSelectedListener, IHomeView {
    private HomePresenter presenter;

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBar actionBar;

    private final String LIST_STORE = "list_store", STATISTIC = "statistic", POLICY = "policy", APP_INFO = "app_info", FAQ = "faq";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        presenter = new HomePresenter(this);
        initView();
        initNavigationView();
        replaceListStore();
    }

    //     Khởi tạo view
    private void initView() {
        drawer = findDrawerLayout(R.id.drawer_layout);
        navigationView = findNavigationView(R.id.nav_view);
    }

    //    Khởi tạo navigation
    @SuppressWarnings("deprecation")
    private void initNavigationView() {
        Toolbar toolbar = findToolbar(R.id.home_toolbar);
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
        actionBar.setTitle(getString(R.string.title_activity_list_store));
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
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home_action_user_info) {
            startActivity(ProfileActivity.class);
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

    @Override
    public Activity getActivity() {
        return this;
    }
}