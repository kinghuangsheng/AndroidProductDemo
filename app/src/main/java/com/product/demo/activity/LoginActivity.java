package com.product.demo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import com.product.demo.R;
import com.product.demo.config.Constants;
import com.product.demo.exception.BusinessException;
import com.product.demo.greendao.DaoSession;
import com.product.demo.greendao.entity.User;
import com.product.demo.retrofit.service.RequestService;
import com.product.demo.util.Poi2007ExcelUtil;
import com.product.demo.util.PoiExcelUtil;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import base.android.BaseActivity;
import base.dagger2.component.ActivityComponent;
import base.util.LogUtil;
import base.util.ToastUtil;
import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.AsyncOnSubscribe;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {

    @Inject
    DaoSession daoSession;

    @Inject
    ToastUtil toastUtil;

    @Inject
    RequestService requestService;

    @Inject
    SharedPreferences sharedPreferences;

    @BindView(R.id.et_account)
    EditText accountET;

    @BindView(R.id.et_password)
    EditText passwordET;

    @BindView(R.id.cb_save_password)
    CheckBox savePasswordCB;

    @BindView(R.id.btn_login)
    Button loginBtn;

    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(sharedPreferences.getBoolean(Constants.SHAREDPREFERENCES_KEY_SAVE_PASSWORD, false)){
            accountET.setText(sharedPreferences.getString(Constants.SHAREDPREFERENCES_KEY_ACCOUNT, ""));
            passwordET.setText(sharedPreferences.getString(Constants.SHAREDPREFERENCES_KEY_PASSWORD, ""));
            savePasswordCB.setChecked(true);
        }else{
            savePasswordCB.setChecked(false);
        }
        accountET.setSelection(accountET.getText().length());
        passwordET.setSelection(passwordET.getText().length());
    }

    @OnClick(R.id.btn_login)
    public void login(){
        final String account = accountET.getText().toString();
        final String password = passwordET.getText().toString();
        if("".equals(account)){
            toastUtil.showString("请输入账号");
            return;
        }
        if("".equals(password)){
            toastUtil.showString("请输入密码");
            return;
        }
        loginBtn.setEnabled(false);
        loginBtn.setText("登录中，请稍后...");
        Single.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                if(new Poi2007ExcelUtil().checkUser(account, password)){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Constants.SHAREDPREFERENCES_KEY_ACCOUNT, account);
                    editor.putString(Constants.SHAREDPREFERENCES_KEY_PASSWORD, password);
                    editor.putBoolean(Constants.SHAREDPREFERENCES_KEY_SAVE_PASSWORD, savePasswordCB.isChecked());
                    editor.commit();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }else{
                    throw new BusinessException("用户不存在或密码错误！");
                }
                return null;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleSubscriber<Object>()
        {
            @Override
            public void onSuccess(Object value) {
                onFinish("------onSuccess");
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
                toastUtil.showString(error.getMessage());
                onFinish("------onError");
            }

            public void onFinish(String result){
                LogUtil.info(this, result);
                loginBtn.setEnabled(true);
                loginBtn.setText("登录");
            }
        });
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
