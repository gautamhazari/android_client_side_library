package com.gsma.mobileconnect.r2.android.clientside.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.gsma.mobileconnect.r2.android.clientside.R;

public class AboutActivity extends BaseActivity {

    private Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        init();
        configureToolbar(toolbar, getString(R.string.action_about));
    }

    @Override
    protected void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }
}
