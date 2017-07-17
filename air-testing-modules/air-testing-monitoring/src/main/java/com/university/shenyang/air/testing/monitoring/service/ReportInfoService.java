package com.university.shenyang.air.testing.monitoring.service;

import com.university.shenyang.air.testing.model.ReportInfo;

import java.util.List;

/**
 * Created by cjcqqqq on 2017/5/15.
 */
public interface ReportInfoService {
    int batchInsert(List<ReportInfo> records);
    ReportInfo queryLatestReportByDeviceCode(String deviceCode);
}
