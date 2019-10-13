package com.university.shenyang.air.testing.app.response;

import com.university.shenyang.air.testing.common.response.BaseResponse;
import com.university.shenyang.air.testing.model.DeviceInfo;

/**
 * Created by cjcqqqq on 2017/5/17.
 */
public class QueryOneDeviceResponse extends AppBaseResponse {
    private DeviceInfo data;

   public DeviceInfo getData() {
        return data;
    }

    public void setData(DeviceInfo data) {
        this.data = data;
    }
}
