package com.xtel.ivipbusiness.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.Gallery;
import com.xtel.ivipbusiness.presenter.GalleryPresenter;
import com.xtel.ivipbusiness.view.activity.LoginActivity;
import com.xtel.ivipbusiness.view.activity.ResizeImageActivity;
import com.xtel.ivipbusiness.view.adapter.GalleryAdapter;
import com.xtel.ivipbusiness.view.fragment.inf.IGalleryView;
import com.xtel.ivipbusiness.view.widget.ProgressView;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.xtel.sdk.callback.DialogListener;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.NetWorkInfo;

import java.util.ArrayList;

/**
 * Created by Mr. M.2 on 1/13/2017
 */

public class GalleryFragment extends BasicFragment implements IGalleryView {
    private GalleryPresenter presenter;

    private GalleryAdapter adapter;
    private ArrayList<Gallery> listData;
    private ProgressView progressView;
    private CallbackManager callbackManager;

    private boolean isClearData = false;
    private int gallery_position = -1;

    protected final int REQUEST_RESIZE_IMAGE = 8;

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callbackManager = CallbackManager.create(getActivity());

        presenter = new GalleryPresenter(this);
        initProgressView();
        initFloatingActionButton();
    }

    //    Khởi tạo layout và recyclerview
    private void initProgressView() {
        progressView = findProgressView();
        progressView.initData(-1, getString(R.string.no_gallery), getString(R.string.click_to_try_again));

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        listData = new ArrayList<>();
        adapter = new GalleryAdapter(this, listData);
        progressView.setUpRecyclerView(layoutManager, adapter);

        progressView.onLayoutClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressView.setRefreshing(true);
                progressView.showData();
                presenter.getGallery(true);
            }
        });

        progressView.onRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataAgain();
            }
        });

        progressView.onSwipeLayoutPost(new Runnable() {
            @Override
            public void run() {
                progressView.setRefreshing(true);
                progressView.showData();
                presenter.getGallery(true);
            }
        });
    }

    protected void initFloatingActionButton() {
        FloatingActionButton fab = findFloatingActionButton(R.id.gallery_fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.takePicture();
            }
        });
    }

    protected void getDataAgain() {
        isClearData = true;
        adapter.setLoadMore(false);
        adapter.notifyDataSetChanged();
        progressView.setRefreshing(true);
        progressView.showData();
        presenter.getGallery(true);
    }

    //    Kiểm tra xem danh sách cửa hàng có trống không
    private void checkListData() {
        progressView.setRefreshing(false);

        if (listData.size() > 0) {
            adapter.notifyDataSetChanged();
            progressView.showData();
        } else {
            progressView.initData(-1, getString(R.string.no_gallery), getString(R.string.click_to_try_again));
            progressView.hideData();
        }
    }

    /*
    * Lấy ảnh đã đưuọc resize
    * */
    protected void getImageResize(Intent data) {
        try {
            int type = data.getIntExtra(Constants.TYPE, -1);
            String server_uri = data.getStringExtra(Constants.URI);

            if (type != -1 && server_uri != null) {
                showProgressBar(false, false, null, getString(R.string.doing_add_gallery));
                presenter.addPicture(server_uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onGetDataError() {
        progressView.setRefreshing(false);
        progressView.updateData(-1, getString(R.string.have_error), getString(R.string.click_to_try_again));
        progressView.hideData();
    }

    @Override
    public void getNewSession(final ICmd iCmd, final Object... parame) {
        callbackManager.getNewSesion(new CallbacListener() {
            @Override
            public void onSuccess(RESP_Login success) {
                iCmd.execute(parame);
            }

            @Override
            public void onError(Error error) {
                showShortToast(getString(R.string.error_end_of_session));
                getActivity().finishAffinity();
                startActivity(LoginActivity.class);
            }
        });
    }

    @Override
    public void onLoadMore() {
        presenter.getGallery(false);
    }

    //    Sự kiện load danh sách store thành công
    @Override
    public void onGetGallerySuccess(final ArrayList<Gallery> arrayList) {
        if (arrayList.size() < 10) {
            adapter.setLoadMore(false);
            adapter.notifyDataSetChanged();
        }

        if (isClearData) {
            listData.clear();
            adapter.setLoadMore(true);
            isClearData = false;
        }
        listData.addAll(arrayList);

        checkListData();
    }


    @Override
    public void onGetStoresError(Error error) {
        adapter.setLoadMore(false);
        adapter.notifyDataSetChanged();

        if (listData.size() > 0)
            showShortToast(JsonParse.getCodeMessage(error.getCode(), getString(R.string.error)));
        else {
            progressView.setRefreshing(false);
            progressView.updateData(-1, JsonParse.getCodeMessage(error.getCode(), getString(R.string.error)), getString(R.string.click_to_try_again));
            progressView.hideData();

            listData.clear();
            adapter.notifyDataSetChanged();
            if (isClearData)
                isClearData = false;
        }
    }

    @Override
    public void onDeleteSuccess() {
        adapter.deleteGallery(gallery_position);

        closeProgressBar();
        showMaterialDialog(true, true, null, getString(R.string.success_delete_gallery), null, getString(R.string.ok), new DialogListener() {
            @Override
            public void negativeClicked() {
                closeDialog();
            }

            @Override
            public void positiveClicked() {
                closeDialog();
            }
        });
    }

    @Override
    public void onRequestError(Error error) {
        gallery_position = -1;
        closeProgressBar();

        if (error.getCode() == 301)
            showShortToast(getString(R.string.error_gallery_not_exists));
        else if (error.getCode() == 101)
            showShortToast(getString(R.string.error_chain_not_exists));
        else
            showShortToast(JsonParse.getCodeMessage(error.getCode(), getString(R.string.error_try_again)));
    }

    @Override
    public void onTakePictureGallary(int type, Uri uri) {
        if (!NetWorkInfo.isOnline(getActivity())) {
            showShortToast(getString(R.string.error_no_internet));
            return;
        } else if (uri == null) {
            showShortToast(getString(R.string.error_get_image));
            return;
        }

        Intent intent = new Intent(getActivity(), ResizeImageActivity.class);
        intent.putExtra(Constants.URI, uri);
        intent.putExtra(Constants.TYPE, type);

        startActivityForResult(intent, REQUEST_RESIZE_IMAGE);
    }

    @Override
    public void onTakePictureCamera(int type, Bitmap bitmap) {
        if (!NetWorkInfo.isOnline(getActivity())) {
            showShortToast(getString(R.string.error_no_internet));
            return;
        } else if (bitmap == null) {
            showShortToast(getString(R.string.error_get_image));
            return;
        }

        Intent intent = new Intent(getActivity(), ResizeImageActivity.class);
        intent.putExtra(Constants.BITMAP, bitmap);
        intent.putExtra(Constants.TYPE, type);

        startActivityForResult(intent, REQUEST_RESIZE_IMAGE);
    }

//    @Override
//    public void onPostPictureSuccess(RESP_Image resp_image) {
//        presenter.addPicture(resp_image);
//    }

    @Override
    public void onAddPictureSuccess() {
        closeProgressBar();
        showMaterialDialog(true, true, null, getString(R.string.success_add_gallery), null, getString(R.string.ok), new DialogListener() {
            @Override
            public void negativeClicked() {
                closeDialog();
                getDataAgain();
            }

            @Override
            public void positiveClicked() {
                closeDialog();
                getDataAgain();
            }
        });
    }

    @Override
    public void onDeleteGallery(final int id, final int position) {
        showMaterialDialog(true, true, null, getString(R.string.ask_delete_gallery), getString(R.string.delete), getString(R.string.back), new DialogListener() {
            @Override
            public void negativeClicked() {
                closeDialog();
                gallery_position = position;
                showProgressBar(false, false, null, getString(R.string.doing_delete_gallery));
                presenter.deleteGallery(id);
            }

            @Override
            public void positiveClicked() {
                closeDialog();
            }
        });
    }

    @Override
    public void closeProgressBar() {
        super.closeProgressBar();
    }

    @Override
    public void showShortToast(String message) {
        super.showShortToast(message);
    }

    @Override
    public void onNoNetwork() {
        progressView.setRefreshing(false);

        if (listData.size() > 0)
            showShortToast(getString(R.string.error_no_internet));
        else {
            progressView.updateData(-1, getString(R.string.error_no_internet), getString(R.string.click_to_try_again));
            progressView.hideData();
        }
    }

    @Override
    public void startActivity(Class clazz, String key, Object object) {
        super.startActivity(clazz, key, object);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void onDestroy() {
        presenter.setExists(false);
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callbackManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RESIZE_IMAGE && resultCode == Activity.RESULT_OK) {
            getImageResize(data);
        } else
            presenter.onActivityResult(requestCode, resultCode, data);
    }
}