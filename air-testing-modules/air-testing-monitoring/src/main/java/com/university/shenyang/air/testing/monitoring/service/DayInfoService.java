package com.university.shenyang.air.testing.monitoring.service;

import com.university.shenyang.air.testing.model.DayInfo;

import java.util.List;

/**
 * Created by cjcqqqq on 2017/5/15.
 */
public interface DayInfoService {


    int batchInsert(List<DayInfo> records);
    int insert(DayInfo record);
    List<DayInfo> selectLatestByDeviceCode(String deviceCode);

}
