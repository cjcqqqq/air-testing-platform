package com.university.shenyang.air.testing.model;

import java.io.Serializable;
import java.util.Date;

public class CommandSendLog implements Serializable {
    private Long logId;

    private String deviceCode;

    private Date sendtime;

    private Integer commandId;

    private String commandContent;

    private Integer commandStatus;

    private Date updatetime;

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode == null ? null : deviceCode.trim();
    }

    public Date getSendtime() {
        return sendtime;
    }

    public void setSendtime(Date sendtime) {
        this.sendtime = sendtime;
    }

    public Integer getCommandId() {
        return commandId;
    }

    public void setCommandId(Integer commandId) {
        this.commandId = commandId;
    }

    public String getCommandContent() {
        return commandContent;
    }

    public void setCommandContent(String commandContent) {
        this.commandContent = commandContent == null ? null : commandContent.trim();
    }

    public Integer getCommandStatus() {
        return commandStatus;
    }

    public void setCommandStatus(Integer commandStatus) {
        this.commandStatus = commandStatus;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}