package com.university.shenyang.air.testing.gateway.pojo;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * Created by cjcqqqq on 2017/5/20.
 */
public class DeviceParam implements Serializable {
    private String wifiAccount;
    private String wifiPassword;
    private int interval;

    public String getWifiAccount() {
        return wifiAccount;
    }

    public void setWifiAccount(String wifiAccount) {
        this.wifiAccount = wifiAccount;
    }

    public String getWifiPassword() {
        return wifiPassword;
    }

    public void setWifiPassword(String wifiPassword) {
        this.wifiPassword = wifiPassword;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
