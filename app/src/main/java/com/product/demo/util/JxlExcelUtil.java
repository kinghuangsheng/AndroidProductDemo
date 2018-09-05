package com.product.demo.util;

import android.os.Environment;

import com.product.demo.exception.BusinessException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import jxl.Sheet;
import jxl.Workbook;

/**
 * Created by huangsheng on 2018/9/5.
 */

public class JxlExcelUtil {

    public boolean checkUser(String account, String password){
        FileInputStream fis = null;
        try {
            String fileName = Environment.getExternalStorageDirectory() + "/user.xlsx";
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

}
