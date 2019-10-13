package com.university.shenyang.air.testing.app.command;

import org.hibernate.validator.constraints.NotEmpty;


public class CheckQRcodeCommand extends BaseCommand {
    @NotEmpty(message = "QRcode cannot be empty")
    private String QRcode;

    public String getQRcode() {
        return QRcode;
    }

    public void setQRcode(String QRcode) {
        this.QRcode = QRcode;
    }

}
