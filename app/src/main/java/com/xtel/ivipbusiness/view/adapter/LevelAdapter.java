package com.xtel.ivipbusiness.view.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
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
import com.xtel.sdk.utils.TextUnit;
import com.xtel.sdk.utils.ViewHolderHelper;
import com.xtel.sdk.utils.WidgetHelper;

import java.util.ArrayList;

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
    public void onBindViewHolder(ViewHolder holder, int position) {
        final LevelObject levelObject = arrayList.get(position);

        WidgetHelper.getInstance().setTextViewNoResult(holder.txt_stt, (position + 1));
        WidgetHelper.getInstance().setTextViewNoResult(holder.txt_point, levelObject.getLevel_limit());
        WidgetHelper.getInstance().setTextViewNoResult(holder.txt_name, levelObject.getLevel_name());

        holder.txt_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEnable)
                showQrCode(levelObject.getMember_card());
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

    public void addLevel(LevelObject levelObject) {
        arrayList.add(levelObject);
        notifyItemInserted((arrayList.size() - 1));
        notifyItemRangeRemoved((arrayList.size() - 1), arrayList.size());
    }

    @SuppressWarnings("ConstantConditions")
    private void showQrCode(String url) {
        if (!TextUnit.getInstance().validateText(url)) {
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

    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }
}
