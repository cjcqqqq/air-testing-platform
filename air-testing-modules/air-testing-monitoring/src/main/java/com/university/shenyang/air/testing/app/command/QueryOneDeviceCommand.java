package com.university.shenyang.air.testing.app.command;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by cjcqqqq on 2017/5/17.
 */
public class QueryOneDeviceCommand extends BaseCommand {
    @NotEmpty(message = "deviceCode cannot be empty")
    private String deviceCode;

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

}
