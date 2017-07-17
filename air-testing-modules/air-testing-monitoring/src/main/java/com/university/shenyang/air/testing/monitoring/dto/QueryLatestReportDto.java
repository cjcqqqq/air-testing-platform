package com.university.shenyang.air.testing.monitoring.dto;

import com.university.shenyang.air.testing.model.ReportInfo;

/**
 * Created by cjcqqqq on 2017/5/17.
 */
public class QueryLatestReportDto extends BaseDto{
    private ReportInfo data;

    public ReportInfo getData() {
        return data;
    }

    public void setData(ReportInfo data) {
        this.data = data;
    }
}
