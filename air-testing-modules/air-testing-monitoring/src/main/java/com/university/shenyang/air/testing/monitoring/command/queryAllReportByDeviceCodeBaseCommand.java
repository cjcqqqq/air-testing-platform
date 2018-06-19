package com.university.shenyang.air.testing.monitoring.command;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by pbw on 2018/5/25.
 */
public class queryAllReportByDeviceCodeBaseCommand {
    @NotEmpty(message = "deviceCode cannot be empty")
    private String deviceCode;

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }
}
