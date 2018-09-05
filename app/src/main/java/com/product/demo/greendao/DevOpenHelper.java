package com.product.demo.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.product.demo.greendao.upgrade.AbsUpgradeOperation;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;

import base.util.LogUtil;

/**
 * Created by huangsheng on 2018/9/5.
 */

public class DevOpenHelper extends DatabaseOpenHelper {


    public DevOpenHelper(Context context, String name) {
        super(context, name, DaoMaster.SCHEMA_VERSION);
    }

    public DevOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory, DaoMaster.SCHEMA_VERSION);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        for (int i = oldVersion; i < newVersion; i++) {
            try {
                AbsUpgradeOperation absUpgradeOperation = (AbsUpgradeOperation) Class.forName("com.sn.db.upgrade.UpgradeFrom" + i + "To"+ (i + 1)).newInstance();
                if (absUpgradeOperation != null) {
                    LogUtil.info(this, "数据库升级 from " + i + " to " + (i + 1));
                    absUpgradeOperation.onUpgrade(db);
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.info(this, "数据库升级失败 from " + i + " to " + (i + 1));
                break;
            }
        }
    }
    @Override
    public void onCreate(Database db) {
        Log.i("greenDAO", "Creating tables for schema version " + DaoMaster.SCHEMA_VERSION);
        DaoMaster.createAllTables(db, false);
    }
}
