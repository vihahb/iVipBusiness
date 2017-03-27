package com.xtel.sdk.commons;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.view.MyApplication;
import com.xtel.sdk.callback.CallbackStringListener;

/**
 * Created by Vulcl on 3/25/2017
 */

public class DialogManager {
    private static DialogManager instance;

    public static DialogManager getInstance() {
        if (instance == null)
            instance = new DialogManager();
        return instance;
    }

    @SuppressWarnings("ConstantConditions")
    public void enterUrl(final Context context, final CallbackStringListener callbackStringListener) {
        final Dialog dialog = new Dialog(context, R.style.Theme_Transparent);
        dialog.setContentView(R.layout.dialog_input_url);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        final EditText txt_message = (EditText) dialog.findViewById(R.id.dialog_input_url_edt_url);
        Button btn_negative = (Button) dialog.findViewById(R.id.dialog_input_url_btn_negative);
        final Button btn_positive = (Button) dialog.findViewById(R.id.dialog_input_url_btn_positive);

        btn_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_message.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Vui lòng nhập địa chỉ web", Toast.LENGTH_SHORT).show();
                    return;
                }

                callbackStringListener.negativeClicked(txt_message.getText().toString());
                dialog.dismiss();
            }
        });

        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackStringListener.positiveClicked();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}