package com.university.shenyang.air.testing.app.command;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by cjcqqqq on 2017/5/17.
 */
public class WifiSettingCommand extends BaseCommand {
    @NotEmpty(message = "deviceId cannot be empty")
    private String deviceId;
    @NotEmpty(message = "account cannot be empty")
    private String account;
    @NotEmpty(message = "password cannot be empty")
    private String password;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
