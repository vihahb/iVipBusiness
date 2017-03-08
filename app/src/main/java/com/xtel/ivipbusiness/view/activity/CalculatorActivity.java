package com.xtel.ivipbusiness.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xtel.ivipbusiness.R;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.TextUnit;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class CalculatorActivity extends BasicActivity {
    private TextView txt_monitor, txt_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        initToolbar(R.id.calculator_toolbar, null);
        initView();
        initDelete();
    }

    private void initView() {
        txt_monitor = findTextView(R.id.calculator_txt_monitor);
        txt_result = findTextView(R.id.calculator_txt_result);
    }

    private void initDelete() {
        ImageButton imageButton = findImageButton(R.id.calculator_img_delete);
        imageButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                txt_monitor.setText(null);
                return false;
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String monitor = txt_monitor.getText().toString();

                if (monitor.length() > 0) {
                    StringBuilder sb = new StringBuilder(monitor);
                    sb.deleteCharAt((sb.length() - 1));

                    txt_monitor.setText(sb);
                }
            }
        });
    }

    public void AC(View view) {
        txt_monitor.setText(null);
    }

    public void addition(View view) {
        String monitor = txt_monitor.getText().toString();

        if (monitor.length() > 0) {
            if (monitor.charAt((monitor.length() - 1)) != '+') {
                monitor += "+";
                txt_monitor.setText(monitor);
            }
        }
    }

    public void subtraction(View view) {
        String monitor = txt_monitor.getText().toString();

        if (monitor.length() > 0) {
            if (monitor.charAt((monitor.length() - 1)) != '-') {
                monitor += "-";
                txt_monitor.setText(monitor);
            }
        }
    }

    public void multiplication(View view) {
        String monitor = txt_monitor.getText().toString();

        if (monitor.length() > 0) {
            if (monitor.charAt((monitor.length() - 1)) != '*') {
                monitor += "*";
                txt_monitor.setText(monitor);
            }
        }
    }

    public void division(View view) {
        String monitor = txt_monitor.getText().toString();

        if (monitor.length() > 0) {
            if (monitor.charAt((monitor.length() - 1)) != '/') {
                monitor += "/";
                txt_monitor.setText(monitor);
            }
        }
    }

    public void percent(View view) {

    }

    public void dot(View view) {
        String monitor = txt_monitor.getText().toString();

        if (monitor.length() > 0) {
            if (monitor.charAt((monitor.length() - 1)) != '.') {
                monitor += ".";
                txt_monitor.setText(monitor);
            }
        }
    }

    public void equal(View view) {
        try {
            Expression expression = new ExpressionBuilder(txt_monitor.getText().toString()).build();

            double result = expression.evaluate();
            txt_result.setText(("= " + result));
            txt_monitor.setText(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void zero(View view) {
        String monitor = txt_monitor.getText().toString();
        monitor += "0";

        txt_monitor.setText(monitor);
    }

    public void doubleZero(View view) {
        String monitor = txt_monitor.getText().toString();
        monitor += "00";

        txt_monitor.setText(monitor);
    }

    public void one(View view) {
        String monitor = txt_monitor.getText().toString();
        monitor += "1";

        txt_monitor.setText(monitor);
    }

    public void two(View view) {
        String monitor = txt_monitor.getText().toString();
        monitor += "2";

        txt_monitor.setText(monitor);
    }

    public void three(View view) {
        String monitor = txt_monitor.getText().toString();
        monitor += "3";

        txt_monitor.setText(monitor);
    }

    public void four(View view) {
        String monitor = txt_monitor.getText().toString();
        monitor += "4";

        txt_monitor.setText(monitor);
    }

    public void five(View view) {
        String monitor = txt_monitor.getText().toString();
        monitor += "5";

        txt_monitor.setText(monitor);
    }

    public void six(View view) {
        String monitor = txt_monitor.getText().toString();
        monitor += "6";

        txt_monitor.setText(monitor);
    }

    public void seven(View view) {
        String monitor = txt_monitor.getText().toString();
        monitor += "7";

        txt_monitor.setText(monitor);
    }

    public void eight(View view) {
        String monitor = txt_monitor.getText().toString();
        monitor += "8";

        txt_monitor.setText(monitor);
    }

    public void nine(View view) {
        String monitor = txt_monitor.getText().toString();
        monitor += "9";

        txt_monitor.setText(monitor);
    }

    protected void setResultOk() {
        if (!TextUnit.getInstance().validateText(txt_result.getText().toString())) {
            showShortToast(getString(R.string.error_input_result));
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(Constants.MODEL, txt_result.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calculator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home)
            finish();
        else if (id == R.id.action_calculator_done) {
            setResultOk();
        }

        return super.onOptionsItemSelected(item);
    }
}