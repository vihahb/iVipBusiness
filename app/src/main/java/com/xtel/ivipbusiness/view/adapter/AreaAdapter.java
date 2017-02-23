package com.xtel.ivipbusiness.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.Area;
import com.xtel.sdk.utils.WidgetHelper;

import java.util.ArrayList;

/**
 * Created by vulclph03762 on 12/11/2016
 */

public class AreaAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;

    private ArrayList<Area> arrayList;

    public AreaAdapter(Context context, ArrayList<Area> arrayList) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrayList = arrayList;
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
    public View getDropDownView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_spinner_area_dropdown, parent, false);

        final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.item_spinner_area_chk);
        TextView textView = (TextView) convertView.findViewById(R.id.item_spinner_area_txt);

        checkBox.setChecked(arrayList.get(position).isSelected());
        WidgetHelper.getInstance().setTextViewWithResult(textView, arrayList.get(position).getName(), context.getString(R.string.updating));

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                arrayList.get(position).setSelected(isChecked);
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.get(position).setSelected((!arrayList.get(position).isSelected()));
                checkBox.setChecked(arrayList.get(position).isSelected());
            }
        });

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_spinner_area_normal, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.view = convertView.findViewById(R.id.item_spinner_area_view);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.item_spinner_area_txt_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        WidgetHelper.getInstance().setTextViewWithResult(viewHolder.textView, arrayList.get(position).getName(), context.getString(R.string.updating));

        return convertView;
    }

    private class ViewHolder {
        private View view;
        private TextView textView;
    }

    private class ViewHolderDropdown {
        private CheckBox checkBox;
        private TextView textView;
    }
}
