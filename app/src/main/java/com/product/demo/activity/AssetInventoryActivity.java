package com.product.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.product.zcpd.R;
import com.product.demo.adapter.AssetsListAdapter;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import javax.inject.Inject;

import base.android.BaseActivity;
import base.dagger2.component.ActivityComponent;
import base.util.LogUtil;
import base.util.ToastUtil;
import butterknife.BindView;

public class AssetInventoryActivity extends BaseActivity {

    @BindView(R.id.lv)
    ListView listView;
    @BindView(R.id.tv_warning)
    TextView warningTV;

    @Inject
    ToastUtil toastUtil;
    @Inject
    DaoSession daoSession;

    IUHFService iuhfService;
    List<Assets> assetsList = new ArrayList<Assets>();
    List<String> epcList = new ArrayList<String>();
    AssetsListAdapter assetsListAdapter;
    private boolean running = false;
    LinkedBlockingQueue<String> epcsScanedToHandle = new LinkedBlockingQueue<String>();
    private boolean devOpened = false;
    String curEpc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        initIUHfService();

    }

    private void initIUHfService() {
        iuhfService = UHFManager.getUHFService(AssetInventoryActivity.this);
        iuhfService.setOnReadListener(new OnSpdReadListener() {
            @Override
            public void getReadData(SpdReadData spdReadData) {
                //SpdReadData
                //int status 读取状态，成功与否
                //byte[] EPCData 目标卡片EPC
                //byte[] ReadData 读到的数据
                //int EPCLen EPC长度
                //int DataLen 读到的数据长度
                //int RSS 信号强度
                LogUtil.info(this, "thread = " + Thread.currentThread());
                if(spdReadData.getStatus() == 0){
                    String readHexString = StringUtils.byteToHexString(spdReadData.getReadData(), spdReadData.getDataLen());
                    String realData = null;
                    if("000000000000000000000000000000000000000000000000".equals(readHexString)){
                        realData = null;
                    }else{
                        realData = new String(ByteUtil.hexString2Bytes(readHexString));
                        LogUtil.info(this, "getReadData = " + realData);
                    }
                    onFindBarCode(curEpc, realData);
                }
            }
        });
        iuhfService.setOnInventoryListener(new OnSpdInventoryListener() {
            @Override
            public void getInventoryData(SpdInventoryData spdInventoryData) {
                //SpdInventoryData
                //String epc 卡片EPC（16进制）
                //String rssi 信号强度
                //String tid 存放TID或USER数据（仅R2000模块支持）
                LogUtil.info(this, spdInventoryData.epc + "");
                LogUtil.info(this, spdInventoryData.rssi + "");
                LogUtil.info(this, spdInventoryData.tid + "");
                try {
                    epcsScanedToHandle.put(spdInventoryData.epc);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {

                int result = iuhfService.openDev();
                if(result != 0){
                    toastUtil.showString("RFID设备起用失败！");
                    finish();
                    return;
                }
                devOpened = true;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        warningTV.setText("正在盘点，已盘点到的资产：");
                    }
                });
                running = true;
                while (running){
                    try {
                        if(epcsScanedToHandle.isEmpty()){
                            iuhfService.inventoryStart();
                            LogUtil.info(this, "开始盘点");
                        }
                        curEpc = epcsScanedToHandle.take();
                        LogUtil.info(this, "拿到epc，是否已处理过 = " + epcList.contains(curEpc));
                        if(!epcList.contains(curEpc)){
                            epcList.add(curEpc);
                            read(curEpc);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        if(devOpened){
            super.onBackPressed();
        }else{
            toastUtil.showString("正在启用RFID设备，请稍后再操作");
        }
    }

    private void onFindBarCode(final String epc, final String barCode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AssetsDao assetsDao = daoSession.getAssetsDao();
                if(barCode == null){
                    Assets assets = assetsDao.queryBuilder().where(AssetsDao.Properties.Epc.eq(epc)).unique();
                    if(assets == null){
                        assets = new Assets();
                        assets.setEpc(epc);
                        assets.setBarCode(barCode);
                        assets.setStatus(Assets.STATUS_SCAN);
                        assetsDao.insert(assets);
                    }
                    assetsList.add(assets);
                    assetsListAdapter.notifyDataSetChanged();
                }else{
                    Assets assets = assetsDao.queryBuilder().where(AssetsDao.Properties.BarCode.eq(barCode),
                            AssetsDao.Properties.Status.notEq(Assets.STATUS_SCAN)).unique();
                    if(assets != null){
                        assets.setStatus(Assets.STATUS_MATCH);
                        assetsDao.update(assets);
                    }else{
                        assets = assetsDao.queryBuilder().where(AssetsDao.Properties.BarCode.eq(barCode),
                                AssetsDao.Properties.Status.eq(Assets.STATUS_SCAN)).unique();
                        if(assets == null){
                            assets = new Assets();
                            assets.setEpc(epc);
                            assets.setBarCode(barCode);
                            assets.setStatus(Assets.STATUS_SCAN);
                            assetsDao.insert(assets);
                        }
                    }
                    assetsList.add(assets);
                    assetsListAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    private void initView() {
        assetsListAdapter = new AssetsListAdapter(this, assetsList);
        listView.setAdapter(assetsListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Assets assets = assetsList.get(i);
                if(AssetsListActivity.INTENT_KEY_STATUS_IMPORT.equals(assets.getStatus()) || AssetsListActivity.INTENT_KEY_STATUS_MATCH.equals(assets.getStatus())){
                    Intent intent = new Intent(AssetInventoryActivity.this, AssetsDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(AssetsDetailActivity.INTENT_KEY_ASSETS, assetsList.get(i));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onActivityFinishOrDestroy() {
        running = false;
        super.onActivityFinishOrDestroy();
        iuhfService.inventoryStop();
        iuhfService.closeDev();
    }

    @Override
    public int getLayoutViewID() {
        return R.layout.activity_assets_inventory;
    }

    @Override
    public void dagger2Inject(ActivityComponent component) {
        component.inject(this);
    }

    private void read(final String epc){
        iuhfService.selectCard(1, epc, true); //选卡
        int readArea = iuhfService.readArea(3,0,12,"00000000");
        LogUtil.info(this, "readArea = " + readArea);
        if (readArea != 0) {
            toastUtil.showString("读取数据失败！");
        }
        iuhfService.selectCard(1, "", false); //取消选卡
        LogUtil.info(this, "read thread = " + Thread.currentThread());

    }
}
