package com.product.demo.greendao.upgrade;


import org.greenrobot.greendao.database.Database;

/**
 * Created by huangsheng1 on 2016/12/6.
 */

public abstract class AbsUpgradeOperation {

    public abstract void onUpgrade(Database db) throws Exception;
}
