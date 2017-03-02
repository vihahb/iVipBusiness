package com.xtel.sdk.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.view.MyApplication;
import com.xtel.ivipbusiness.view.widget.CircleTransform;

import java.io.File;
import java.util.Calendar;

import jp.wasabeef.blurry.Blurry;

/**
 * Created by Vulcl on 1/17/2017
 */

public class WidgetHelper {
    private static WidgetHelper instance;

    public static WidgetHelper getInstance() {
        if (instance == null) {
            instance = new WidgetHelper();
        }
        return instance;
    }

    public void setImageFile(ImageView view, final File file) {
        if (file == null)
            return;

        Picasso.with(MyApplication.context)
                .load(file)
                .noPlaceholder()
                .error(R.drawable.color_primarykey)
                .into(view, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.e("WidgetHelper", "load ok " + file.getPath());
                        deleteFile(file);
                    }

                    @Override
                    public void onError() {
                        Log.e("WidgetHelper", "load error " + file.getPath());
                        deleteFile(file);
                    }
                });
    }

    public void setImageURL(ImageView view, String url) {
        if (url == null || url.isEmpty())
            return;

        final String finalUrl = url.replace("https", "http").replace("9191", "9190");

        Picasso.with(MyApplication.context)
                .load(finalUrl)
                .noPlaceholder()
                .error(R.drawable.color_primarykey)
                .into(view, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.e("WidgetHelper", "load ok " + finalUrl);
                    }

                    @Override
                    public void onError() {
                        Log.e("WidgetHelper", "load error " + finalUrl);
                    }
                });
    }

    public void setImageBlurURL(final ImageView imageBlur, String url) {
        String finalUrl = url.replace("https", "http").replace("9191", "9190");
        Picasso.with(MyApplication.context)
                .load(finalUrl)
                .noPlaceholder()
                .error(R.mipmap.ic_avatar_default)
                .into(imageBlur, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.e("setImageBlurURL", "success");
                        Bitmap bitmap = ((BitmapDrawable) imageBlur.getDrawable()).getBitmap();

                        Blurry.with(MyApplication.context).from(bitmap).into(imageBlur);

//                        Bitmap blurredBitmap = BlurBuilder.blur(MyApplication.context, bitmap);
//                        imageBlur.setImageDrawable(new BitmapDrawable(MyApplication.context.getResources(), blurredBitmap));
                    }

                    @Override
                    public void onError() {
                        Log.e("setImageBlurURL", "error");
                    }
                });
    }

    public void setImageBlurResource(final ImageView imageBlur, int resource) {
        Bitmap bitmap = BitmapFactory.decodeResource(MyApplication.context.getResources(), resource);

        Blurry.with(MyApplication.context).from(bitmap).into(imageBlur);
//        Bitmap blurredBitmap = BlurBuilder.blur(MyApplication.context, bitmap);
//        imageBlur.setImageDrawable(new BitmapDrawable(MyApplication.context.getResources(), blurredBitmap));
    }

    public void setImageBlurFile(final ImageView imageBlur, File file) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), options);

        Blurry.with(MyApplication.context).from(bitmap).into(imageBlur);

//        Bitmap blurredBitmap = BlurBuilder.blur(MyApplication.context, bitmap);
//        imageBlur.setImageDrawable(new BitmapDrawable(MyApplication.context.getResources(), blurredBitmap));
    }

    public void setSmallImageURL(ImageView view, String url) {
        if (url == null || url.isEmpty())
            return;

        final String finalUrl = url.replace("https", "http").replace("9191", "9190");

        Picasso.with(MyApplication.context)
                .load(finalUrl)
                .noPlaceholder()
                .error(R.drawable.color_primarykey)
                .fit()
                .centerCrop()
                .into(view, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.e("WidgetHelper", "load ok " + finalUrl);
                    }

                    @Override
                    public void onError() {
                        Log.e("WidgetHelper", "load error " + finalUrl);
                    }
                });
    }

    public void setAvatarImageURL(ImageView view, String url) {
        if (url == null || url.isEmpty())
            return;

        final String finalUrl = url.replace("https", "http").replace("9191", "9190");

        Picasso.with(MyApplication.context)
                .load(finalUrl)
                .noPlaceholder()
                .error(R.mipmap.ic_avatar_default)
                .fit()
                .centerCrop()
                .into(view, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.e("WidgetHelper", "load ok " + finalUrl);
                    }

                    @Override
                    public void onError() {
                        Log.e("WidgetHelper", "load error " + finalUrl);
                    }
                });
    }

    public void setAvatarImageFile(ImageView view, final File file) {
        if (file == null)
            return;

        Picasso.with(MyApplication.context)
                .load(file)
                .noPlaceholder()
                .error(R.mipmap.ic_avatar_default)
                .fit()
                .centerCrop()
                .into(view, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.e("WidgetHelper", "load ok " + file.getPath());
                        deleteFile(file);
                    }

                    @Override
                    public void onError() {
                        Log.e("WidgetHelper", "load error " + file.getPath());
                        deleteFile(file);
                    }
                });
    }

    private void deleteFile(File file) {
        try {
            boolean delete = file.delete();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setImageResource(ImageView view, int resource) {
        view.setImageResource(resource);
    }

    public void setViewBackground(View view, int resource) {
        view.setBackgroundResource(resource);
    }

    public void setViewBackgroundColor(View view, int resource) {
        view.setBackgroundColor(resource);
    }

    public void setEditTextNoResult(EditText view, int content) {
        view.setText(String.valueOf(content));
    }

    public void setEditTextNoResult(EditText view, String content) {
        view.setText(content);
    }

    public void setEditTextWithResult(EditText view, String content, String result) {
        if (content == null || content.isEmpty())
            view.setHint(result);
        else
            view.setText(content);
    }

    public void setEditTextWithResult(EditText view, String title, String content, String result) {
        if (content == null || content.isEmpty())
            view.setHint((title + result));
        else
            view.setText((title + content));
    }

    public void setEditTextGemder(EditText view, int gender) {
        String mGender[] = MyApplication.context.getResources().getStringArray(R.array.gender);
        mGender[0] = MyApplication.context.getString(R.string.not_update_gender);
        view.setText(mGender[gender]);
    }

    public void setEditTextGemder(EditText view, String title, int gender) {
        String mGender[] = MyApplication.context.getResources().getStringArray(R.array.gender);
        mGender[0] = MyApplication.context.getString(R.string.not_update_gender);
        view.setText((title + mGender[gender]));
    }

    public void setEditTextDate(EditText view, long milliseconds) {
        if (milliseconds != 0)
            view.setText(getDate(milliseconds));
    }

    public void setEditTextDate(EditText view, String title, long milliseconds) {
        if (milliseconds != 0)
            view.setText((title + getDate(milliseconds)));
    }

    public void setEditTextDate(EditText view, int day, int month, int year) {
        String mDate, mMonth;

        if (day < 10)
            mDate = "0" + day;
        else
            mDate = String.valueOf(day);

        month = month + 1;
        if (month < 10)
            mMonth = "0" + month;
        else
            mMonth = String.valueOf(month);

        view.setText(day + "-" + mMonth + "-" + year);
    }

    public void setEditTextDateWithResult(EditText view, long milliseconds, String result) {
        if (milliseconds != 0)
            view.setText(getDate(milliseconds));
        else
            view.setText(result);
    }

    public void setEditTextDateWithResult(EditText view, String title, long milliseconds, String result) {
        if (milliseconds != 0)
            view.setText((title + getDate(milliseconds)));
        else
            view.setText(result);
    }

    public void setEditTextTime(EditText view, int hour, int minute) {
        String mHour, mMinute;

        if (hour < 10)
            mHour = "0" + hour;
        else
            mHour = String.valueOf(hour);

        if (minute < 10)
            mMinute = "0" + minute;
        else
            mMinute = String.valueOf(minute);

        view.setText(mHour + ":" + mMinute);
    }

    public void setEditTextTime(EditText view, String title, int hour, int minute) {
        String mHour, mMinute;

        if (hour < 10)
            mHour = "0" + hour;
        else
            mHour = String.valueOf(hour);

        if (minute < 10)
            mMinute = "0" + minute;
        else
            mMinute = String.valueOf(minute);

        view.setText(title + mHour + ":" + mMinute);
    }

    public void setEditTextTime(EditText view, String title, long milisecond) {
        String mHour, mMinute;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milisecond);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        if (hour < 10)
            mHour = "0" + hour;
        else
            mHour = String.valueOf(hour);

        if (minute < 10)
            mMinute = "0" + minute;
        else
            mMinute = String.valueOf(minute);

        view.setText(title + mHour + ":" + mMinute);
    }

    public void setEditTextDrawable(EditText view, int position, int resource) {
        switch (position) {
            case 0:
                view.setCompoundDrawablesWithIntrinsicBounds(resource, 0, 0, 0);
                break;
            case 1:
                view.setCompoundDrawablesWithIntrinsicBounds(0, resource, 0, 0);
                break;
            case 2:
                view.setCompoundDrawablesWithIntrinsicBounds(0, 0, resource, 0);
                break;
            case 3:
                view.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, resource);
                break;
            default:
                break;
        }
    }

    public void setTextViewNoResult(TextView view, int content) {
        view.setText(String.valueOf(content));
    }

    public void setTextViewNoResult(TextView view, String content) {
        view.setText(content);
    }

    public void setTextViewNoResult(TextView view, String title, String content) {
        view.setText((title + ": " + content));
    }

    public void setTextViewWithResult(TextView view, String content, String result) {
        if (content == null || content.isEmpty())
            view.setText(result);
        else
            view.setText(content);
    }

    public void setTextViewWithResult(TextView view, String title, String content, String result) {
        if (content == null || content.isEmpty())
            view.setText((title + result));
        else
            view.setText((title + content));
    }

    public void setTextViewDate(TextView view, String content, Long milliseconds) {
        if (milliseconds == null) {
            view.setText((content + MyApplication.context.getString(R.string.updating)));
            return;
        }

        if (milliseconds == 0)
            view.setText((content + MyApplication.context.getString(R.string.updating)));
        else
            view.setText((content + getDate(milliseconds)));
    }

    public void setTextViewDateTime(TextView view, String content, long milliseconds) {
        if (milliseconds == 0)
            view.setText((content + MyApplication.context.getString(R.string.updating)));
        else
            view.setText((content + getDateTime(milliseconds)));
    }

    public void setTextViewGender(TextView view, String content, int gender) {
        String[] lisst_gender = MyApplication.context.getResources().getStringArray(R.array.gender_notify);
        lisst_gender[0] = MyApplication.context.getString(R.string.updating);
        view.setText((content + lisst_gender[gender]));
    }

    public void setTextViewOneArea(TextView view, String content, Integer[] area) {
        if (area == null) {
            view.setText((content + MyApplication.context.getString(R.string.updating)));
            return;
        }

        String[] lisst_area = MyApplication.context.getResources().getStringArray(R.array.area);
        view.setText((content + lisst_area[area[0]]));
    }

    public void setTextViewOneLevel(TextView view, String content, Integer[] level) {
        if (level == null) {
            view.setText((content + MyApplication.context.getString(R.string.updating)));
            return;
        }

        String[] lisst_level = MyApplication.context.getResources().getStringArray(R.array.level);
        view.setText((content + lisst_level[level[0]]));
    }

    public void setTextViewAgeFromTo(TextView view, String content, int from, int to) {
        view.setText((content + from + "-" + to + " " + MyApplication.context.getResources().getString(R.string.notify_age)));
    }

    public void setTextViewDrawable(TextView view, int position, int resource) {
        switch (position) {
            case 0:
                view.setCompoundDrawablesWithIntrinsicBounds(resource, 0, 0, 0);
                break;
            case 1:
                view.setCompoundDrawablesWithIntrinsicBounds(0, resource, 0, 0);
                break;
            case 2:
                view.setCompoundDrawablesWithIntrinsicBounds(0, 0, resource, 0);
                break;
            case 3:
                view.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, resource);
                break;
            default:
                break;
        }
    }

    public void setTextViewDrawable(TextView view, int position, Drawable resource) {
        switch (position) {
            case 0:
                view.setCompoundDrawablesWithIntrinsicBounds(resource, null, null, null);
                break;
            case 1:
                view.setCompoundDrawablesWithIntrinsicBounds(null, resource, null, null);
                break;
            case 2:
                view.setCompoundDrawablesWithIntrinsicBounds(null, null, resource, null);
                break;
            case 3:
                view.setCompoundDrawablesWithIntrinsicBounds(null, null, null, resource);
                break;
            default:
                break;
        }
    }

    private String getDate(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH) + 1;
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        String day;
        if (mDay < 10)
            day = "0" + mDay;
        else
            day = String.valueOf(mDay);

        String month;
        if (mMonth < 10)
            month = "0" + mMonth;
        else
            month = String.valueOf(mMonth);

        return day + "-" + month + "-" + mYear;
    }

    private String getDateTime(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH) + 1;
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String dateTime = "";

        if (mDay < 10)
            dateTime += "0" + mDay;
        else
            dateTime += String.valueOf(mDay);

        if (mMonth < 10)
            dateTime += "/0" + mMonth;
        else
            dateTime += "/" + String.valueOf(mMonth);

        dateTime += "/" + mYear;

        if (hour < 10)
            dateTime += "   0" + hour;
        else
            dateTime += "   " + String.valueOf(hour);

        if (minute < 10)
            dateTime += ":0" + minute;
        else
            dateTime += ":" + String.valueOf(minute);

        return dateTime;
    }

    public void setSpinnerGender(Spinner view, int type) {
        switch (type) {
            case 1:
                view.setSelection(1);
                break;
            case 2:
                view.setSelection(2);
                break;
            case 3:
                view.setSelection(3);
                break;
            default:
                view.setSelection(0);
                break;
        }
        view.setSelection(type);
    }

    public void setSpinnerNewsType(Spinner view, int type) {
        int pos = type - 2;
        if (pos >= 0)
            view.setSelection(pos);
    }

    public void setSpinnerVoucherType(Spinner view, int type) {
        int pos = type - 2;
        if (pos >= 0)
            view.setSelection(pos);
    }
}