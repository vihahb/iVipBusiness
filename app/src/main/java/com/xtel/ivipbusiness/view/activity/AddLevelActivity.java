package com.xtel.ivipbusiness.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.Card;
import com.xtel.ivipbusiness.model.entity.RESP_Image;
import com.xtel.ivipbusiness.presenter.AddLevelPresenter;
import com.xtel.ivipbusiness.view.activity.inf.IAddLevelView;
import com.xtel.ivipbusiness.view.adapter.MemberCardAdapter;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.xtel.sdk.callback.DialogListener;
import com.xtel.sdk.utils.NetWorkInfo;

import java.io.IOException;
import java.util.ArrayList;

public class AddLevelActivity extends BasicActivity implements IAddLevelView {
    private AddLevelPresenter presenter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CallbackManager callbackManager;

    protected EditText edt_limit, edt_name;
    protected ArrayList<Card> arrayList;
    protected MemberCardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_level);
        callbackManager = CallbackManager.create(this);

        presenter = new AddLevelPresenter(this);
        initToolbar(R.id.add_level_toolbar, null);
        presenter.getData();
    }

    //    Khởi tạo swipeRefreshLayout để hiển thị load thông tin
    private void initSwwipe() {
        swipeRefreshLayout = findSwipeRefreshLayout(R.id.add_level_swipe);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
    }

    protected void initView() {
        edt_limit = findEditText(R.id.add_level_edt_limit);
        edt_name = findEditText(R.id.add_level_edt_name);
    }

    protected void initRecyclerview() {
        RecyclerView recyclerView = findRecyclerView(R.id.add_level_recyclerview);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapter = new MemberCardAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);
    }

    protected void initListener() {
        Button btn_done = findButton(R.id.add_level_btn_done);
        Button btn_add_card = findButton(R.id.add_level_btn_add_member_card);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.done(edt_limit.getText().toString(), edt_name.getText().toString(), adapter.getMemberCard());
            }
        });

        btn_add_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.takePicture();
            }
        });
    }

    @Override
    public void onGetDataSuccess() {
        initSwwipe();
        initView();
        initListener();

    }

    @Override
    public void onGetDataError() {
        showMaterialDialog(false, false, null, getString(R.string.error_try_again), null, getString(R.string.back), new DialogListener() {
            @Override
            public void negativeClicked() {

            }

            @Override
            public void positiveClicked() {
                closeDialog();
                finish();
            }
        });
    }

    @Override
    public void onGetCardDefaultSuccess(ArrayList<Card> arrayList) {
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(false);

        this.arrayList = arrayList;
        initRecyclerview();
    }

    @Override
    public void onTakePictureGallary(Uri uri) {
        if (!NetWorkInfo.isOnline(this)) {
            showShortToast(getString(R.string.error_no_internet));
            return;
        } else if (uri == null) {
            showShortToast(getString(R.string.error_get_image));
            return;
        }

        showProgressBar(false, false, null, getString(R.string.uploading_file));

        Bitmap bitmap = null;

        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bitmap != null) {
            presenter.postImage(bitmap, true);
        }
    }

    @Override
    public void onTakePictureCamera(Bitmap bitmap) {
        if (!NetWorkInfo.isOnline(this)) {
            showShortToast(getString(R.string.error_no_internet));
            return;
        } else if (bitmap == null) {
            showShortToast(getString(R.string.error_get_image));
            return;
        }

        showProgressBar(false, false, null, getString(R.string.uploading_file));
        presenter.postImage(bitmap, true);
    }

    @Override
    public void onPostImageSuccess(RESP_Image resp_image) {
        Card card = new Card();
        card.setFile_path(resp_image.getFile_path());
        card.setCard_name(getString(R.string.updating));
        card.setCard_path(resp_image.getServer_path());
        card.setCard_url(resp_image.getUri());

        closeProgressBar();
        adapter.addMemberCard(card);
    }

    @Override
    public void getNewSession(final ICmd iCmd, final Object... params) {
        callbackManager.getNewSesion(new CallbacListener() {
            @Override
            public void onSuccess(RESP_Login success) {
                iCmd.execute(params);
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
    public void onRequestError(Error error) {
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(false);

        showMaterialDialog(false, false, null, JsonParse.getCodeMessage(error.getCode(), getString(R.string.error_try_again)), null, getString(R.string.back), new DialogListener() {
            @Override
            public void negativeClicked() {

            }

            @Override
            public void positiveClicked() {
                closeDialog();
                finish();
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
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onBackPressed() {
        if (adapter.getCurrentAnimation())
            return;
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callbackManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }
}