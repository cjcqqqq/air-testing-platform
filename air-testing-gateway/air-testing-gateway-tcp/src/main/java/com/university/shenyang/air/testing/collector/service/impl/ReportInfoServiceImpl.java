package com.university.shenyang.air.testing.collector.service.impl;

import com.university.shenyang.air.testing.collector.service.ReportInfoService;
import com.university.shenyang.air.testing.mapper.ReportInfoMapper;
import com.university.shenyang.air.testing.model.ReportInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by cjcqqqq on 2017/5/16.
 */
@Service("reportInfoService")
public class ReportInfoServiceImpl implements ReportInfoService {
    @Autowired
    private ReportInfoMapper reportInfoMapper;

    @Override
    public int batchInsert(List<ReportInfo> records) {
        return reportInfoMapper.batchInsert(records);
    }

    @Override
    public ReportInfo queryLatestReportByDeviceCode(String deviceCode) {
        return reportInfoMapper.selectLatestByDeviceCode(deviceCode);
    }
}
