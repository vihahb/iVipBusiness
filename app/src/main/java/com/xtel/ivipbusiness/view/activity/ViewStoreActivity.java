package com.xtel.ivipbusiness.view.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.view.fragment.MemberFragment;
import com.xtel.ivipbusiness.view.fragment.NewsFragment;
import com.xtel.ivipbusiness.view.fragment.StoresFragment;
import com.xtel.ivipbusiness.view.fragment.StoreInfoFragment;

public class ViewStoreActivity extends BasicActivity {
    private ActionBar actionBar;

    private final String STORE_INFO = "store_info", LIST_STORE = "list_store", LIST_MENBER = "list_member", LIST_NEWS = "list_news", LIST_NEAR_NEWS = "list_near_news";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_store);

        initToolbar();
        initTablayout();
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

    //    Khởi tạo tab layout phía dưới
    private void initTablayout() {
        int[] icon = new int[] {R.drawable.ic_action_store_info, R.drawable.ic_action_list_store, R.drawable.ic_action_member, R.drawable.ic_action_news, R.drawable.ic_action_news_fcm};
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

    //    hiển thị fratment thông tin store
    private void replaceStoreInfo() {
        actionBar.setTitle(getString(R.string.title_activity_view_store));
        replaceFragment(R.id.view_store_container, StoreInfoFragment.newInstance(), STORE_INFO);
    }

    //    hiển thị fratment danh sách chuỗi store
    private void replaceListStore() {
        actionBar.setTitle(getString(R.string.title_activity_list_store));
        replaceFragment(R.id.view_store_container, StoresFragment.newInstance(), LIST_STORE);
    }

    //    hiển thị fratment member
    private void replaceListMember() {
        actionBar.setTitle(getString(R.string.title_activity_list_member));
        replaceFragment(R.id.view_store_container, MemberFragment.newInstance(), LIST_MENBER);
    }

    //    hiển thị fratment bản tin
    private void replaceListNews() {
        actionBar.setTitle(getString(R.string.title_activity_list_news));
        replaceFragment(R.id.view_store_container, NewsFragment.newInstance(), LIST_NEWS);
    }

    //    hiển thị fratment bản tin gần đây
    private void replaceListNearNews() {
        actionBar.setTitle(getString(R.string.title_activity_list_fcm_news));
        replaceFragment(R.id.view_store_container, StoreInfoFragment.newInstance(), LIST_NEAR_NEWS);
    }

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





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
