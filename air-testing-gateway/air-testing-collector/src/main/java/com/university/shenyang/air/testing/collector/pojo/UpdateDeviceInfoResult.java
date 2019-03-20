package com.university.shenyang.air.testing.collector.pojo;

import com.university.shenyang.air.testing.model.DeviceInfo;

import java.io.Serializable;
import java.util.List;

public class UpdateDeviceInfoResult implements Serializable {
    private int resultCode;
    private String[] msg;
    private List<DeviceInfo> data;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String[] getMsg() {
        return msg;
    }

    public void setMsg(String[] msg) {
        this.msg = msg;
    }

    public List<DeviceInfo> getData() {
        return data;
    }

    public void setData(List<DeviceInfo> data) {
        this.data = data;
    }
}