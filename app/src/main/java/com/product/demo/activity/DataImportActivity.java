package com.product.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.product.demo.R;
import com.product.demo.greendao.AssetsDao;
import com.product.demo.greendao.DaoSession;
import com.product.demo.greendao.entity.Assets;
import com.product.demo.greendao.entity.BarCode;
import com.product.demo.util.JxlExcelUtil;
import com.product.demo.util.Poi2007ExcelUtil;

import java.util.List;
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

public class DataImportActivity extends BaseActivity {


    @BindView(R.id.tv_warning)
    TextView warningTV;

    @BindView(R.id.btn_sure)
    TextView sureBtn;

    @Inject
    ToastUtil toastUtil;

    @Inject
    DaoSession daoSession;

    AssetsDao assetsDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assetsDao = daoSession.getAssetsDao();
    }

    @OnClick(R.id.btn_sure)
    public void sure(){
        warningTV.setText("正在导入中,请稍后...");
        sureBtn.setEnabled(false);
        Single.fromCallable(new Callable<List<Assets>>() {
            @Override
            public List<Assets> call() throws Exception {
                List<Assets> assetsList = new JxlExcelUtil().importData();
                assetsDao.deleteAll();
                assetsDao.insertInTx(assetsList);
                return assetsList;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleSubscriber<List<Assets>>()
        {
            @Override
            public void onSuccess(List<Assets> assetsList) {
                onFinish("数据导入成功！");
                LogUtil.info(this, "barcode size = " + assetsDao.count());
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
                onFinish("数据导入失败,原因：" + error.getMessage());
            }

            public void onFinish(String result){
                warningTV.setText(result);
                sureBtn.setEnabled(true);
            }
        });
    }

    @OnClick(R.id.btn_cancel)
    public void cancel() {
        finish();
    }

    @Override
    public int getLayoutViewID() {
        return R.layout.activity_data_import;
    }

    @Override
    public void dagger2Inject(ActivityComponent component) {
        component.inject(this);
    }
}
