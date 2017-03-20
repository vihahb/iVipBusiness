package com.xtel.ivipbusiness.view.activity;

import android.app.Activity;
import android.content.Intent;
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
import com.xtel.ivipbusiness.model.entity.RESP_Full_Profile;
import com.xtel.ivipbusiness.presenter.HomePresenter;
import com.xtel.ivipbusiness.view.activity.inf.IHomeView;
import com.xtel.ivipbusiness.view.fragment.ChainsFragment;
import com.xtel.ivipbusiness.view.fragment.StatisticFragment;
import com.xtel.ivipbusiness.view.widget.CircleTransform;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;

/**
 * Created by Lê Công Long Vũ on 12/2/2016
 */

public class HomeActivity extends BasicActivity implements NavigationView.OnNavigationItemSelectedListener, IHomeView {
    protected HomePresenter presenter;
    protected CallbackManager callbackManager;

    protected DrawerLayout drawer;
    protected NavigationView navigationView;
    protected ActionBar actionBar;
    protected MenuItem menu_avatar, menu_coupons;

    protected final String LIST_STORE = "list_store", STATISTIC = "statistic", POLICY = "policy", APP_INFO = "app_info", FAQ = "faq";
    protected final int REQUEST_STORE_INFO = 11, REQUEST_PROFILE = 22, REQUEST_ADD_STORE = 33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        callbackManager = CallbackManager.create(this);

        presenter = new HomePresenter(this);
        initView();
        initNavigationView();
        replaceListStore();
    }

    /*
    * Khởi tạo các view trong layout
    * */
    protected void initView() {
        drawer = findDrawerLayout(R.id.drawer_layout);
        navigationView = findNavigationView(R.id.nav_view);
    }

    /*
    * Khởi tạo navigation
    * */
    @SuppressWarnings("deprecation")
    protected void initNavigationView() {
        Toolbar toolbar = findToolbar(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    /*
    * Hiển thị menu item cho layout danh sách cửa hàng
    * */
    protected void showMenuListStore() {
        if (menu_avatar != null && menu_coupons != null) {
            menu_avatar.setVisible(true);
            menu_coupons.setVisible(true);
        }
    }

    /*
    * Ẩn tất cả các menu item
    * */
    protected void hideMenuItem() {
        menu_avatar.setVisible(false);
        menu_coupons.setVisible(false);
    }

    /*
    * Hiển thị màn hình danh sách cửa hàng
    * */
    protected void replaceListStore() {
        showMenuListStore();
        actionBar.setTitle(getString(R.string.title_activity_list_chain));
        replaceFragment(R.id.home_container, ChainsFragment.newInstance(), LIST_STORE);
    }

    /*
    * Hiển thị màn hình thống kê
    * */
    protected void replaceStatistic() {
        hideMenuItem();
        actionBar.setTitle(getString(R.string.title_activity_statistic));
        replaceFragment(R.id.home_container, StatisticFragment.newInstance(), STATISTIC);
    }

    /*
    * Hiển thị màn hình chính sách
    * */
    protected void replacePolicy() {
        hideMenuItem();
        actionBar.setTitle(getString(R.string.title_activity_policy));
        replaceFragment(R.id.home_container, ChainsFragment.newInstance(), POLICY);
    }

    /*
    * Hiển thị màn hình thông tin ứng dụng
    * */
    protected void replaceAppInfo() {
        hideMenuItem();
        actionBar.setTitle(getString(R.string.title_activity_app_info));
        replaceFragment(R.id.home_container, ChainsFragment.newInstance(), APP_INFO);
    }

    /*
    * Hiển thị màn hình faq
    * */
    protected void replaceFaq() {
        hideMenuItem();
        actionBar.setTitle(getString(R.string.title_activity_faq));
        replaceFragment(R.id.home_container, ChainsFragment.newInstance(), FAQ);
    }




    /*
    * Lấy thông tin user thành công
    * Load avatar lên menu item
    * */
    protected ImageView img_avatar;
    @Override
    public void onGetShortUserDataSuccess(RESP_Full_Profile obj) {
        if (obj != null)
            if (obj.getAvatar() != null) {
                if (img_avatar == null)
                    img_avatar = new ImageView(this);
                img_avatar.setVisibility(View.GONE);

                String finalUrl = obj.getAvatar().replace("https", "http").replace("9191", "9190");

                Picasso.with(HomeActivity.this)
                        .load(finalUrl)
                        .noPlaceholder()
                        .transform(new CircleTransform())
                        .error(R.mipmap.ic_launcher)
                        .into(img_avatar, new Callback() {
                            @Override
                            public void onSuccess() {
                                menu_avatar.setIcon(img_avatar.getDrawable());
                                img_avatar.destroyDrawingCache();
                            }

                            @Override
                            public void onError() {
                            }
                        });
            }
    }

    /*
    * Lấy thông tin data thất bại
    * */
    @Override
    public void onGetUserDataError() {
        presenter.getFullUserData();
    }

    /*
    * Đăng ký fcm thất bại
    * */
    @Override
    public void onRegisterFcmError() {
        presenter.registerFCm();
    }

    /*
    * Lấy session mới khi session cũ hết hạn
    * */
    @Override
    public void getNewSession(final ICmd iCmd, final int type) {
        callbackManager.getNewSesion(new CallbacListener() {
            @Override
            public void onSuccess(RESP_Login success) {
                iCmd.execute(type);
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
        menu_coupons = menu.findItem(R.id.action_home_coupons);

        presenter.getFullUserData();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_home_user_info)
            startActivityForResult(ProfileActivity.class, REQUEST_PROFILE);
        else if (id == R.id.action_home_coupons) {
            showShortToast("Dang xay dung");
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_STORE_INFO || requestCode == REQUEST_ADD_STORE) {
            ChainsFragment fragment = (ChainsFragment) getSupportFragmentManager().findFragmentByTag(LIST_STORE);
            if (fragment != null)
                fragment.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == REQUEST_PROFILE && resultCode == RESULT_OK) {
            presenter.getFullUserData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callbackManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}