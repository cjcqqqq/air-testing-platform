package com.university.shenyang.air.testing.app.response;


import com.university.shenyang.air.testing.app.pojo.UserDeviceInfo;

import java.util.List;

public class GetUserDeviceListResponse extends AppBaseResponse {
    private List<UserDeviceInfo> data;

    public List<UserDeviceInfo> getData() {
        return data;
    }

    public void setData(List<UserDeviceInfo> data) {
        this.data = data;
    }
}
