package com.product.demo.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.product.demo.R;
import com.product.demo.util.ByteUtil;
import com.scandecode.ScanDecode;
import com.scandecode.inf.ScanInterface;
import com.speedata.libuhf.IUHFService;
import com.speedata.libuhf.UHFManager;
import com.speedata.libuhf.bean.SpdInventoryData;
import com.speedata.libuhf.bean.SpdWriteData;
import com.speedata.libuhf.interfaces.OnSpdInventoryListener;
import com.speedata.libuhf.interfaces.OnSpdWriteListener;
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

public class WriteTagActivity extends BaseActivity {

    ScanDecode scanDecode;

    IUHFService iuhfService;

    @BindView(R.id.tv_scanned_data)
    TextView scannedDataTV;

    @BindView(R.id.tv_tag)
    TextView tagTV;

    @Inject
    ToastUtil toastUtil;

    SpdInventoryData curSpdInventoryData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scanDecode = new ScanDecode(this);
        scanDecode.initService("false");//初始化扫描服务
        scanDecode.getBarCode(new ScanInterface.OnScanListener() {
            @Override
            public void getBarcode(final String scannedData) {
                scannedDataTV.post(new Runnable() {
                    @Override
                    public void run() {
                        scannedDataTV.setText(scannedData);
                    }
                });
            }

            @Override
            public void getBarcodeByte(byte[] bytes) {

            }
        });

        iuhfService = UHFManager.getUHFService(WriteTagActivity.this);
        int result = iuhfService.openDev();
        if(result != 0){
            toastUtil.showString("RFID设备起用失败！");
            finish();
        }
        inventoryStart();

    }


    @OnClick(R.id.btn_scan_data)
    public void scanData(){
        scanDecode.starScan();
    }

    @OnClick(R.id.btn_scan_tag)
    public void scanTag(){
        curSpdInventoryData = null;
        tagTV.setText(null);
        inventoryStart();
    }

    @OnClick(R.id.btn_write_tag)
    public void write(){
        final String text = scannedDataTV.getText().toString();
        if(TextUtils.isEmpty(text)){
            toastUtil.showString("没有扫描到的数据，请扫描");
            return;
        }
        if(text.length() != 24){
            toastUtil.showString("扫描到的数据有误，请重新扫描");
            return;
        }
        if(curSpdInventoryData == null){
            toastUtil.showString("没有连接到标签，不能写入");
            return;
        }
        Single.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                int result = iuhfService.selectCard(1, curSpdInventoryData.epc, true); //选卡
                if(result != 0){
                    return "与标签失去连接";
                }
                iuhfService.setOnWriteListener(new OnSpdWriteListener() {
                    @Override
                    public void getWriteData(SpdWriteData spdWriteData) {
                        //SpdWriteData
                        //int status 写入状态，成功与否
                        //byte[] EPCData 目标卡片EPC
                        //int EPCLen EPC长度
                        //int RSS 信号强度
                        if (spdWriteData.getStatus() == 0){
                            LogUtil.info(this, "" + spdWriteData.getEPCData());
                        }
                    }
                });
                String hexString = ByteUtil.bytes2HexString(scannedDataTV.getText().toString().getBytes("utf8"));
                int writeResult = iuhfService.writeArea(3,0, hexString.length() / 4,"00000000", StringUtils.stringToByte(hexString));
                LogUtil.info(this, "writeArea = " + writeResult);
                if (writeResult == 0) {
                    return "数据写入成功！";
                }else{
                    return "数据写入失败！";
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleSubscriber<String>()
        {
            @Override
            public void onSuccess(String value) {
                toastUtil.showString(value);
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
                toastUtil.showString(error.getMessage());

            }
        });

    }

    @Override
    public void onActivityFinishOrDestroy() {
        super.onActivityFinishOrDestroy();
        iuhfService.inventoryStop();
        iuhfService.closeDev();
        scanDecode.onDestroy();
    }

    @Override
    public int getLayoutViewID() {
        return R.layout.activity_write_tag;
    }

    @Override
    public void dagger2Inject(ActivityComponent component) {
        component.inject(this);
    }

    private void inventoryStart(){
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
                            tagTV.post(new Runnable() {
                                @Override
                                public void run() {
                                    tagTV.setText(curSpdInventoryData.epc);
                                }
                            });
                            iuhfService.inventoryStop();
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
                }
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
                toastUtil.showString("RFID功能启用失败！");
            }
        });
    }

}
