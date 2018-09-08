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
import com.product.demo.greendao.AssetsDao;
import com.product.demo.greendao.BarCodeDao;
import com.product.demo.greendao.DaoMaster;
import com.product.demo.greendao.DaoSession;
import com.product.demo.greendao.entity.Assets;
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


    @Inject
    DaoSession daoSession;
    @Inject
    ToastUtil toastUtil;

    AssetsDao assetsDao;

    @BindView(R.id.btn_import_count)
    Button importCountBtn;
    @BindView(R.id.btn_match_count)
    Button matchCountBtn;
    @BindView(R.id.btn_scan_count)
    Button scanCountBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assetsDao = daoSession.getAssetsDao();

    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshCount();
    }

    private void refreshCount(){
        long importCount = assetsDao.queryBuilder().where(AssetsDao.Properties.Status.eq(Assets.STATUS_IMPORT)).count();
        long matchCount = assetsDao.queryBuilder().where(AssetsDao.Properties.Status.eq(Assets.STATUS_MATCH)).count();
        importCountBtn.setText("已导入总数：" + (importCount + matchCount));
        matchCountBtn.setText("已匹配数：" + matchCount);
        long scanCount = assetsDao.queryBuilder().where(AssetsDao.Properties.Status.eq(Assets.STATUS_SCAN)).count();
        scanCountBtn.setText("已扫描但未匹配数：" + scanCount);
    }

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
        startActivity(new Intent(this, DataImportActivity.class));
    }
    @OnClick(R.id.btn_setting)
    public void setting(){
        toastUtil.showString("功能暂未实现");
//        startActivity(new Intent(this, TestActivity.class));
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
