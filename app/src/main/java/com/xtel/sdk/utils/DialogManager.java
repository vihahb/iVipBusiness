package com.xtel.sdk.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.xtel.ivipbusiness.R;
import com.xtel.sdk.callback.CallbackIntListener;
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

    public void chooseColor(final Activity activity, View view, final CallbackIntListener callbackIntListener) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.popup_color, (ViewGroup) activity.findViewById(R.id.popup_color_viewgroup));
            // create a 300px width and 470px height PopupWindow
            final PopupWindow popupWindow = new PopupWindow(activity);
            popupWindow.setContentView(layout);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            layout.findViewById(R.id.popup_color_one).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    callbackIntListener.negativeClicked(R.color.color_1);
                }
            });
            layout.findViewById(R.id.popup_color_two).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    callbackIntListener.negativeClicked(R.color.color_2);
                }
            });
            layout.findViewById(R.id.popup_color_three).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    callbackIntListener.negativeClicked(R.color.color_3);
                }
            });
            layout.findViewById(R.id.popup_color_four).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    callbackIntListener.negativeClicked(R.color.color_4);
                }
            });
            layout.findViewById(R.id.popup_color_five).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    callbackIntListener.negativeClicked(R.color.color_5);
                }
            });
            layout.findViewById(R.id.popup_color_six).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    callbackIntListener.negativeClicked(R.color.color_6);
                }
            });
            layout.findViewById(R.id.popup_color_seven).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    callbackIntListener.negativeClicked(R.color.color_7);
                }
            });
            layout.findViewById(R.id.popup_color_eight).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    callbackIntListener.negativeClicked(R.color.color_8);
                }
            });
            layout.findViewById(R.id.popup_color_nine).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    callbackIntListener.negativeClicked(R.color.color_9);
                }
            });
            layout.findViewById(R.id.popup_color_ten).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    callbackIntListener.negativeClicked(R.color.color_10);
                }
            });
            layout.findViewById(R.id.popup_color_eleven).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    callbackIntListener.negativeClicked(R.color.color_11);
                }
            });
            layout.findViewById(R.id.popup_color_twelve).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    callbackIntListener.negativeClicked(R.color.color_12);
                }
            });
            layout.findViewById(R.id.popup_color_thirteen).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    callbackIntListener.negativeClicked(R.color.color_13);
                }
            });
            layout.findViewById(R.id.popup_color_forteen).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    callbackIntListener.negativeClicked(R.color.color_14);
                }
            });
            layout.findViewById(R.id.popup_color_fifteen).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    callbackIntListener.negativeClicked(R.color.color_15);
                }
            });
            layout.findViewById(R.id.popup_color_sixteen).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    callbackIntListener.negativeClicked(R.color.color_16);
                }
            });
            layout.findViewById(R.id.popup_color_seventeen).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    callbackIntListener.negativeClicked(R.color.color_17);
                }
            });
            layout.findViewById(R.id.popup_color_eighteen).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    callbackIntListener.negativeClicked(R.color.color_18);
                }
            });
            layout.findViewById(R.id.popup_color_nineteen).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    callbackIntListener.negativeClicked(R.color.color_19);
                }
            });
            layout.findViewById(R.id.popup_color_twenty).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    callbackIntListener.negativeClicked(R.color.color_21);
                }
            });
            layout.findViewById(R.id.popup_color_btn_positive).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callbackIntListener.positiveClicked();
                    popupWindow.dismiss();
                }
            });

            // display the popup in the center
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}