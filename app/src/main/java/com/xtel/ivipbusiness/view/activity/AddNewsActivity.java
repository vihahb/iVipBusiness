package com.xtel.ivipbusiness.view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.presenter.AddNewsPresenter;
import com.xtel.ivipbusiness.view.activity.inf.IAddNewsView;
import com.xtel.ivipbusiness.view.adapter.SpinnerOneIconAdapter;
import com.xtel.ivipbusiness.view.adapter.TypeAdapter;
import com.xtel.sdk.utils.WidgetHelper;

import java.util.Calendar;

public class AddNewsActivity extends BasicActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, IAddNewsView {
    private AddNewsPresenter presenter;

    private ImageView img_banner, img_camera;
    private EditText edt_title, edt_des, edt_number_voucher, edt_sale, edt_begin_time, edt_end_time, edt_take_effect, edt_point;
    private TextView txt_public;
    private CheckBox chk_voucher;
    private Spinner sp_type, sp_type_sale;

    private LinearLayout layout_voucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);

        presenter = new AddNewsPresenter(this);
        initToolbar(R.id.add_news_toolbar, null);
        initView();
        initType();
        initTypeSale();
        initListener();
    }

    //    Lấy toàn bộ view
    private void initView() {
        img_banner = findImageView(R.id.add_news_img_banner);
        img_camera = findImageButton(R.id.add_news_img_camera);

        txt_public = findTextView(R.id.add_news_txt_public);

        edt_number_voucher = findEditText(R.id.add_news_edt_number_of_voucher);
        edt_sale = findEditText(R.id.add_news_edt_sale);
        edt_begin_time = findEditText(R.id.add_news_edt_sale);
        edt_end_time = findEditText(R.id.add_news_edt_sale);
        edt_take_effect = findEditText(R.id.add_news_edt_sale);
        edt_point = findEditText(R.id.add_news_edt_sale);
        edt_des = findEditText(R.id.add_news_edt_des);

        chk_voucher = findCheckBox(R.id.add_news_chk_create_news);
        layout_voucher = findLinearLayout(R.id.add_news_layout_voucher);
    }

    //    Khởi tạo spinner chọn type
    private void initType() {
        sp_type = findSpinner(R.id.add_news_sp_type);
        TypeAdapter typeAdapter = new TypeAdapter(this);
        sp_type.setAdapter(typeAdapter);
    }

    //    Khởi tạo spinner chọn loại giảm giá
    private void initTypeSale() {
        String[] arraylist = getResources().getStringArray(R.array.type_sale);

        sp_type_sale = findSpinner(R.id.add_news_sp_type_salse);
        SpinnerOneIconAdapter typeAdapter = new SpinnerOneIconAdapter(this, R.drawable.ic_action_gender, arraylist);
        sp_type_sale.setAdapter(typeAdapter);
    }

    private void initListener() {
        chk_voucher.setOnCheckedChangeListener(this);
        edt_begin_time.setOnClickListener(this);
        edt_end_time.setOnClickListener(this);
        edt_take_effect.setOnClickListener(this);
    }

    private void selectTime(final int type) {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this, R.style.AppCompatAlertDialogStyle, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (type == 0) {
                    WidgetHelper.getInstance().setEditTextTime(edt_begin_time, getString(R.string.open_time) + ": ", hourOfDay, minute);
//                    BEGIN_TIME = hourOfDay + ":" + minute;
                } else if (type == 1) {
                    WidgetHelper.getInstance().setEditTextTime(edt_end_time, getString(R.string.close_time) + ": ", hourOfDay, minute);
//                    END_TIME = hourOfDay + ":" + minute;
                } else {
                    WidgetHelper.getInstance().setEditTextTime(edt_take_effect, getString(R.string.close_time) + ": ", hourOfDay, minute);
                }
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }





























    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.add_news_edt_begin_time:
                selectTime(0);
                break;
            case R.id.add_news_edt_end_time:
                selectTime(1);
                break;
            case R.id.add_news_edt_take_effect:
                selectTime(3);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
//             Prepare the View for the animation
            layout_voucher.setVisibility(View.VISIBLE);
            layout_voucher.setAlpha(0.0f);

//             Start the animation
            layout_voucher.animate()
                    .translationY(0)
                    .alpha(1.0f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                        }
                    });
            Log.e("onCheckedChanged", "show");
        } else {
            layout_voucher.animate()
                    .translationY(-layout_voucher.getHeight())
                    .alpha(0.0f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            layout_voucher.setVisibility(View.GONE);
//            hideFloatingActionButton(layout_voucher);
                        }
                    });
            Log.e("onCheckedChanged", "show");
        }
    }

    @Override
    public void onGetDataError() {

    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
