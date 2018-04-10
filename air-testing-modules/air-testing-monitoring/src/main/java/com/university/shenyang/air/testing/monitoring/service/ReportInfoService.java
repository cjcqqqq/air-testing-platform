package com.university.shenyang.air.testing.monitoring.service;

import com.university.shenyang.air.testing.model.ReportInfo;
import com.university.shenyang.air.testing.monitoring.pojo.ReportInfoSim;
import com.university.shenyang.air.testing.monitoring.pojo.ReportTypeInfo;


import java.util.List;

/**
 * Created by cjcqqqq on 2017/5/15.
 */
public interface ReportInfoService {
    int batchInsert(List<ReportInfo> records);


    ReportInfo queryLatestReportByDeviceCode(String deviceCode);

    List<ReportInfo> queryAllDeviceLatestReport();

    List<ReportInfoSim> queryAllDeviceLatestSim();

    List<ReportTypeInfo> queryAllDeviceLatestInfoByType(String type);

}
