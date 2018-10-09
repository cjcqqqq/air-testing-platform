package com.university.shenyang.air.testing.model;

import java.io.Serializable;
import java.util.Date;

public class ReportInfoKey implements Serializable {
    private String deviceCode;

    private Date collectTime;

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode == null ? null : deviceCode.trim();
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }
}