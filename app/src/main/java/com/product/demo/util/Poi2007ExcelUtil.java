package com.product.demo.util;

import android.os.Environment;

import com.product.demo.exception.BusinessException;
import com.product.demo.greendao.entity.BarCode;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangsheng on 2018/9/5.
 */

public class Poi2007ExcelUtil {

    public boolean checkUser(String account, String password){
        FileInputStream fis = null;
        try {
            String fileName = Environment.getExternalStorageDirectory() + "/user.xlsx";
//            String fileName = "/mnt/user/0/primary/user.xlsx";
            fis = new FileInputStream(new File(fileName));
            //通过构造函数传参
            XSSFWorkbook workbook = null;
            workbook = new XSSFWorkbook(fis);
            //获取工作表
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowNum = sheet.getLastRowNum();
            for(int i = 0; i <= rowNum; i++){
                //获取行,行号作为参数传递给getRow方法,第一行从0开始计算
                XSSFRow row = sheet.getRow(i);
                //获取单元格,row已经确定了行号,列号作为参数传递给getCell,第一列从0开始计算
                XSSFCell cell = row.getCell(0);
                //设置单元格的值,即C1的值(第一行,第三列)
                cell.setCellType(Cell.CELL_TYPE_STRING);
                String rowAccount = cell.getStringCellValue();
                if(account.equals(rowAccount)){
                    cell = row.getCell(1);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if(password.equals(cell.getStringCellValue())){
                        return true;
                    }
                }else{
                    continue;
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

    public List<BarCode> importData() {
        FileInputStream fis = null;
        List<BarCode> barCodeList = new ArrayList<BarCode>();
        try {
            String fileName = Environment.getExternalStorageDirectory() + "/import.xlsx";
            fis = new FileInputStream(new File(fileName));
            //通过构造函数传参
            XSSFWorkbook workbook = null;
            workbook = new XSSFWorkbook(fis);
            //获取工作表
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowNum = sheet.getLastRowNum();
            for(int i = 0; i <= rowNum; i++){
                //获取行,行号作为参数传递给getRow方法,第一行从0开始计算
                XSSFRow row = sheet.getRow(i);
                //获取单元格,row已经确定了行号,列号作为参数传递给getCell,第一列从0开始计算
                XSSFCell cell = row.getCell(0);
                //设置单元格的值,即C1的值(第一行,第三列)
                cell.setCellType(Cell.CELL_TYPE_STRING);
                BarCode barCode = new BarCode();
                barCode.setCode(cell.getStringCellValue());
                cell = row.getCell(1);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                barCode.setDesc(cell.getStringCellValue());
                barCode.setStatus(BarCode.STATUS_IMPORT);
                barCodeList.add(barCode);
            }
            return barCodeList;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new BusinessException("数据文件文件不存在！");
        }catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("数据文件不存在！");
        }catch(Exception e){
            e.printStackTrace();
            throw new BusinessException("数据导入错误！");
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
