package com.xtel.ivipbusiness.view.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
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
 * Created by Lê Công Long Vũ on 12/29/2016
 */

public class BasicFragment extends IFragment {
    private ProgressDialog progressDialog;
    private Dialog dialog;
    private Toast toast;

    protected void debug(String message) {
        Log.e(this.getClass().getSimpleName(), message);
    }

    protected void showLongToast(String message) {
        if (toast != null)
            toast.cancel();

        toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    protected void showShortToast(String message) {
        if (toast != null)
            toast.cancel();

        toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    protected void showProgressBar(boolean isTouchOutside, boolean isCancel, String title, String message) {
        progressDialog = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);
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
        dialog = new Dialog(getContext(), R.style.Theme_Transparent);
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
                dialogListener.onClicked(null);
            }
        });

        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialogListener.onCancel();
            }
        });

        if (dialog != null)
            dialog.show();
    }

    public void closeDialog() {
        if (dialog != null)
            dialog.dismiss();
    }

    protected void startActivity(Class clazz) {
        getActivity().startActivity(new Intent(getActivity(), clazz));
    }

    protected void startActivity(Class clazz, String key, Object object) {
        Intent intent = new Intent(getActivity(), clazz);
        intent.putExtra(key, (Serializable) object);
        getActivity().startActivity(intent);
    }

    protected void startActivityForResult(Class clazz, int requestCode) {
        Intent intent = new Intent(getActivity(), clazz);
        getActivity().startActivityForResult(intent, requestCode);
    }

    protected void startActivityForResult(Class clazz, String key, Object object, int requestCode) {
        Intent intent = new Intent(getActivity(), clazz);
        intent.putExtra(key, (Serializable) object);
        getActivity().startActivityForResult(intent, requestCode);
    }

    protected void startActivityAndFinish(Class clazz) {
        getActivity().startActivity(new Intent(getActivity(), clazz));
        getActivity().finish();
    }

    protected void finishActivity() {
        getActivity().finish();
    }
}
