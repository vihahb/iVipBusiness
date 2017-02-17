package com.xtel.ivipbusiness.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xtel.ivipbusiness.R;
import com.xtel.sdk.utils.WidgetHelper;

/**
 * Created by vulclph03762 on 12/11/2016
 */

public class GenderAdapter extends BaseAdapter {
    private String[] arrayList;
    private Activity activity;
    private LayoutInflater inflater;

    public GenderAdapter(Activity activity) {
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        arrayList = activity.getResources().getStringArray(R.array.gender);
    }

    @Override
    public int getCount() {
        return arrayList.length;
    }

    @Override
    public Object getItem(int position) {
        return arrayList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolderDropdown viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_spinner_gender_dropdown, parent, false);
            viewHolder = new ViewHolderDropdown();

            viewHolder.textView = (TextView) convertView.findViewById(R.id.item_spinner_txt_gender);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderDropdown) convertView.getTag();
        }

//        WidgetHelper.getInstance().setTextViewDrawable(viewHolder.textView, 0, type.getResource());
        WidgetHelper.getInstance().setTextViewWithResult(viewHolder.textView, arrayList[position], activity.getString(R.string.updating));

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_spinner_type_normal, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.textView = (TextView) convertView.findViewById(R.id.item_spinner_edt_type);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        WidgetHelper.getInstance().setTextViewDrawable(viewHolder.textView, 0, R.drawable.ic_action_gender);
        WidgetHelper.getInstance().setTextViewWithResult(viewHolder.textView, arrayList[position], activity.getString(R.string.updating));

        return convertView;
    }

    private class ViewHolder {
        private TextView textView;
    }

    private class ViewHolderDropdown {
        private TextView textView;
    }
}
