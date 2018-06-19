package com.university.shenyang.air.testing.monitoring.command;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;

/**
 * Created by pbw on 2018/5/25.
 */
public class QueryReportByDeviceCodeAndTimeCommand {
    private String deviceCode;

    @NotEmpty(message = "startTime cannot be empty")
    private String startTime;

    @NotEmpty(message = "endTime cannot be empty")
    private String endTime;

    @Min(value = 1, message = "pageNum must >= 1")
    private int pageNum;

    @Min(value = 1, message = "pageSize must >= 1")
    private int pageSize;

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
