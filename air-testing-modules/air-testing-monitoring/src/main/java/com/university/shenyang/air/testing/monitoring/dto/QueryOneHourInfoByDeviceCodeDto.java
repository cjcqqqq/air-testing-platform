package com.university.shenyang.air.testing.monitoring.dto;

import com.university.shenyang.air.testing.model.HourInfo;


import java.util.List;

public class QueryOneHourInfoByDeviceCodeDto extends BaseDto{
    private List<HourInfo> data;

    public List<HourInfo> getData() {
        return data;
    }

    public void setData(List<HourInfo> data) {
        this.data = data;
    }
}
