package com.university.shenyang.air.testing.collector.dto;

import com.university.shenyang.air.testing.collector.pojo.DeviceParam;

/**
 * Created by cjcqqqq on 2017/5/17.
 */
public class ParamQueryDto extends BaseDto{
    private DeviceParam data;

    public DeviceParam getData() {
        return data;
    }

    public void setData(DeviceParam data) {
        this.data = data;
    }
}
