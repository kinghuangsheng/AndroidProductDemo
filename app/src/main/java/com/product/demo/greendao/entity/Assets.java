package com.product.demo.greendao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * Created by huangsheng on 2018/9/6.
 */

@Entity
public class Assets implements Serializable{

    public final static int STATUS_IMPORT = 1;
    public final static int STATUS_MATCH = 2;
    public final static int STATUS_SCAN = 3;

    private static final long serialVersionUID = -3928832861296252415L;

    @Id(autoincrement = true)
    private Long id;
    //资产名称
    private String name;
    //资产编码
    private String code;
    //资产条码
    private String barCode;
    //资产序列号
    private String serialNumber;
    //资产规格型号
    private String specifications;
    //资产计量单位
    private String unit;
    //资产供应商
    private String supplier;
    //资产生产厂家
    private String produceCompany;
    //资产维保单位
    private String maintainCompany;
    //资产维保开始日期
    private String maintainStartDate;
    //资产维保结束日期
    private String maintainEndDate;
    //资产领用日期
    private String useDate;
    //资产使用部门
    private String department;
    //资产位置
    private String position;
    //资产用户ID
    private String userId;
    //资产用户
    private String userName;
    //主资产编码
    private String mainAssetsCode;
    //资状态
    private String state;

    private int status;


    @Generated(hash = 1601811016)
    public Assets(Long id, String name, String code, String barCode,
            String serialNumber, String specifications, String unit,
            String supplier, String produceCompany, String maintainCompany,
            String maintainStartDate, String maintainEndDate, String useDate,
            String department, String position, String userId, String userName,
            String mainAssetsCode, String state, int status) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.barCode = barCode;
        this.serialNumber = serialNumber;
        this.specifications = specifications;
        this.unit = unit;
        this.supplier = supplier;
        this.produceCompany = produceCompany;
        this.maintainCompany = maintainCompany;
        this.maintainStartDate = maintainStartDate;
        this.maintainEndDate = maintainEndDate;
        this.useDate = useDate;
        this.department = department;
        this.position = position;
        this.userId = userId;
        this.userName = userName;
        this.mainAssetsCode = mainAssetsCode;
        this.state = state;
        this.status = status;
    }

    @Generated(hash = 1373698660)
    public Assets() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getProduceCompany() {
        return produceCompany;
    }

    public void setProduceCompany(String produceCompany) {
        this.produceCompany = produceCompany;
    }

    public String getMaintainCompany() {
        return maintainCompany;
    }

    public void setMaintainCompany(String maintainCompany) {
        this.maintainCompany = maintainCompany;
    }

    public String getMaintainStartDate() {
        return maintainStartDate;
    }

    public void setMaintainStartDate(String maintainStartDate) {
        this.maintainStartDate = maintainStartDate;
    }

    public String getMaintainEndDate() {
        return maintainEndDate;
    }

    public void setMaintainEndDate(String maintainEndDate) {
        this.maintainEndDate = maintainEndDate;
    }

    public String getUseDate() {
        return useDate;
    }

    public void setUseDate(String useDate) {
        this.useDate = useDate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMainAssetsCode() {
        return mainAssetsCode;
    }

    public void setMainAssetsCode(String mainAssetsCode) {
        this.mainAssetsCode = mainAssetsCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean inventorySuccess() {
        return status == STATUS_MATCH;
    }
}
