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
import android.util.Log;
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
import com.xtel.nipservicesdk.utils.JsonHelper;
import com.xtel.sdk.callback.DialogListener;
import com.xtel.sdk.commons.Constants;

public class ViewStoreActivity extends BasicActivity implements IViewStoreView {
    private ViewStorePresenter presenter;

    private ActionBar actionBar;
    private MenuItem menu_create, menu_choose, menu_add_news, menu_edi_storet;

    private RESP_Store resp_store = null;
    private SortStore sortStore;
    private final String CHAIN_TYPE = "CHAIN", STORE_TYPE = "STORE";
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

    private int currentTab = 0;

    //    Khởi tạo tab chức năng
    private void initTablayout(final boolean isStore) {
        int[] icon = new int[]{R.mipmap.ic_store_info, R.mipmap.ic_list_store, R.mipmap.ic_member, R.mipmap.ic_news, R.mipmap.ic_news_fcm};
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.view_store_tablayout);

        for (int i = 0; i < 5; i++) {
            tabLayout.addTab(tabLayout.newTab().setIcon(icon[i]));
        }

        if (isStore)
            //noinspection ConstantConditions
            tabLayout.getTabAt(1).setIcon(R.mipmap.ic_list_store_gray);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (isStore && tab.getPosition() == 1) {
                    tabLayout.getTabAt(currentTab).select();
                    return;
                }

                currentTab = tab.getPosition();
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

    //    Hiện menu của tab thông tin cửa hàng
    private void showMenuStoreInfo() {
        if (menu_create != null && menu_choose != null && menu_add_news != null && menu_edi_storet != null) {
            menu_create.setVisible(false);
            menu_choose.setVisible(false);
            menu_edi_storet.setVisible(true);
            menu_add_news.setVisible(false);
        }
    }

    //    Hiện menu của tab danh sách cửa hàng của chuỗi cửa hàng
    private void showMenuListNew() {
        if (menu_create != null && menu_choose != null && menu_add_news != null && menu_edi_storet != null) {
            menu_create.setVisible(true);
            menu_choose.setVisible(true);
            menu_edi_storet.setVisible(false);
            menu_add_news.setVisible(false);
        }
    }

    //    Hiện menu của tab bản tin
    private void showMenuNews() {
        if (menu_create != null && menu_choose != null && menu_add_news != null && menu_edi_storet != null) {
            menu_create.setVisible(false);
            menu_choose.setVisible(false);
            menu_edi_storet.setVisible(false);
            menu_add_news.setVisible(true);
        }
    }

    //    Ản toàn bộ item trong menu
    private void hideMenuItem() {
        if (menu_create != null && menu_choose != null && menu_add_news != null && menu_edi_storet != null) {
            menu_create.setVisible(false);
            menu_choose.setVisible(false);
            menu_edi_storet.setVisible(false);
            menu_add_news.setVisible(false);
        }
    }

    //    hiển thị fratment thông tin store
    private void replaceStoreInfo() {
        actionBar.setTitle(getString(R.string.title_activity_view_store));
        replaceFragment(R.id.view_store_container, StoreInfoFragment.newInstance(sortStore), STORE_INFO);
        showMenuStoreInfo();
    }

    //    hiển thị fratment danh sách chuỗi store
    private void replaceListStore() {
        actionBar.setTitle(getString(R.string.title_activity_list_store));
        replaceFragment(R.id.view_store_container, StoresFragment.newInstance(sortStore.getId()), LIST_STORE);
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
        replaceFragment(R.id.view_store_container, NewsFragment.newInstance(sortStore), LIST_NEWS);
        showMenuNews();
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

    public void onUpdateStoreSuccess() {
        menu_edi_storet.setIcon(R.drawable.ic_action_edit_line);
    }

    public void restoreMenuIcon() {
        menu_edi_storet.setIcon(R.drawable.ic_action_edit_line);
    }




















    @Override
    public void onGetDataSuccess(SortStore sortStore) {
        this.sortStore = sortStore;
        Log.e(this.getClass().getSimpleName(), "sortStore " + JsonHelper.toJson(sortStore));

        initToolbar();

        if (sortStore.getStore_type().equals(STORE_TYPE))
            initTablayout(true);
        else
            initTablayout(false);

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_store, menu);

        menu_create = menu.findItem(R.id.action_view_store_create_store);
        menu_choose = menu.findItem(R.id.action_view_store_choose_store);
        menu_add_news = menu.findItem(R.id.action_view_store_add_news);
        menu_edi_storet = menu.findItem(R.id.action_view_store_edit_store);

        menu_create.setVisible(false);
        menu_choose.setVisible(false);
        menu_add_news.setVisible(false);
        menu_edi_storet.setVisible(true);

        SpannableString s = new SpannableString(menu_create.getTitle());
        s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 0, s.length(), 0);
        menu_create.setTitle(s);

        SpannableString s2 = new SpannableString(menu_choose.getTitle());
        s2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 0, s.length(), 0);
        menu_choose.setTitle(s2);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        else if (id == R.id.action_view_store_create_store) {
            Intent intent = new Intent(this, AddStoreActivity.class);
            intent.putExtra(Constants.ID, sortStore.getId());
            intent.putExtra(Constants.MODEL, STORE_TYPE);
            startActivityForResult(intent, 21);
        } else if (id == R.id.action_view_store_choose_store) {
            startActivityForResult(ListStoresActivity.class, 22);
        } else if (id == R.id.action_view_store_edit_store) {
            StoreInfoFragment fragment = (StoreInfoFragment) getSupportFragmentManager().findFragmentByTag(STORE_INFO);

            if (fragment != null) {
                if (menu_edi_storet.getIcon().getConstantState() == (getResources().getDrawable(R.drawable.ic_action_edit_line).getConstantState())) {
                    fragment.setEnableView(true);
                    menu_edi_storet.setIcon(R.drawable.ic_action_done);
                } else {
                    fragment.updateStore();
                }
            }
        } else if (id == R.id.action_view_store_add_news) {
            startActivity(AddNewsActivity.class, Constants.MODEL, sortStore);
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
}