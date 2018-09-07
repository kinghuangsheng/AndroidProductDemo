package com.product.demo.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.product.demo.R;
import com.product.demo.util.ByteUtil;
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

import java.io.UnsupportedEncodingException;
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

public class AssetInventoryActivity extends BaseActivity {

    IUHFService iuhfService;

    @BindView(R.id.tv_inventory)
    TextView inventoryTV;
    @BindView(R.id.btn_start)
    Button startBtn;
    @BindView(R.id.btn_stop)
    Button stopBtn;

    @Inject
    ToastUtil toastUtil;

    SpdInventoryData curSpdInventoryData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iuhfService = UHFManager.getUHFService(AssetInventoryActivity.this);
        int result = iuhfService.openDev();
        if(result != 0){
            toastUtil.showString("RFID设备起用失败！");
            finish();
        }

    }

    @OnClick(R.id.btn_stop)
    public void stop(){
        startBtn.setVisibility(View.VISIBLE);
        stopBtn.setVisibility(View.INVISIBLE);
        iuhfService.inventoryStop();
    }

    @OnClick(R.id.btn_start)
    public void start(){
        inventoryStart();
    }

    @Override
    public void onActivityFinishOrDestroy() {
        super.onActivityFinishOrDestroy();
        iuhfService.inventoryStop();
        iuhfService.closeDev();
    }

    @Override
    public int getLayoutViewID() {
        return R.layout.activity_asset_inventory;
    }

    @Override
    public void dagger2Inject(ActivityComponent component) {
        component.inject(this);
    }

    private void inventoryStart(){
        startBtn.setVisibility(View.INVISIBLE);
        stopBtn.setVisibility(View.VISIBLE);
        Single.fromCallable(new Callable<String>() {
            @Override
            public String call() {
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
                        if(curSpdInventoryData == null
                                || !curSpdInventoryData.rssi.equals(spdInventoryData.rssi)){
                            curSpdInventoryData = spdInventoryData;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    stop();
                                }
                            });
                            read();
                        }
                    }
                });
                //开始盘点
                iuhfService.inventoryStart();
                return "";
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleSubscriber<String>()
        {
            @Override
            public void onSuccess(String value) {
                if(!TextUtils.isEmpty(value)){
                    toastUtil.showString(value);
                    finish();
                }
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
                toastUtil.showString("RFID功能启用失败！");
                finish();
            }
        });
    }

    private void read(){
        Single.fromCallable(new Callable<String>() {
            @Override
            public String call() {
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
                        if(spdReadData.getStatus() == 0){
                            String readHexString = StringUtils.byteToHexString(spdReadData.getReadData(), spdReadData.getDataLen());
                            final String realData = new String(ByteUtil.hexString2Bytes(readHexString));
                            LogUtil.info(this, "getReadData = " + realData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    inventoryTV.setText(realData);
                                }
                            });
                        }
                    }
                });
                iuhfService.selectCard(1, curSpdInventoryData.epc, true); //选卡
                int readArea = iuhfService.readArea(3,0,12,"00000000");
                LogUtil.info(this, "readArea = " + readArea);
                if (readArea != 0) {
                    return "读取数据失败！";
                }
                return "";
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleSubscriber<String>()
        {
            @Override
            public void onSuccess(String value) {
                if(!TextUtils.isEmpty(value)){
                    toastUtil.showString(value);
                }
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
                toastUtil.showString("读取数据失败！");
            }
        });
    }
}
