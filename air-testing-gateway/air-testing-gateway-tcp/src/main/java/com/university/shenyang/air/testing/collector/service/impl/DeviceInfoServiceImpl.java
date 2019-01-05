package com.university.shenyang.air.testing.collector.service.impl;

import com.university.shenyang.air.testing.collector.cache.DevicesManager;
import com.university.shenyang.air.testing.collector.service.DeviceInfoService;
import com.university.shenyang.air.testing.mapper.DeviceInfoMapper;
import com.university.shenyang.air.testing.model.DeviceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    @Override
    public int updateDeviceCollectInterval(String deviceCode, int interval) {
        int updateResult = 0;
        DeviceInfo deviceInfo = deviceInfoMapper.selectByDeviceCode(deviceCode);
        deviceInfo.setCollectInterval(interval);
        deviceInfo.setUpdatetime(new Date());

        updateResult = deviceInfoMapper.updateByPrimaryKey(deviceInfo);
        if(updateResult > 0){
            DevicesManager.getInstance().addIdMappingDevice(deviceInfo.getId(), deviceInfo);
            DevicesManager.getInstance().addCodeMappingDevice(deviceInfo.getDeviceCode(), deviceInfo);
        }

        return updateResult;
    }
}
