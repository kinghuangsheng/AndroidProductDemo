package com.product.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.product.zcpd.R;
import com.product.demo.greendao.BarCodeDao;
import com.product.demo.greendao.DaoSession;
import com.product.demo.greendao.entity.BarCode;
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

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;

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

public class TestActivity extends BaseActivity {

    @BindView(R.id.btn_scan_data)
    View scanBtn;

    @BindView(R.id.btn_import)
    Button importBtn;

    ScanDecode scanDecode;

    IUHFService iuhfService;

    @Inject
    ToastUtil toastUtil;

    @Inject
    DaoSession daoSession;

    BarCodeDao barCodeDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        barCodeDao = daoSession.getBarCodeDao();

        scanDecode = new ScanDecode(this);
        scanDecode.initService("false");//初始化扫描服务
        scanDecode.getBarCode(new ScanInterface.OnScanListener() {
            @Override
            public void getBarcode(String s) {
                onDataScaned(s);
            }

            @Override
            public void getBarcodeByte(byte[] bytes) {

            }
        });

        iuhfService = UHFManager.getUHFService(this);

    }

    private boolean open = false;
    @OnClick(R.id.btn_open_close)
    public void onClickOpenClose(){
        if(open){
            iuhfService.closeDev();
            open = false;
        }else{
            iuhfService.openDev();
            open = true;
        }

    }

    @OnClick(R.id.btn_import)
    public void onClickImport(){
        importBtn.setText("数据导入中，请稍后...");
        Single.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
//                List<BarCode> barCodeList =  new Poi2007ExcelUtil().importData();
                List<BarCode> barCodeList =  null;
                barCodeDao.deleteAll();
                barCodeDao.insertInTx(barCodeList);
                return barCodeList;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleSubscriber<Object>()
        {
            @Override
            public void onSuccess(Object value) {
                onFinish("------onSuccess");
                LogUtil.info(this, "barcode size = " + barCodeDao.count());
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
                toastUtil.showString(error.getMessage());
                onFinish("------onError");
            }

            public void onFinish(String result){
                LogUtil.info(this, result);
                importBtn.setText("导入");
            }
        });
    }

    LinkedBlockingQueue<String> epcsScanedToHandle = new LinkedBlockingQueue<String>();

    private  boolean inventorying = false;
    @OnClick(R.id.btn_inventory)
    public void onClickInventory(){
        Intent intent = new Intent(
                Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivityForResult(intent, 1);
        if(inventorying){
            inventorying = false;
            iuhfService.inventoryStop();
        }else{
            inventorying = true;
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
            //开始盘点
            iuhfService.inventoryStart();
        }

    }
    @OnClick(R.id.btn_write)
    public void onClickWrite(){
        //↓↓↓↓↓↓↓↓↓↓↓↓重点注意↓↓↓↓↓↓↓↓↓↓↓↓
//写入操作会持续写入，写入回调有时会返多次值，当写入回调status得到一次为0的时候就可以认为写入成功
        iuhfService.selectCard(1, "E20040748801014026700AF9", true); //选卡
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
        String hexString = ByteUtil.bytes2HexString("HafI!@#12356".getBytes());
        byte[] b = hexString.getBytes();
         int writeArea = iuhfService.writeArea(3,0, hexString.length() / 4,"00000000", StringUtils.stringToByte(hexString));
        LogUtil.info(this, "writeArea = " + writeArea);
        if (writeArea != 0) {
            //参数错误
        }
    }
    @OnClick(R.id.btn_read)
    public void onClickRead(){
        //示例代码
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
                    String s = "";
                    for(int i = 0; i < spdReadData.getDataLen(); i++){
                        s += spdReadData.getReadData()[i] + ",";
                    }
                    LogUtil.info(this,  "getReadData bytes = " + s);
                    String realData = new String(ByteUtil.hexString2Bytes(readHexString));
                    LogUtil.info(this, "getReadData = " + realData);
                }

            }
        });
        iuhfService.selectCard(1, "E20040748801014026700AF9", true); //选卡
        int readArea = iuhfService.readArea(3,0,31,"00000000");
        LogUtil.info(this, "readArea = " + readArea);
        if (readArea != 0) {
            //参数错误
        }
    }


    @Override
    public void onActivityFinishOrDestroy() {
        super.onActivityFinishOrDestroy();
        iuhfService.closeDev();
        scanDecode.onDestroy();

    }

    public void onDataScaned(String data){
        QueryBuilder<BarCode> qb = barCodeDao.queryBuilder().where(BarCodeDao.Properties.Code.eq(data));
        BarCode barCode = qb.unique();
        if(barCode != null){
            barCode.setStatus(BarCode.STATUS_MATCH);
            barCodeDao.update(barCode);
        }else{
            barCode = new BarCode();
            barCode.setCode(data);
            barCode.setStatus(BarCode.STATUS_SCAN);
            barCodeDao.insert(barCode);
        }
    }


    @OnTouch(R.id.btn_scan_data)
    public boolean onTouchScan(View v, MotionEvent event){
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:{
//                scanDecode.stopScan();//停止扫描
//                onDataScaned("barcode000988");
                break;
            }
            case MotionEvent.ACTION_DOWN:{
                scanDecode.starScan();//启动扫描
                break;
            }
            default:
                break;
        }
        return true;
    }

    @Override
    public int getLayoutViewID() {
        return R.layout.activity_test;
    }

    @Override
    public void dagger2Inject(ActivityComponent component) {
        component.inject(this);
    }
}
