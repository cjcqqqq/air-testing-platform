package com.university.shenyang.air.testing.app.command;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class UserAddDeviceCommand extends BaseCommand {
    @NotEmpty(message = "QRcode cannot be empty")
    private String QRcode;
    @NotEmpty(message = "deviceDesc cannot be empty")
    private String deviceDesc;

    public String getQRcode() {
        return QRcode;
    }

    public void setQRcode(String QRcode) {
        this.QRcode = QRcode;
    }

    public String getDeviceDesc() {
        return deviceDesc;
    }

    public void setDeviceDesc(String deviceDesc) {
        this.deviceDesc = deviceDesc;
    }
}
