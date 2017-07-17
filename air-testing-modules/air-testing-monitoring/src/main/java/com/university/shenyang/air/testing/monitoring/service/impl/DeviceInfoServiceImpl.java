package com.university.shenyang.air.testing.monitoring.service.impl;

import com.university.shenyang.air.testing.mapper.DeviceInfoMapper;
import com.university.shenyang.air.testing.model.DeviceInfo;
import com.university.shenyang.air.testing.monitoring.service.DeviceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by cjcqqqq on 2017/5/16.
 */
@Service("deviceInfoService")
public class DeviceInfoServiceImpl implements DeviceInfoService {
    @Autowired
    private DeviceInfoMapper deviceInfoMapper;

    @Override
    public List<DeviceInfo> queryAll() {
        return deviceInfoMapper.selectAll();
    }
}
