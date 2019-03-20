package com.university.shenyang.air.testing.collector.service;

import com.university.shenyang.air.testing.model.DeviceInfo;
import com.university.shenyang.air.testing.pojo.DeviceCodeMappingUsername;

import java.util.List;

/**
 * Created by cjcqqqq on 2017/5/15.
 */
public interface DeviceInfoService {
    List<DeviceInfo> queryAll();

    int insertDeviceInfo(DeviceInfo record);
}
