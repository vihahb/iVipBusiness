package com.xtel.ivipbusiness.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.Type;
import com.xtel.sdk.utils.WidgetHelper;

import java.util.ArrayList;

/**
 * Created by vulclph03762 on 12/11/2016
 */

public class TypeAdapter extends BaseAdapter {
    private ArrayList<Type> arrayList;
    private Activity activity;
    private LayoutInflater inflater;

    public TypeAdapter(Activity activity) {
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initArraylist();
    }

    private void initArraylist() {
        arrayList = new ArrayList<>();

        String[] type_name = activity.getResources().getStringArray(R.array.store_type);
        int[] type_resource = {R.drawable.ic_action_view, R.drawable.ic_action_view, R.drawable.ic_action_view, R.drawable.ic_action_view, R.drawable.ic_action_view, R.drawable.ic_action_view};

        for (int i = 0; i < 6; i++) {
            Type type = new Type(type_resource[i], type_name[i]);
            arrayList.add(type);
        }

        Log.e("total_array", "total: " + arrayList.size());
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolderDropdown viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_spinner_type_dropdown, parent, false);
            viewHolder = new ViewHolderDropdown();

            viewHolder.textView = (TextView) convertView.findViewById(R.id.item_spinner_txt_type);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderDropdown) convertView.getTag();
        }

        Type type = arrayList.get(position);
        WidgetHelper.getInstance().setTextViewDrawable(viewHolder.textView, 0, type.getResource());
        WidgetHelper.getInstance().setTextViewWithResult(viewHolder.textView, type.getName(), activity.getString(R.string.updating));

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

        Type type = arrayList.get(position);
        WidgetHelper.getInstance().setTextViewDrawable(viewHolder.textView, 0, type.getResource());
        WidgetHelper.getInstance().setTextViewWithResult(viewHolder.textView, type.getName(), activity.getString(R.string.updating));

        return convertView;
    }

    private class ViewHolder {
        private TextView textView;
    }

    private class ViewHolderDropdown {
        private TextView textView;
    }
}