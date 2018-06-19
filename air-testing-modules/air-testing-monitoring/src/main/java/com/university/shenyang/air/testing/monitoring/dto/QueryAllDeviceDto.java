package com.university.shenyang.air.testing.monitoring.dto;


import com.university.shenyang.air.testing.model.DeviceInfo;
        import java.util.List;

public class QueryAllDeviceDto extends BaseDto{
    private List<DeviceInfo> data;

    public List<DeviceInfo> getData() {
        return data;
    }

    public void setData(List<DeviceInfo> data) {
        this.data = data;
    }
}
