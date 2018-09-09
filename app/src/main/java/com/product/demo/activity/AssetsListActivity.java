package com.product.demo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.product.zcpd.R;
import com.product.demo.adapter.AssetsListAdapter;
import com.product.demo.greendao.AssetsDao;
import com.product.demo.greendao.DaoSession;
import com.product.demo.greendao.entity.Assets;
import com.product.demo.util.JxlExcelUtil;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import javax.inject.Inject;

import base.android.BaseActivity;
import base.dagger2.component.ActivityComponent;
import base.util.ToastUtil;
import butterknife.BindView;
import butterknife.OnClick;

public class AssetsListActivity extends BaseActivity {

    public static final String INTENT_KEY_STATUS = "INTENT_KEY_STATUS";
    public static final String INTENT_KEY_STATUS_IMPORT = "INTENT_KEY_STATUS_IMPORT";
    public static final String INTENT_KEY_STATUS_MATCH = "INTENT_KEY_STATUS_MATCH";
    public static final String INTENT_KEY_STATUS_UNMATCH = "INTENT_KEY_STATUS_UNMATCH";
    public static final String INTENT_KEY_STATUS_SCAN = "INTENT_KEY_STATUS_SCAN";

    @Inject
    DaoSession daoSession;
    @Inject
    ToastUtil toastUtil;

    AssetsDao assetsDao;

    @BindView(R.id.lv)
    ListView listView;
    @BindView(R.id.btn_export)
    Button exportBtn;

    private String status;

    List<Assets> assetsList;

    String exportFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assetsDao = daoSession.getAssetsDao();
        status = getIntent().getStringExtra("INTENT_KEY_STATUS");
        QueryBuilder<Assets> queryBuilder = null;
        if (INTENT_KEY_STATUS_SCAN.equals(status)) {
            queryBuilder = assetsDao.queryBuilder().where(AssetsDao.Properties.Status.eq(Assets.STATUS_SCAN));
            exportFileName = "盘点失败资产列表.xls";
        } else if (INTENT_KEY_STATUS_MATCH.equals(status)) {
            exportFileName = "盘点成功资产列表.xls";
            queryBuilder = assetsDao.queryBuilder().where(AssetsDao.Properties.Status.eq(Assets.STATUS_MATCH));
        } else if (INTENT_KEY_STATUS_UNMATCH.equals(status)) {
            exportFileName = "未盘点资产列表.xls";
            queryBuilder = assetsDao.queryBuilder().where(AssetsDao.Properties.Status.eq(Assets.STATUS_IMPORT));
        } else {
            exportFileName = "导入的资产列表.xls";
            queryBuilder = assetsDao.queryBuilder().whereOr(AssetsDao.Properties.Status.eq(Assets.STATUS_IMPORT),
                    AssetsDao.Properties.Status.eq(Assets.STATUS_MATCH)).orderAsc(AssetsDao.Properties.Status);
        }
        assetsList = queryBuilder.list();
        listView.setAdapter(new AssetsListAdapter(this, assetsList));
        if (INTENT_KEY_STATUS_IMPORT.equals(status) || INTENT_KEY_STATUS_MATCH.equals(status) || INTENT_KEY_STATUS_UNMATCH.equals(status)) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(AssetsListActivity.this, AssetsDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(AssetsDetailActivity.INTENT_KEY_ASSETS, assetsList.get(i));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    }

    @OnClick(R.id.btn_export)
    public void export() {
        exportBtn.setText("正在导出，请稍侯...");
        exportBtn.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new JxlExcelUtil().export(assetsList, exportFileName);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            exportBtn.setText("导出文件成功");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            exportBtn.setText("导出文件失败");
                        }
                    });
                }
            }
        }).start();


    }

    @Override
    public int getLayoutViewID() {
        return R.layout.activity_import_list;
    }

    @Override
    public void dagger2Inject(ActivityComponent component) {
        component.inject(this);
    }
}
