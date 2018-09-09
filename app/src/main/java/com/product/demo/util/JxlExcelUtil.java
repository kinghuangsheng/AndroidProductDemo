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
import java.util.Arrays;
import java.util.List;

import jxl.CellView;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

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

    public static boolean updatePassword(String account, String password, String newPassword){
        FileInputStream fis = null;
        try {
            String fileName = Environment.getExternalStorageDirectory() + "/user.xls";
            fis = new FileInputStream(new File(fileName));
            //通过构造函数传参
            Workbook book = Workbook.getWorkbook(fis);
            Sheet sheet =  book.getSheet(0);
            for(int i = 0; i < sheet.getRows(); i++){
                if(account.equals(sheet.getCell(0, i).getContents()) && password.equals(sheet.getCell(1, i).getContents())){
                    File file = new File(Environment.getExternalStorageDirectory() + "/user.xls");
                    WritableWorkbook bookTmp = Workbook.createWorkbook(file, book);
                    WritableSheet writableSheet = bookTmp.getSheet(0);
                    writableSheet.addCell(new Label(1, i, newPassword));
                    bookTmp.write();
                    bookTmp.close();
                    return true;
                }
            }
            throw new BusinessException("原密码错误！");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new BusinessException("用户配置文件不存在！");
        }catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("用户配置文件不存在！");
        }catch(Exception e){
            e.printStackTrace();
            throw new BusinessException("原密码错误！");
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

    public void export(List<Assets> assetsList, String fileName) throws IOException, WriteException {
        WritableWorkbook book = null;
        String[] title = new String[]{"资产名称", "资产编码", "资产条码", "资产序列号", "规格型号", "计量单位",
                "供应商", "生产厂家", "维保单位", "维保开始日期", "维保结束日期", "领用日期",
                "使用部门", "位置", "用户ID", "用户", "主资产编码", "资产状态"};
        int[] lengths = new int[18];
        Arrays.fill(lengths, 30);
        try{
            File file = new File(Environment.getExternalStorageDirectory() + "/" + fileName);
            if(file.exists()){
                file.delete();
            }
            book = Workbook.createWorkbook(file);
            //生成名为eccif的工作表，参数0表示第一页
            WritableSheet sheet = book.createSheet("sheet", 0);
            //表头导航
            for (int j = 0; j < 18; j++) {
                Label label = new Label(j, 0, title[j]);
                sheet.addCell(label);
                sheet.setColumnView(j, 24); //设置col显示样式
            }
            for (int i = 0; i < assetsList.size(); i++) {
                int rowIndex = i + 1;
                Assets assets = assetsList.get(i);
                sheet.addCell(new Label(0, rowIndex, assets.getName()));
                sheet.addCell(new Label(1, rowIndex, assets.getCode()));
                sheet.addCell(new Label(2, rowIndex, assets.getBarCode()));
                sheet.addCell(new Label(3, rowIndex, assets.getSerialNumber()));
                sheet.addCell(new Label(4, rowIndex, assets.getSpecifications()));
                sheet.addCell(new Label(5, rowIndex, assets.getUnit()));
                sheet.addCell(new Label(6, rowIndex, assets.getSupplier()));
                sheet.addCell(new Label(7, rowIndex, assets.getProduceCompany()));
                sheet.addCell(new Label(8, rowIndex, assets.getMaintainCompany()));
                sheet.addCell(new Label(9, rowIndex, assets.getMaintainStartDate()));
                sheet.addCell(new Label(10, rowIndex, assets.getMaintainEndDate()));
                sheet.addCell(new Label(11, rowIndex, assets.getUseDate()));
                sheet.addCell(new Label(12, rowIndex, assets.getDepartment()));
                sheet.addCell(new Label(13, rowIndex, assets.getPosition()));
                sheet.addCell(new Label(14, rowIndex, assets.getUserId()));
                sheet.addCell(new Label(15, rowIndex, assets.getUserName()));
                sheet.addCell(new Label(16, rowIndex, assets.getMainAssetsCode()));
                sheet.addCell(new Label(17, rowIndex, assets.getState()));
            }
            // 写入数据并关闭文件
            book.write();
        }finally{
            if(book!=null){
                try {
                    book.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
