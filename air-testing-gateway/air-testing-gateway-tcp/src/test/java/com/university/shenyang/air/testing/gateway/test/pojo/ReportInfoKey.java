package com.university.shenyang.air.testing.gateway.test.pojo;

import java.io.Serializable;
import java.util.Date;

public class ReportInfoKey implements Serializable {
    private String deviceCode;

    private String collectTime;

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode == null ? null : deviceCode.trim();
    }

    public String getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(String collectTime) {
        this.collectTime = collectTime;
    }
}