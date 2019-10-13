package com.university.shenyang.air.testing.app.command;

import org.hibernate.validator.constraints.NotEmpty;

public class SetDefaultCommand extends BaseCommand {
    @NotEmpty(message = "deviceId cannot be empty")
    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
