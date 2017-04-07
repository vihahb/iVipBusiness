package com.xtel.ivipbusiness.view.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.LevelObject;
import com.xtel.ivipbusiness.view.activity.SettingActivity;
import com.xtel.nipservicesdk.utils.JsonHelper;
import com.xtel.sdk.utils.NetWorkInfo;
import com.xtel.sdk.utils.ViewHolderHelper;
import com.xtel.sdk.utils.WidgetHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Vulcl on 3/2/2017
 */

public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.ViewHolder> {
    private Context context;
    private ArrayList<LevelObject> arrayList;
    private boolean isEnable = false;

    public LevelAdapter(Context context, ArrayList<LevelObject> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_level, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (arrayList.get(position).getLevel() == null)
            arrayList.get(position).setLevel((position + 1));

        final LevelObject levelObject = arrayList.get(position);

        WidgetHelper.getInstance().setTextViewNoResult(holder.txt_stt, levelObject.getLevel());
        WidgetHelper.getInstance().setTextViewNoResult(holder.txt_point, levelObject.getLevel_limit());
        WidgetHelper.getInstance().setTextViewNoResult(holder.txt_name, levelObject.getLevel_name());

        holder.txt_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetWorkInfo.isOnline(context)) {
                    Toast.makeText(context, context.getResources().getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
                    return;
                }
                    showMemberCard(levelObject);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (isEnable)
                    ((SettingActivity) context).deleteLevel(position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends ViewHolderHelper {
        private TextView txt_stt, txt_point, txt_name, txt_image;

        public ViewHolder(View itemView) {
            super(itemView);

            txt_stt = findTextView(R.id.item_level_txt_stt);
            txt_point = findTextView(R.id.item_level_txt_point);
            txt_name = findTextView(R.id.item_level_txt_name);
            txt_image = findTextView(R.id.item_level_txt_image);
        }
    }

    //    Thêm cấp bậc mới
    public void addLevel(LevelObject levelObject) {
        arrayList.add(levelObject);
        notifyItemInserted((arrayList.size() - 1));
        notifyItemRangeRemoved((arrayList.size() - 1), arrayList.size());

        new SortMemberCard().execute();
    }

    //    Hiển thì card lên cho người dùng
    @SuppressWarnings("ConstantConditions")
    private void showMemberCard(LevelObject levelObject) {
        String url = null;
        if (levelObject.getUrl_card() != null)
            url = levelObject.getUrl_card();
        else if (levelObject.getMember_card() != null)
            url = levelObject.getMember_card();

        Log.e("showMemberCard", JsonHelper.toJson(levelObject));

        if (url == null) {
            Toast.makeText(context, context.getString(R.string.error_view_code), Toast.LENGTH_SHORT).show();
            return;
        }

        final Dialog bottomSheetDialog = new Dialog(context, R.style.MaterialDialogSheet);
        bottomSheetDialog.setContentView(R.layout.dialog_show_image);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        bottomSheetDialog.getWindow().setGravity(Gravity.CENTER);

        Button txt_close = (Button) bottomSheetDialog.findViewById(R.id.dialog_btn);
        ImageView img_qr_code = (ImageView) bottomSheetDialog.findViewById(R.id.dialog_img);

        WidgetHelper.getInstance().setImageURL(img_qr_code, url);

        if (txt_close != null)
            txt_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                }
            });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bottomSheetDialog.show();
            }
        }, 200);
    }

    public void deleteLevel(int position) {
        arrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position, arrayList.size());

        //noinspection unchecked
        new SortMemberCard().execute();
    }

    public void changeAdapter() {
        //noinspection unchecked
        new SortMemberCard().execute();
    }

    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    private class SortMemberCard extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Log.e("SortMemberCard", JsonHelper.toJson(arrayList));

            if (arrayList.size() == 0)
                return null;

            for (int i = arrayList.size() - 1; i >= 0; i--)
                arrayList.get(i).setLevel(null);

            Collections.sort(arrayList, new Comparator<LevelObject>() {
                public int compare(LevelObject o1, LevelObject o2) {
                    if (o1.getLevel_limit() == null || o2.getLevel_limit() == null)
                        return 0;
                    return o1.getLevel_limit().compareTo(o2.getLevel_limit());
                }
            });

            Log.e("SortMemberCard", JsonHelper.toJson(arrayList));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            notifyDataSetChanged();
        }
    }
}
