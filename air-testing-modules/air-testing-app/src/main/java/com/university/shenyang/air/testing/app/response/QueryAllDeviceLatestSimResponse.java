package com.university.shenyang.air.testing.app.response;


import com.university.shenyang.air.testing.app.pojo.ReportInfoSim;
import com.university.shenyang.air.testing.common.response.BaseResponse;

import java.util.List;

/**
 * Created by cjcqqqq on 2017/5/17.
 */
public class QueryAllDeviceLatestSimResponse extends AppBaseResponse {
    private List<ReportInfoSim> data;

    public List<ReportInfoSim> getData() {
        return data;
    }

    public void setData(List<ReportInfoSim> data) {
        this.data = data;
    }
}
