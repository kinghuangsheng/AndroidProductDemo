package com.product.demo.util;

import android.os.Environment;

import com.product.demo.exception.BusinessException;
import com.product.demo.greendao.entity.Assets;
import com.product.demo.greendao.entity.BarCode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

/**
 * Created by huangsheng on 2018/9/5.
 */

public class JxlExcelUtil {

    public boolean checkUser(String account, String password){
        FileInputStream fis = null;
        try {
            String fileName = Environment.getExternalStorageDirectory() + "/user.xls";
            fis = new FileInputStream(new File(fileName));
            //通过构造函数传参
            Workbook book = Workbook.getWorkbook(fis);
            Sheet sheet =  book.getSheet(0);
            for(int i = 0; i < sheet.getRows(); i++){
                if(account.equals(sheet.getCell(0, i).getContents()) && password.equals(sheet.getCell(1, i).getContents())){
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new BusinessException("用户配置文件不存在！");
        }catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("用户配置文件不存在！");
        }catch(Exception e){
            e.printStackTrace();
            throw new BusinessException("用户不存在或密码错误！");
        }finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public List<Assets> importData() {
        FileInputStream fis = null;
        try {
            String fileName = Environment.getExternalStorageDirectory() + "/import.xls";
            fis = new FileInputStream(new File(fileName));
            //通过构造函数传参
            Workbook book = Workbook.getWorkbook(fis);
            Sheet sheet =  book.getSheet(0);
            List<Assets> assetsList = new ArrayList<Assets>();
            for(int i = 1; i < sheet.getRows(); i++){
                Assets assets = new Assets();
                assets.setName(sheet.getCell(0, i).getContents());
                assets.setCode(sheet.getCell(1, i).getContents());
                assets.setBarCode(sheet.getCell(2, i).getContents());
                assets.setSerialNumber(sheet.getCell(3, i).getContents());
                assets.setSpecifications(sheet.getCell(4, i).getContents());
                assets.setUnit(sheet.getCell(5, i).getContents());
                assets.setSupplier(sheet.getCell(6, i).getContents());
                assets.setProduceCompany(sheet.getCell(7, i).getContents());
                assets.setMaintainCompany(sheet.getCell(8, i).getContents());
                assets.setMaintainStartDate(sheet.getCell(9, i).getContents());
                assets.setMaintainEndDate(sheet.getCell(10, i).getContents());
                assets.setUseDate(sheet.getCell(11, i).getContents());
                assets.setDepartment(sheet.getCell(12, i).getContents());
                assets.setPosition(sheet.getCell(13, i).getContents());
                assets.setUserId(sheet.getCell(14, i).getContents());
                assets.setUserName(sheet.getCell(15, i).getContents());
                assets.setMainAssetsCode(sheet.getCell(16, i).getContents());
                assets.setState(sheet.getCell(17, i).getContents());
                assets.setStatus(Assets.STATUS_IMPORT);
                assetsList.add(assets);
            }
            return assetsList;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new BusinessException("数据文件不存在！");
        }catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("读取数据文件失败！");
        }catch(Exception e){
            e.printStackTrace();
            throw new BusinessException("解析数据文件失败！");
        }finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
