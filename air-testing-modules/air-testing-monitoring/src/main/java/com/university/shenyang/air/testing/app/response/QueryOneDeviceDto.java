package com.university.shenyang.air.testing.app.response;

import com.university.shenyang.air.testing.model.DeviceInfo;

/**
 * Created by cjcqqqq on 2017/5/17.
 */
public class QueryOneDeviceDto extends BaseDto{
    private DeviceInfo data;

   public DeviceInfo getData() {
        return data;
    }

    public void setData(DeviceInfo data) {
        this.data = data;
    }
}
