package com.university.shenyang.air.testing.app.response;


import com.university.shenyang.air.testing.app.pojo.ReportInfoSim;

import java.util.List;

/**
 * Created by cjcqqqq on 2017/5/17.
 */
public class QueryAllDeviceLatestSimDto extends BaseDto{
    private List<ReportInfoSim> data;

    public List<ReportInfoSim> getData() {
        return data;
    }

    public void setData(List<ReportInfoSim> data) {
        this.data = data;
    }
}
