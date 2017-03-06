package com.xtel.ivipbusiness.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.Type;
import com.xtel.sdk.utils.WidgetHelper;

import java.util.ArrayList;

/**
 * Created by vulclph03762 on 12/11/2016
 */

public class TypeSaleAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;

    private boolean isEnable = true;
    private ArrayList<Type> arrayList;

    public TypeSaleAdapter(Activity activity) {
        this.activity = activity;

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initArraylist();
    }

    private void initArraylist() {
        arrayList = new ArrayList<>();

        String[] type_name = activity.getResources().getStringArray(R.array.store_type);
        int[] type_resource = {R.drawable.ic_action_fashion, R.drawable.ic_action_food, R.drawable.ic_action_technology, R.drawable.ic_action_hospital, R.drawable.ic_action_view};

        for (int i = 0; i < type_name.length; i++) {
            Type type = new Type(type_resource[i], type_name[i]);
            arrayList.add(type);
        }
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
            convertView = inflater.inflate(R.layout.item_spinner_gender_dropdown, parent, false);
            viewHolder = new ViewHolderDropdown();

            viewHolder.textView = (TextView) convertView.findViewById(R.id.item_spinner_txt_gender);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderDropdown) convertView.getTag();
        }

        WidgetHelper.getInstance().setTextViewDrawable(viewHolder.textView, 0, arrayList.get(position).getResource());
        WidgetHelper.getInstance().setTextViewWithResult(viewHolder.textView, arrayList.get(position).getName(), activity.getString(R.string.updating));

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_spinner_type_normal, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.view = convertView.findViewById(R.id.item_spinner_type_view);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.item_spinner_type_txt_type);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (isEnable)
            viewHolder.view.setBackgroundColor(activity.getResources().getColor(android.R.color.white));
        else
            viewHolder.view.setBackgroundColor(activity.getResources().getColor(R.color.line_disable));

        WidgetHelper.getInstance().setTextViewDrawable(viewHolder.textView, 0, arrayList.get(position).getResource());
        WidgetHelper.getInstance().setTextViewWithResult(viewHolder.textView, arrayList.get(position).getName(), activity.getString(R.string.updating));

        return convertView;
    }

    private class ViewHolder {
        private View view;
        private TextView textView;
    }

    private class ViewHolderDropdown {
        private TextView textView;
    }

    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
        notifyDataSetChanged();
    }
}
