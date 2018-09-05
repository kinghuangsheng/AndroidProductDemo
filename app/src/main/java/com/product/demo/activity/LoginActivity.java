package com.product.demo.activity;

import android.os.Bundle;
import android.widget.EditText;

import com.product.demo.R;
import com.product.demo.greendao.DaoSession;
import com.product.demo.greendao.UserDao;
import com.product.demo.greendao.entity.User;
import com.product.demo.retrofit.service.RequestService;

import javax.inject.Inject;

import base.android.BaseActivity;
import base.dagger2.component.ActivityComponent;
import base.util.LogUtil;
import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @Inject
    DaoSession daoSession;

    @Inject
    RequestService requestService;

    @BindView(R.id.et_account)
    EditText accountET;

    @BindView(R.id.et_password)
    EditText passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountET.setSelection(accountET.getText().length());
        passwordET.setSelection(passwordET.getText().length());
    }

    @OnClick(R.id.btn_login)
    public void login(){
        User user = new User();
        user.setAccount(accountET.getText().toString());
        user.setPassword(passwordET.getText().toString());
        daoSession.getUserDao().save(user);
        LogUtil.i(this, "" + daoSession.getUserDao().count());
    }

    @Override
    public int getLayoutViewID() {
        return R.layout.activity_login;
    }

    @Override
    public void dagger2Inject(ActivityComponent component) {
        component.inject(this);
    }
}
