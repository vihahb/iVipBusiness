package com.xtel.ivipbusiness.view.activity;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Lê Công Long Vũ on 1/3/2017
 */

public abstract class IActivity extends AppCompatActivity {

    protected Toolbar findToolbar(int id) {
        return (Toolbar) findViewById(id);
    }

    protected DrawerLayout findDrawerLayout(int id) {
        return (DrawerLayout) findViewById(id);
    }

    protected NavigationView findNavigationView(int id) {
        return (NavigationView) findViewById(id);
    }

    protected FloatingActionButton findFloatingActionButton(int id) {
        return (FloatingActionButton) findViewById(id);
    }

    protected RecyclerView findRecyclerView(int id) {
        return (RecyclerView) findViewById(id);
    }

    protected EditText findEditText(int id) {
        return (EditText) findViewById(id);
    }

    protected TextView findTextView(int id) {
        return (TextView) findViewById(id);
    }

    protected Button findButton(int id) {
        return (Button) findViewById(id);
    }

    protected CheckBox findCheckBox(int id) {
        return (CheckBox) findViewById(id);
    }

    protected Spinner findSpinner(int id) {
        return (Spinner) findViewById(id);
    }

    protected ProgressBar findProgressBar(int id) {
        return (ProgressBar) findViewById(id);
    }

    protected SeekBar findSeekBar(int id) {
        return (SeekBar) findViewById(id);
    }

    protected RatingBar findRatingBar(int id) {
        return (RatingBar) findViewById(id);
    }
}
