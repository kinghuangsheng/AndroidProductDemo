package com.product.demo.activity;

import com.product.demo.R;

import base.android.BaseActivity;
import base.dagger2.component.ActivityComponent;

public class MainActivity extends BaseActivity {


    @Override
    public int getLayoutViewID() {
        return R.layout.activity_login;
    }

    @Override
    public void dagger2Inject(ActivityComponent component) {
        component.inject(this);
    }
}
