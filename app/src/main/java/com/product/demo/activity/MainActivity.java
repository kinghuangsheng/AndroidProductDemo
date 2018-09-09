package com.product.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.product.demo.R;
import com.product.demo.greendao.AssetsDao;
import com.product.demo.greendao.DaoSession;
import com.product.demo.greendao.entity.Assets;

import javax.inject.Inject;

import base.android.BaseActivity;
import base.dagger2.component.ActivityComponent;
import base.util.ToastUtil;
import butterknife.BindView;
import butterknife.OnClick;

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

    long importCount;
    long matchCount;
    long scanCount;

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
        importCount = assetsDao.queryBuilder().where(AssetsDao.Properties.Status.eq(Assets.STATUS_IMPORT)).count();
        matchCount = assetsDao.queryBuilder().where(AssetsDao.Properties.Status.eq(Assets.STATUS_MATCH)).count();
        importCountBtn.setText("已导入总数：" + (importCount + matchCount));
        matchCountBtn.setText("已匹配数：" + matchCount);
        scanCount = assetsDao.queryBuilder().where(AssetsDao.Properties.Status.eq(Assets.STATUS_SCAN)).count();
        scanCountBtn.setText("已扫描但未匹配数：" + scanCount);
    }

    @OnClick(R.id.btn_import_count)
    public void importList(){
        if(importCount + matchCount == 0){
            toastUtil.showString("暂无数据");
            return;
        }
        Intent intent = new Intent(this, AssetsListActivity.class);
        intent.putExtra(AssetsListActivity.INTENT_KEY_STATUS, AssetsListActivity.INTENT_KEY_STATUS_IMPORT);
        startActivity(intent);
    }
    @OnClick(R.id.btn_match_count)
    public void matchList(){
        if(matchCount == 0){
            toastUtil.showString("暂无数据");
            return;
        }
        Intent intent = new Intent(this, AssetsListActivity.class);
        intent.putExtra(AssetsListActivity.INTENT_KEY_STATUS, AssetsListActivity.INTENT_KEY_STATUS_MATCH);
        startActivity(intent);
    }
    @OnClick(R.id.btn_scan_count)
    public void scanList(){
        if(scanCount == 0){
            toastUtil.showString("暂无数据");
            return;
        }
        Intent intent = new Intent(this, AssetsListActivity.class);
        intent.putExtra(AssetsListActivity.INTENT_KEY_STATUS, AssetsListActivity.INTENT_KEY_STATUS_SCAN);
        startActivity(intent);
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
//        toastUtil.showString("功能暂未实现");
        startActivity(new Intent(this, SettingActivity.class));
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
