package com.xtel.ivipbusiness.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.RESP_Store;
import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.ivipbusiness.presenter.ViewStorePresenter;
import com.xtel.ivipbusiness.view.activity.inf.IViewStoreView;
import com.xtel.ivipbusiness.view.fragment.MemberFragment;
import com.xtel.ivipbusiness.view.fragment.NewsFragment;
import com.xtel.ivipbusiness.view.fragment.StoreInfoFragment;
import com.xtel.ivipbusiness.view.fragment.StoresFragment;
import com.xtel.sdk.callback.DialogListener;

public class ViewStoreActivity extends BasicActivity implements IViewStoreView {
    private ViewStorePresenter presenter;

    private ActionBar actionBar;
    private MenuItem menu_create, menu_choose, menu_add;

    private RESP_Store resp_store = null;
    private SortStore sortStore;
    private final String STORE_INFO = "store_info", LIST_STORE = "list_store", LIST_MENBER = "list_member", LIST_NEWS = "list_news", LIST_NEAR_NEWS = "list_near_news";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_store);

        presenter = new ViewStorePresenter(this);
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

    //    Khởi tạo tab chức năng
    private void initTablayout() {
        int[] icon = new int[] {R.mipmap.ic_store_info, R.mipmap.ic_list_store, R.mipmap.ic_member, R.mipmap.ic_news, R.mipmap.ic_news_fcm};
        TabLayout tabLayout = (TabLayout) findViewById(R.id.view_store_tablayout);

        for (int i = 0; i < 5; i++) {
            tabLayout.addTab(tabLayout.newTab().setIcon(icon[i]));
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                checkTabSelected(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //    lựa chọn chức năng khi tab được chọn
    private void checkTabSelected(int position) {
        switch (position) {
            case 0:
                replaceStoreInfo();
                break;
            case 1:
                replaceListStore();
                break;
            case 2:
                replaceListMember();
                break;
            case 3:
                replaceListNews();
                break;
            case 4:
                replaceListNearNews();
                break;
            default:
                break;
        }
    }

//    Hiện menu của tab danh sách cửa hàng của chuỗi cửa hàng
    private void showMenuItem() {
        if (menu_create != null && menu_choose != null && menu_add != null) {
            menu_create.setVisible(true);
            menu_choose.setVisible(true);
            menu_add.setVisible(false);
        }
    }

//    Ản toàn bộ item trong menu
    private void hideMenuItem() {
        if (menu_create != null && menu_choose != null && menu_add != null) {
            menu_create.setVisible(false);
            menu_choose.setVisible(false);
            menu_add.setVisible(false);
        }
    }

    //    hiển thị fratment thông tin store
    private void replaceStoreInfo() {
        actionBar.setTitle(getString(R.string.title_activity_view_store));
        replaceFragment(R.id.view_store_container, StoreInfoFragment.newInstance(sortStore), STORE_INFO);
        hideMenuItem();
    }

    //    hiển thị fratment danh sách chuỗi store
    private void replaceListStore() {
        actionBar.setTitle(getString(R.string.title_activity_list_store));
        replaceFragment(R.id.view_store_container, StoresFragment.newInstance(), LIST_STORE);
        showMenuItem();
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
        hideMenuItem();
        menu_add.setVisible(true);
    }

    //    hiển thị fratment bản tin gần đây
    private void replaceListNearNews() {
        actionBar.setTitle(getString(R.string.title_activity_list_fcm_news));
        replaceFragment(R.id.view_store_container, StoreInfoFragment.newInstance(sortStore), LIST_NEAR_NEWS);
        hideMenuItem();
    }

    public RESP_Store getResp_store() {
        return resp_store;
    }

    public void setResp_store(RESP_Store resp_store) {
        this.resp_store = resp_store;
    }






























    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_store, menu);

        menu_create = menu.findItem(R.id.action_view_store_create_store);
        menu_choose = menu.findItem(R.id.action_view_store_choose_store);
        menu_add = menu.findItem(R.id.action_view_store_add_store);

        menu_create.setVisible(false);
        menu_choose.setVisible(false);
        menu_add.setVisible(false);

        SpannableString s = new SpannableString(menu_create.getTitle());
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 0, s.length(), 0);
        menu_create.setTitle(s);

        SpannableString s2 = new SpannableString(menu_choose.getTitle());
        s2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 0, s.length(), 0);
        menu_choose.setTitle(s2);

//        menu_add.setIcon(R.drawable.ic_action_add_store);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        else if (id == R.id.action_view_store_create_store) {
            StoresFragment fragment = (StoresFragment) getSupportFragmentManager().findFragmentByTag(LIST_STORE);
            if (fragment != null) {
                fragment.createNewStore();
            }
        } else if (id == R.id.action_view_store_choose_store) {
            StoresFragment fragment = (StoresFragment) getSupportFragmentManager().findFragmentByTag(LIST_STORE);
            if (fragment != null) {
                fragment.chooseExistsStore();
            }
        } else if (id == R.id.action_view_store_add_store) {
            startActivity(AddStoreActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        StoreInfoFragment fragment = (StoreInfoFragment) getSupportFragmentManager().findFragmentByTag(STORE_INFO);
        if (fragment != null)
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        debug(requestCode + "   " + resultCode);
        StoreInfoFragment fragment = (StoreInfoFragment) getSupportFragmentManager().findFragmentByTag(STORE_INFO);
        if (fragment != null)
            fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onGetDataSuccess(SortStore sortStore) {
        this.sortStore = sortStore;

        initToolbar();
        initTablayout();
        replaceStoreInfo();
    }

    @Override
    public void onGetDataError() {
        showMaterialDialog(false, false, null, getString(R.string.error_try_again), null, getString(R.string.back), new DialogListener() {
            @Override
            public void onClicked(Object object) {
                closeDialog();
                finish();
            }

            @Override
            public void onCancel() {
                closeDialog();
                finish();
            }
        });
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}