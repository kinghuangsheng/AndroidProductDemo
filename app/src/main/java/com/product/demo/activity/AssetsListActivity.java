package com.product.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.product.demo.R;
import com.product.demo.adapter.AssetsListAdapter;
import com.product.demo.greendao.AssetsDao;
import com.product.demo.greendao.DaoSession;
import com.product.demo.greendao.entity.Assets;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import javax.inject.Inject;

import base.android.BaseActivity;
import base.dagger2.component.ActivityComponent;
import base.util.ToastUtil;
import butterknife.BindView;

public class AssetsListActivity extends BaseActivity {

    public static final String INTENT_KEY_STATUS = "INTENT_KEY_STATUS";
    public static final String INTENT_KEY_STATUS_IMPORT = "INTENT_KEY_STATUS_IMPORT";
    public static final String INTENT_KEY_STATUS_MATCH = "INTENT_KEY_STATUS_MATCH";
    public static final String INTENT_KEY_STATUS_SCAN = "INTENT_KEY_STATUS_SCAN";

    @Inject
    DaoSession daoSession;
    @Inject
    ToastUtil toastUtil;

    AssetsDao assetsDao;

    @BindView(R.id.lv)
    ListView listView;

    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assetsDao = daoSession.getAssetsDao();
        status = getIntent().getStringExtra("INTENT_KEY_STATUS");
        QueryBuilder<Assets> queryBuilder = null;
        if(INTENT_KEY_STATUS_SCAN.equals(status)){
            queryBuilder = assetsDao.queryBuilder().where(AssetsDao.Properties.Status.eq(Assets.STATUS_SCAN));
        }else if(INTENT_KEY_STATUS_MATCH.equals(status)){
            queryBuilder = assetsDao.queryBuilder().where(AssetsDao.Properties.Status.eq(Assets.STATUS_MATCH));
        }else{
            queryBuilder = assetsDao.queryBuilder().whereOr(AssetsDao.Properties.Status.eq(Assets.STATUS_IMPORT),
                    AssetsDao.Properties.Status.eq(Assets.STATUS_MATCH));
        }
        final List<Assets> assetsList = queryBuilder.list();
        listView.setAdapter(new AssetsListAdapter(this, assetsList));
        if(INTENT_KEY_STATUS_IMPORT.equals(status) || INTENT_KEY_STATUS_MATCH.equals(status)){
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

    @Override
    public int getLayoutViewID() {
        return R.layout.activity_import_list;
    }

    @Override
    public void dagger2Inject(ActivityComponent component) {
        component.inject(this);
    }
}
