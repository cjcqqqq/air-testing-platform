package com.university.shenyang.air.testing.app.response;

import com.university.shenyang.air.testing.model.DayInfo;


import java.util.List;

public class QueryOneDayInfoByDeviceCodeDto extends BaseDto{
    private List<DayInfo> data;

    public List<DayInfo> getData() {
        return data;
    }

    public void setData(List<DayInfo> data) {
        this.data = data;
    }
}
