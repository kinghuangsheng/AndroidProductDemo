package com.product.demo.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.product.zcpd.R;
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
    @BindView(R.id.btn_write_tag)
    TextView writeTabBtn;
    @BindView(R.id.btn_scan_tag)
    TextView scanTagBtn;

    @BindView(R.id.tv_tag)
    TextView tagTV;

    @Inject
    ToastUtil toastUtil;

    SpdInventoryData curSpdInventoryData;

    private boolean rfidDevOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initScan();

        initiUHFService();

        writeTabBtn.setEnabled(false);
        writeTabBtn.setText("正在启动RFID设备...");


    }

    private void initiUHFService() {
        iuhfService = UHFManager.getUHFService(WriteTagActivity.this);
        iuhfService.setOnInventoryListener(new OnSpdInventoryListener() {
            @Override
            public void getInventoryData(SpdInventoryData spdInventoryData) {
                //SpdInventoryData
                //String epc 卡片EPC（16进制）
                //String rssi 信号强度
                //String tid 存放TID或USER数据（仅R2000模块支持）
                iuhfService.inventoryStop();
                LogUtil.info(this, spdInventoryData.epc + "");
                LogUtil.info(this, spdInventoryData.rssi + "");
                LogUtil.info(this, spdInventoryData.tid + "");
                if(curSpdInventoryData == null
                        || !curSpdInventoryData.rssi.equals(spdInventoryData.rssi)){
                    curSpdInventoryData = spdInventoryData;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tagTV.setText(curSpdInventoryData.epc);
                        }
                    });
                }
            }
        });
        iuhfService.setOnWriteListener(new OnSpdWriteListener() {
            @Override
            public void getWriteData(SpdWriteData spdWriteData) {
                //SpdWriteData
                //int status 写入状态，成功与否
                //byte[] EPCData 目标卡片EPC
                //int EPCLen EPC长度
                //int RSS 信号强度
                LogUtil.info(this, "待写入回调 status = " + spdWriteData.getStatus() + "  epcData = "
                        + StringUtils.byteToHexString(spdWriteData.getEPCData(), spdWriteData.getEPCLen()));

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                int result = iuhfService.openDev();
                if(result != 0){
                    toastUtil.showString("RFID设备起用失败！");
                    finish();
                }
                rfidDevOpen = true;
                iuhfService.inventoryStart();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        writeTabBtn.setEnabled(true);
                        writeTabBtn.setText("写入");
                        scanTagBtn.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();
    }

    private void initScan() {
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
    }


    @OnClick(R.id.btn_scan_data)
    public void scanData(){
        scanDecode.starScan();
    }

    @OnClick(R.id.btn_scan_tag)
    public void scanTag(){
        curSpdInventoryData = null;
        tagTV.setText(null);
        iuhfService.inventoryStart();

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
        writeTabBtn.setEnabled(false);
        Single.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                int result = iuhfService.selectCard(1, curSpdInventoryData.epc, true); //选卡
                if(result != 0){
                    return "与标签失去连接";
                }
                String hexString = ByteUtil.bytes2HexString(scannedDataTV.getText().toString().getBytes("utf8"));
                LogUtil.info(this, "待写入16进制字符串 = " + hexString);
                int writeResult = iuhfService.writeArea(3,0, hexString.length() / 4,"00000000", StringUtils.stringToByte(hexString));
                LogUtil.info(this, "writeArea = " + writeResult);
                result = iuhfService.selectCard(1, "", false); //选卡
                if(result != 0){
                    return "与标签失去连接";
                }
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
                writeTabBtn.setEnabled(true);
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
                toastUtil.showString(error.getMessage());
                writeTabBtn.setEnabled(true);

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(rfidDevOpen){
            super.onBackPressed();
        }else{
            toastUtil.showString("正在启用RFID设备，请稍后再操作");
        }
    }

    @Override
    public void onActivityFinishOrDestroy() {
        super.onActivityFinishOrDestroy();
        iuhfService.inventoryStop();
        iuhfService.closeDev();
        try{
            scanDecode.onDestroy();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getLayoutViewID() {
        return R.layout.activity_write_tag;
    }

    @Override
    public void dagger2Inject(ActivityComponent component) {
        component.inject(this);
    }

}
