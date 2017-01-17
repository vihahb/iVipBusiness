package com.xtel.sdk.utils;

import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.view.MyApplication;

import java.util.Calendar;

/**
 * Created by Vulcl on 1/17/2017
 */

public class WidgetHelper {
    private static WidgetHelper instance;

    public static WidgetHelper getInstance() {
        if (instance == null) {
            Log.e("WidgetHelper", "create");
            instance = new WidgetHelper();
        }
        return instance;
    }

    public void setImageURL(ImageView view, String url) {
        if (url == null || url.isEmpty())
            return;

        Picasso.with(MyApplication.context)
                .load(url)
                .noPlaceholder()
                .into(view);
    }

    public void setEditTextNoResult(EditText view, String content) {
        view.setText(content);
    }

    public void setEditTextBirthday(EditText view, long milliseconds) {
        if (milliseconds != 0)
            view.setText(getBirthday(milliseconds));
    }

    public void setEditTextBirthday(EditText view, int day, int month, int year) {
        String mMonth;

        month = month + 1;
        if (month < 10)
            mMonth = "0" + month;
        else
            mMonth = String.valueOf(month);

        view.setText(day + "-" + mMonth + "-" + year);
    }

    public void setTextViewNoResult(TextView view, String content) {
        view.setText(content);
    }

    public void setTextViewWithResult(TextView view, String content, String result) {
        if (content == null || content.isEmpty())
            view.setText(result);
        else
            view.setText(content);
    }

    public void setTextViewBirthday(TextView view, String content, long milliseconds) {
        if (milliseconds == 0)
            view.setText((content + MyApplication.context.getString(R.string.updating)));
        else
            view.setText((content + getBirthday(milliseconds)));
    }

    private String getBirthday(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH) + 1;
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        String month;
        if (mMonth < 10)
            month = "0" + mMonth;
        else
            month = String.valueOf(mMonth);

        return mDay + "-" + month + "-" + mYear;
    }

    public void setSpinnerGender(Spinner view, int type) {
        view.setSelection(type);
    }
}