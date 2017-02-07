package com.xtel.ivipbusiness.view.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xtel.ivipbusiness.R;
import com.xtel.sdk.callback.DialogListener;

import java.io.Serializable;

/**
 * Created by Lê Công Long Vũ on 12/2/2016
 */

public abstract class BasicActivity extends IActivity {
    private ProgressDialog progressDialog;
    private Dialog dialog;
    boolean isWaitingForExit = false;
    private Toast toast;

    protected void initToolbar(int id, String title) {
        Toolbar toolbar = (Toolbar) findViewById(id);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (title != null)
            actionBar.setTitle(title);
    }

    protected void debug(String message) {
        Log.e(this.getClass().getSimpleName(), message);
    }

    protected void showLongToast(String message) {
        if (toast != null)
            toast.cancel();

        toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    protected void showShortToast(String message) {
        if (toast != null)
            toast.cancel();

        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    protected void showProgressBar(boolean isTouchOutside, boolean isCancel, String title, String message) {
        progressDialog = new ProgressDialog(BasicActivity.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCanceledOnTouchOutside(isTouchOutside);
        progressDialog.setCancelable(isCancel);

        if (title != null)
            progressDialog.setTitle(title);
        if (message != null)
            progressDialog.setMessage(message);

        progressDialog.show();
    }

    protected void closeProgressBar() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    protected void showMaterialDialog(boolean isTouchOutside, boolean isCancelable, String title, String message,
                                      String negative, String positive, final DialogListener dialogListener) {
        dialog = new Dialog(BasicActivity.this, R.style.Theme_Transparent);
        dialog.setContentView(R.layout.dialog_material);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(isTouchOutside);
        dialog.setCanceledOnTouchOutside(isCancelable);

        TextView txt_title = (TextView) dialog.findViewById(R.id.dialog_txt_title);
        TextView txt_message = (TextView) dialog.findViewById(R.id.dialog_txt_message);
        Button btn_negative = (Button) dialog.findViewById(R.id.dialog_btn_negative);
        Button btn_positive = (Button) dialog.findViewById(R.id.dialog_btn_positive);

        if (title == null)
            txt_title.setVisibility(View.GONE);
        else
            txt_title.setText(title);

        if (message == null)
            txt_message.setVisibility(View.GONE);
        else
            txt_message.setText(message);

        if (negative == null)
            btn_negative.setVisibility(View.GONE);
        else
            btn_negative.setText(negative);

        if (positive == null)
            btn_positive.setVisibility(View.GONE);
        else
            btn_positive.setText(positive);

        btn_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialogListener.onCancel();
            }
        });

        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialogListener.onClicked(null);
            }
        });

        if (dialog != null)
            dialog.show();
    }

    public void closeDialog() {
        if (dialog != null)
            dialog.dismiss();
    }

    //    Khởi chạy fragment giao diện và add vào stack
    protected void replaceFragment(int id, Fragment fragment, String tag) {
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(id, fragment, tag);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
        }
    }

    protected void startActivity(Class clazz) {
        startActivity(new Intent(this, clazz));
    }

    protected void startActivityForResult(Class clazz, int requestCode) {
        startActivityForResult(new Intent(this, clazz), requestCode);
    }

    protected void startActivityForResultWithInteger(Class clazz, String key, int data, int requestCode) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra(key, data);
        startActivityForResult(intent, requestCode);
    }

    protected void startActivityForResult(Class clazz, String key, Object object, int requestCode) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra(key, (Serializable) object);
        startActivityForResult(intent, requestCode);
    }

    protected void startActivityAndFinish(Class clazz) {
        startActivity(new Intent(this, clazz));
        finish();
    }

    protected void finishActivity() {
        finish();
    }

    protected void showConfirmExitApp() {
        if (isWaitingForExit) {
            finish();
        } else {
            new AsyncTask<Object, Object, Object>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    isWaitingForExit = true;
                    showShortToast(getString(R.string.text_back_press_to_exit));

                }

                @Override
                protected Object doInBackground(Object... params) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                    isWaitingForExit = false;
                }
            }.execute();
        }
    }
}
