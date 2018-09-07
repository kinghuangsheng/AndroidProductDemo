package com.product.demo.greendao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by huangsheng on 2018/9/6.
 */

@Entity
public class BarCode {

    public final static int STATUS_IMPORT = 1;
    public final static int STATUS_MATCH = 2;
    public final static int STATUS_SCAN= 3;

    @Id(autoincrement = true)
    private Long id;
    private String code;
    private String desc;
    private int status;

    @Generated(hash = 303441476)
    public BarCode() {
    }

    @Generated(hash = 532231753)
    public BarCode(Long id, String code, String desc, int status) {
        this.id = id;
        this.code = code;
        this.desc = desc;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
