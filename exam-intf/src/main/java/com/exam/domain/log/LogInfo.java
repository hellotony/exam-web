package com.exam.domain.log;

import java.util.Date;

/**
 * Created by huajianbing on 2015/12/3.
 */
public class LogInfo {
    private Long id;
    private Integer moudleId =1;
    private Integer actionId =1;
    private Integer systemType =5;
    private Integer logLevel=1;
    private Integer logType =1;
    private Integer orderId =0;
    private long visaOrderId=0;
    private Integer touristId=0;
    private Integer resId=0;
    private Integer productId=0;
    private String logContent="";
    private Integer logBy=0;
    private String logName="";
    private Date logTime=new Date();
    private Integer delFlag=0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMoudleId() {
        return moudleId;
    }

    public void setMoudleId(Integer moudleId) {
        this.moudleId = moudleId;
    }

    public Integer getActionId() {
        return actionId;
    }

    public void setActionId(Integer actionId) {
        this.actionId = actionId;
    }

    public Integer getSystemType() {
        return systemType;
    }

    public void setSystemType(Integer systemType) {
        this.systemType = systemType;
    }

    public Integer getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(Integer logLevel) {
        this.logLevel = logLevel;
    }

    public Integer getLogType() {
        return logType;
    }

    public void setLogType(Integer logType) {
        this.logType = logType;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public long getVisaOrderId() {
        return visaOrderId;
    }

    public void setVisaOrderId(long visaOrderId) {
        this.visaOrderId = visaOrderId;
    }

    public Integer getTouristId() {
        return touristId;
    }

    public void setTouristId(Integer touristId) {
        this.touristId = touristId;
    }

    public Integer getResId() {
        return resId;
    }

    public void setResId(Integer resId) {
        this.resId = resId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getLogContent() {
        return logContent;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }

    public Integer getLogBy() {
        return logBy;
    }

    public void setLogBy(Integer logBy) {
        this.logBy = logBy;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}
