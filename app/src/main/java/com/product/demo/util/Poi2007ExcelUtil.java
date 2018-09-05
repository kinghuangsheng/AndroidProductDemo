package com.product.demo.util;

import android.os.Environment;

import com.product.demo.exception.BusinessException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by huangsheng on 2018/9/5.
 */

public class Poi2007ExcelUtil {

    public boolean checkUser(String account, String password){
        FileInputStream fis = null;
        try {
            String fileName = Environment.getExternalStorageDirectory() + "/user.xlsx";
            fis = new FileInputStream(new File(fileName));
            //通过构造函数传参
            XSSFWorkbook workbook = null;
            workbook = new XSSFWorkbook(fis);
            //获取工作表
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowNum = sheet.getLastRowNum();
            for(int i = 0; i < rowNum; i++){
                //获取行,行号作为参数传递给getRow方法,第一行从0开始计算
                XSSFRow row = sheet.getRow(i);
                //获取单元格,row已经确定了行号,列号作为参数传递给getCell,第一列从0开始计算
                XSSFCell cell = row.getCell(0);
                //设置单元格的值,即C1的值(第一行,第三列)
                row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                String rowAccount = cell.getStringCellValue();
                if(account.equals(rowAccount)){
                    row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
                    if(password.equals(row.getCell(1).toString())){
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

}
