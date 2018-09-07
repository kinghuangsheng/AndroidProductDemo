package com.product.demo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.product.demo.R;
import com.product.demo.config.Constants;
import com.product.demo.exception.BusinessException;
import com.product.demo.greendao.BarCodeDao;
import com.product.demo.greendao.DaoMaster;
import com.product.demo.greendao.DaoSession;
import com.product.demo.greendao.entity.BarCode;
import com.product.demo.util.ByteUtil;
import com.product.demo.util.Poi2007ExcelUtil;
import com.scandecode.ScanDecode;
import com.scandecode.inf.ScanInterface;
import com.speedata.libuhf.IUHFService;
import com.speedata.libuhf.UHFManager;
import com.speedata.libuhf.bean.SpdInventoryData;
import com.speedata.libuhf.bean.SpdReadData;
import com.speedata.libuhf.bean.SpdWriteData;
import com.speedata.libuhf.interfaces.OnSpdInventoryListener;
import com.speedata.libuhf.interfaces.OnSpdReadListener;
import com.speedata.libuhf.interfaces.OnSpdWriteListener;
import com.speedata.libuhf.utils.StringUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import base.android.BaseActivity;
import base.dagger2.component.ActivityComponent;
import base.util.LogUtil;
import base.util.ToastUtil;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {



    @OnClick(R.id.btn_write_tag)
    public void writeTag(){
        startActivity(new Intent(this, WriteTagActivity.class));
    }
    @OnClick(R.id.btn_asset_inventory)
    public void assetInventory(){
        startActivity(new Intent(this, AssetInventoryActivity.class));
    }
    @OnClick(R.id.btn_data_import)
    public void dataImport(){
        startActivity(new Intent(this, WriteTagActivity.class));
    }
    @OnClick(R.id.btn_setting)
    public void setting(){
        startActivity(new Intent(this, TestActivity.class));
    }

    @Override
    public int getLayoutViewID() {
        return R.layout.activity_main;
    }

    @Override
    public void dagger2Inject(ActivityComponent component) {
        component.inject(this);
    }
}
