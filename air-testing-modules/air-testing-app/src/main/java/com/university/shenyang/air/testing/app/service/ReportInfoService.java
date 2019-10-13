package com.university.shenyang.air.testing.app.service;

import com.university.shenyang.air.testing.model.ReportInfo;
import com.university.shenyang.air.testing.app.command.QueryReportByDeviceCodeAndTimeCommand;
import com.university.shenyang.air.testing.app.pojo.ReportInfoSim;
import com.university.shenyang.air.testing.app.pojo.ReportTypeInfo;


import java.util.List;

/**
 * Created by cjcqqqq on 2017/5/15.
 */
public interface ReportInfoService {
    int batchInsert(List<ReportInfo> records);


    ReportInfo queryLatestReportByDeviceCode(String deviceCode);

    List<ReportInfo> queryAllDeviceLatestReport();

    List<ReportInfo> queryAllHourDeviceLatestReport();

    List<ReportInfoSim> queryAllDeviceLatestSim();

    List<ReportTypeInfo> queryAllDeviceLatestInfoByType(String type);

    List<ReportInfo> queryAllReportByDeviceCode(String deviceCode);

    List<ReportInfo> queryReportByDeviceCodeAndTime(QueryReportByDeviceCodeAndTimeCommand command);

}
