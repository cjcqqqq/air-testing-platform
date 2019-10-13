package com.university.shenyang.air.testing.app.response;


import com.university.shenyang.air.testing.app.pojo.ReportTypeInfo;

import java.util.List;

/**
 * Created by cjcqqqq on 2017/5/17.
 */
public class QueryLatestInfoByTypeDto extends BaseDto{
    private List<ReportTypeInfo> data;

    public List<ReportTypeInfo> getData() {
        return data;
    }

    public void setData(List<ReportTypeInfo> data) {
        this.data = data;
    }
}
