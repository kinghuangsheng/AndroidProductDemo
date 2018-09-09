package com.product.demo.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.product.zcpd.R;
import com.product.demo.greendao.AssetsDao;
import com.product.demo.greendao.DaoSession;
import com.product.demo.greendao.entity.Assets;
import com.product.demo.util.ByteUtil;
import com.speedata.libuhf.IUHFService;
import com.speedata.libuhf.UHFManager;
import com.speedata.libuhf.bean.SpdInventoryData;
import com.speedata.libuhf.bean.SpdReadData;
import com.speedata.libuhf.interfaces.OnSpdInventoryListener;
import com.speedata.libuhf.interfaces.OnSpdReadListener;
import com.speedata.libuhf.utils.StringUtils;

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

public class AssetsDetailActivity extends BaseActivity {


    public final static String INTENT_KEY_ASSETS = "INTENT_KEY_ASSETS";

    @BindView(R.id.tv_inventory)
    TextView inventoryTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Assets assets = (Assets) getIntent().getSerializableExtra(INTENT_KEY_ASSETS);
        inventoryTV.append("\n资产名称：" + assets.getName());
        inventoryTV.append("\n\n资产编码：" + assets.getCode());
        inventoryTV.append("\n\n资产条码：" + assets.getBarCode());
        inventoryTV.append("\n\n资产序列号：" + assets.getSerialNumber());
        inventoryTV.append("\n\n规格型号：" + assets.getSpecifications());
        inventoryTV.append("\n\n计量单位：" + assets.getUnit());
        inventoryTV.append("\n\n供应商：" + assets.getSupplier());
        inventoryTV.append("\n\n生产厂家：" + assets.getProduceCompany());
        inventoryTV.append("\n\n维保单位：" + assets.getMaintainCompany());
        inventoryTV.append("\n\n维保开始日期：" + assets.getMaintainStartDate());
        inventoryTV.append("\n\n维保结束日期：" + assets.getMaintainEndDate());
        inventoryTV.append("\n\n领用日期：" + assets.getUseDate());
        inventoryTV.append("\n\n使用部门：" + assets.getDepartment());
        inventoryTV.append("\n\n位置：" + assets.getPosition());
        inventoryTV.append("\n\n用户ID：" + assets.getUserId());
        inventoryTV.append("\n\n用户：" + assets.getUserName());
        inventoryTV.append("\n\n主资产编码：" + assets.getMainAssetsCode());
        inventoryTV.append("\n\n资产状态：" + assets.getState());
        inventoryTV.append("\n\n");

    }

    @Override
    public int getLayoutViewID() {
        return R.layout.activity_assets_detail;
    }

    @Override
    public void dagger2Inject(ActivityComponent component) {
        component.inject(this);
    }

}
