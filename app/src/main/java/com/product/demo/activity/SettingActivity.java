package com.product.demo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.product.zcpd.R;
import com.product.demo.config.Constants;
import com.product.demo.dialog.SetModuleDialog;
import com.product.demo.exception.BusinessException;
import com.product.demo.greendao.DaoSession;
import com.product.demo.greendao.entity.User;
import com.product.demo.retrofit.service.RequestService;
import com.product.demo.util.JxlExcelUtil;
import com.speedata.libuhf.IUHFService;
import com.speedata.libuhf.UHFManager;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import base.android.BaseActivity;
import base.dagger2.component.ActivityComponent;
import base.util.LogUtil;
import base.util.ToastUtil;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SettingActivity extends BaseActivity {

    @Inject
    ToastUtil toastUtil;

    @Inject
    SharedPreferences sharedPreferences;

    @BindView(R.id.et_old_password)
    EditText oldPasswordET;

    @BindView(R.id.et_new_password)
    EditText newPasswordET;
    @BindView(R.id.et_new_password2)
    EditText newPassword2ET;
    @BindView(R.id.btn_modify_password)
    Button modifyPasswordBtn;
    @BindView(R.id.et_antenna_power)
    EditText antennaPowerET;
    @BindView(R.id.btn_modify_antenna_power)
    Button modifyAntennaPowerBtn;

    IUHFService iuhfService;

    private boolean devOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iuhfService = UHFManager.getUHFService(this);

        initIUHfService();

    }

    @OnClick(R.id.btn_modify_antenna_power)
    public void modifyAntennaPower(){
//        SetModuleDialog setDialog = new SetModuleDialog(this, iuhfService, "xinlian");
//        setDialog.setTitle("设置");
//        setDialog.show();
        int antennaPower = Integer.parseInt(antennaPowerET.getText().toString());
        if(antennaPower < 0 || antennaPower > 30){
            toastUtil.showString("请输入正确的功率值");
        }
        int result = iuhfService.setAntennaPower(antennaPower);
        if(result == 0){
            toastUtil.showString("修改功率成功");
        }else{
            toastUtil.showString("修改功率失败");
        }
    }

    private void initIUHfService() {

        modifyAntennaPowerBtn.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int result = iuhfService.openDev();
                if(result != 0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toastUtil.showString("RFID设备起用失败！");
                            modifyAntennaPowerBtn.setVisibility(View.INVISIBLE);
                            antennaPowerET.setVisibility(View.INVISIBLE);
                        }
                    });
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        modifyAntennaPowerBtn.setEnabled(true);
                        devOpened = true;
                        int antennaPower = iuhfService.getAntennaPower();
                        antennaPowerET.setText("" + antennaPower);
                    }
                });

            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(devOpened){
            iuhfService.closeDev();
        }
    }

    @OnClick(R.id.btn_modify_password)
    public void modifyPassword(){
        final String oldPassword = oldPasswordET.getText().toString();
        final String newPassword = newPasswordET.getText().toString();
        final String newPassword2 = newPassword2ET.getText().toString();
        if("".equals(oldPassword)){
            toastUtil.showString("请输入原密码");
            return;
        }
        if("".equals(newPassword)){
            toastUtil.showString("请输入新密码");
            return;
        }
        if(!newPassword.equals(newPassword2)){
            toastUtil.showString("新密码不一致,请重新输入");
            return;
        }
        modifyPasswordBtn.setText("密码修改中");
        modifyPasswordBtn.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String account = sharedPreferences.getString(Constants.SHAREDPREFERENCES_KEY_ACCOUNT, "");
                    JxlExcelUtil.updatePassword(account, oldPassword, newPassword);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toastUtil.showString("密码修改成功");
                            modifyPasswordBtn.setEnabled(true);
                            modifyPasswordBtn.setText("修改密码");
                        }
                    });
                }catch (final BusinessException e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toastUtil.showString(e.getMessage());
                            modifyPasswordBtn.setText("修改密码");
                            modifyPasswordBtn.setEnabled(true);
                        }
                    });
                }
            }
        }).start();
    }


    @Override
    public int getLayoutViewID() {
        return R.layout.activity_setting;
    }

    @Override
    public void dagger2Inject(ActivityComponent component) {
        component.inject(this);
    }
}
