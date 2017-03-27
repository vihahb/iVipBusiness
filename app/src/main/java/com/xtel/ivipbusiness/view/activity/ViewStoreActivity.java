package com.xtel.ivipbusiness.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.RESP_Store;
import com.xtel.ivipbusiness.presenter.ViewStorePresenter;
import com.xtel.ivipbusiness.view.activity.inf.IViewStoreView;
import com.xtel.ivipbusiness.view.fragment.GalleryFragment;
import com.xtel.ivipbusiness.view.fragment.MemberFragment;
import com.xtel.ivipbusiness.view.fragment.NewsFragment;
import com.xtel.ivipbusiness.view.fragment.StoreInfoFragment;
import com.xtel.ivipbusiness.view.fragment.StoresFragment;
import com.xtel.nipservicesdk.utils.JsonHelper;
import com.xtel.nipservicesdk.utils.PermissionHelper;
import com.xtel.sdk.callback.DialogListener;
import com.xtel.sdk.commons.Constants;

public class ViewStoreActivity extends BasicActivity implements BottomNavigationView.OnNavigationItemSelectedListener, IViewStoreView {
    private ViewStorePresenter presenter;

    private ActionBar actionBar;
    private BottomNavigationView bottomNavigationView;
    private MenuItem menu_save_point, menu_setting, menu_edi_storet;

    private RESP_Store resp_store = null;
    private final int REQUEST_RESIZE_IMAGE = 8, REQUEST_ADD_STORE = 11, REQUEST_ADD_NEWS = 22, REQUEST_ADD_GALLERY = 33, REQUEST_CAMERA = 99;
    private final String STORE_TYPE = "STORE";
    private final String STORE_INFO = "store_info", LIST_STORE = "list_store", LIST_MENBER = "list_member", LIST_NEWS = "list_news", GALLERY = "gallery";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_store);

        presenter = new ViewStorePresenter(this);
        initToolbar();
        initBottomNavigationView();
        presenter.getData();
    }

    //    Khởi tạo toolbar
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.view_store_toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initBottomNavigationView() {
        bottomNavigationView = findBottomNavigationView(R.id.view_store_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setEnabled(false);
    }

    //    Hiện menu của tab thông tin cửa hàng
    private void showMenuStoreInfo() {
        if (menu_edi_storet != null && menu_setting != null) {
            menu_setting.setVisible(true);
            menu_edi_storet.setVisible(true);

            if (Constants.SORT_STORE.getStore_type().equals(STORE_TYPE))
                menu_save_point.setVisible(true);
        }
    }

    //    Hiện menu của tab danh sách cửa hàng của chuỗi cửa hàng
    private void showMenuListNew() {
        if (menu_edi_storet != null && menu_setting != null) {
            menu_edi_storet.setVisible(false);
//            menu_save_point.setVisible(false);
            menu_setting.setVisible(false);
        }
    }

    //    Hiện menu của tab bản tin
    private void showMenuNews() {
        if (menu_edi_storet != null && menu_setting != null) {
            menu_edi_storet.setVisible(false);
//            menu_save_point.setVisible(false);
            menu_setting.setVisible(false);
        }
    }

    //    Hiện menu của tab bản tin
    private void showMenuGallery() {
        if (menu_edi_storet != null && menu_setting != null) {
            menu_edi_storet.setVisible(false);
//            menu_save_point.setVisible(false);
            menu_setting.setVisible(false);
        }
    }

    //    Ản toàn bộ item trong menu
    private void hideMenuItem() {
        if (menu_edi_storet != null && menu_setting != null) {
            menu_edi_storet.setVisible(false);
//            menu_save_point.setVisible(false);
            menu_setting.setVisible(false);
        }
    }

    //    hiển thị fratment thông tin store
    private void replaceStoreInfo() {
        actionBar.setTitle(getString(R.string.title_activity_view_store));
        replaceFragment(R.id.view_store_container, StoreInfoFragment.newInstance(), STORE_INFO);
        showMenuStoreInfo();
    }

    //    hiển thị fratment danh sách chuỗi store
    private void replaceListStore() {
        actionBar.setTitle(getString(R.string.title_activity_list_store));
        replaceFragment(R.id.view_store_container, StoresFragment.newInstance(), LIST_STORE);
        showMenuListNew();
    }

    //    hiển thị fratment member
    private void replaceListMember() {
        actionBar.setTitle(getString(R.string.title_activity_list_member));
        replaceFragment(R.id.view_store_container, MemberFragment.newInstance(), LIST_MENBER);
        hideMenuItem();
    }

    //    hiển thị fratment bản tin
    private void replaceListNews() {
        actionBar.setTitle(getString(R.string.title_activity_list_news));
        replaceFragment(R.id.view_store_container, NewsFragment.newInstance(), LIST_NEWS);
        showMenuNews();
    }

    //    hiển thị fratment bản tin gần đây
    private void replaceGallery() {
        actionBar.setTitle(getString(R.string.title_activity_gallery));
        replaceFragment(R.id.view_store_container, GalleryFragment.newInstance(), GALLERY);
        showMenuGallery();
    }

    public RESP_Store getResp_store() {
        return resp_store;
    }

    public void setResp_store(RESP_Store resp_store) {
        this.resp_store = resp_store;
    }

    public void changeMenuIcon(int id) {
        menu_edi_storet.setIcon(id);
    }


    @Override
    public void onGetDataSuccess() {
        Log.e(this.getClass().getSimpleName(), "sortStore " + JsonHelper.toJson(Constants.SORT_STORE));

        replaceListNews();
        bottomNavigationView.setEnabled(true);
        if (Constants.SORT_STORE.getStore_type().equals(STORE_TYPE))
            bottomNavigationView.getMenu().findItem(R.id.nav_view_store_list_store).setEnabled(false);
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
    public Activity getActivity() {
        return this;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_store, menu);

        menu_save_point = menu.findItem(R.id.action_view_store_save_point);
        menu_setting = menu.findItem(R.id.action_view_store_setting_store);
        menu_edi_storet = menu.findItem(R.id.action_view_store_edit_store);

        menu_save_point.setVisible(true);
        menu_edi_storet.setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        else if (id == R.id.action_view_store_setting_store) {
            startActivity(SettingActivity.class, Constants.MODEL, Constants.SORT_STORE);
        } else if (id == R.id.action_view_store_edit_store) {
            StoreInfoFragment fragment = (StoreInfoFragment) getSupportFragmentManager().findFragmentByTag(STORE_INFO);

            if (fragment != null) {
                if (menu_edi_storet.getIcon().getConstantState() == (getResources().getDrawable(R.drawable.ic_action_edit_line).getConstantState())) {
                    fragment.enableToEdit();
                } else {
                    fragment.updateStore();
                }
            }
        } else if (id == R.id.action_view_store_save_point) {
            if (PermissionHelper.checkOnlyPermission(Manifest.permission.CAMERA, this, REQUEST_CAMERA))
                startActivity(CheckInUserActivity.class, Constants.MODEL, Constants.SORT_STORE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_view_store_info:
                replaceStoreInfo();
                break;
            case R.id.nav_view_store_list_store:
                if (Constants.SORT_STORE.getStore_type().equals(STORE_TYPE))
                    return false;
                replaceListStore();
                break;
            case R.id.nav_view_store_member:
                replaceListMember();
                break;
            case R.id.nav_view_store_news:
                replaceListNews();
                break;
            case R.id.nav_view_store_gallery:
                replaceGallery();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startActivity(CheckInUserActivity.class, Constants.MODEL, Constants.SORT_STORE);
            else
                showShortToast(getString(R.string.error_permission));
        } else {
            StoreInfoFragment fragment = (StoreInfoFragment) getSupportFragmentManager().findFragmentByTag(STORE_INFO);
            if (fragment != null)
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);

            GalleryFragment galleryFragment = (GalleryFragment) getSupportFragmentManager().findFragmentByTag(GALLERY);
            if (galleryFragment != null)
                galleryFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        debug(requestCode + "   " + resultCode);

        if (requestCode == REQUEST_ADD_STORE) {
            StoresFragment fragment = (StoresFragment) getSupportFragmentManager().findFragmentByTag(LIST_STORE);
            if (fragment != null)
                fragment.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == REQUEST_ADD_NEWS) {
            NewsFragment fragment = (NewsFragment) getSupportFragmentManager().findFragmentByTag(LIST_NEWS);
            if (fragment != null)
                fragment.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == REQUEST_ADD_GALLERY || requestCode == REQUEST_RESIZE_IMAGE) {
            GalleryFragment galleryFragment = (GalleryFragment) getSupportFragmentManager().findFragmentByTag(GALLERY);
            if (galleryFragment != null)
                galleryFragment.onActivityResult(requestCode, resultCode, data);
        } else {
            StoreInfoFragment fragment = (StoreInfoFragment) getSupportFragmentManager().findFragmentByTag(STORE_INFO);
            if (fragment != null)
                fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}