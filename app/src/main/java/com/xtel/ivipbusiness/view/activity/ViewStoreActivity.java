package com.xtel.ivipbusiness.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.view.fragment.ListStoresFragment;
import com.xtel.ivipbusiness.view.fragment.StoreInfoFragment;

public class ViewStoreActivity extends BasicActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private ActionBar actionBar;

    private final String STORE_INFO = "store_info", LIST_STORE = "list_store", LIST_MENBER = "list_member", LIST_NEWS = "list_news", LIST_NEAR_NEWS = "list_near_news";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_store);

        initToolbar();
        initView();
        replaceStoreInfo();
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

    //    Khởi tạo toàn bọ Widget
    private void initView() {
        BottomNavigationView bottomNavigationView = findBottomNavigationView(R.id.view_store_bnv_tab);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    //    hiển thị fratment thông tin store
    private void replaceStoreInfo() {
        actionBar.setTitle(getString(R.string.title_activity_view_store));
        replaceFragment(R.id.view_store_container, StoreInfoFragment.newInstance(), STORE_INFO);
    }

    //    hiển thị fratment danh sách store
    private void replaceListStore() {
        actionBar.setTitle(getString(R.string.title_activity_list_store));
        replaceFragment(R.id.view_store_container, ListStoresFragment.newInstance(), LIST_STORE);
    }

    //    hiển thị fratment member
    private void replaceListMember() {
        actionBar.setTitle(getString(R.string.title_activity_list_member));
        replaceFragment(R.id.view_store_container, StoreInfoFragment.newInstance(), LIST_MENBER);
    }

    //    hiển thị fratment bản tin
    private void replaceListNews() {
        actionBar.setTitle(getString(R.string.title_activity_list_news));
        replaceFragment(R.id.view_store_container, StoreInfoFragment.newInstance(), LIST_NEWS);
    }

    //    hiển thị fratment bản tin gần đây
    private void replaceListNearNews() {
        actionBar.setTitle(getString(R.string.title_activity_list_near_news));
        replaceFragment(R.id.view_store_container, StoreInfoFragment.newInstance(), LIST_NEAR_NEWS);
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_view_store_info:
                replaceStoreInfo();
                break;
            case R.id.nav_view_store_list_store:
                replaceListStore();
                break;
            case R.id.nav_view_store_member:
                replaceListMember();
                break;
            case R.id.nav_view_store_news:
                replaceListNews();
                break;
            case R.id.nav_view_store_near_news:
                replaceListNearNews();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
