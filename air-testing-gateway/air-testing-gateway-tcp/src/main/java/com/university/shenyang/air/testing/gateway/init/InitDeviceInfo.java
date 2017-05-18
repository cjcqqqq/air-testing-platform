package com.university.shenyang.air.testing.gateway.init;

import com.university.shenyang.air.testing.gateway.cache.DevicesManager;
import com.university.shenyang.air.testing.model.DeviceInfo;
import com.university.shenyang.air.testing.service.DeviceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 设备信息初始化
 */
@Component
public class InitDeviceInfo {
    @Autowired
    DeviceInfoService deviceInfoService;

    @PostConstruct
    public void initDate() {
        List<DeviceInfo> deviceInfoList = deviceInfoService.queryAll();
        for(DeviceInfo deviceInfo : deviceInfoList){
            DevicesManager.getInstance().addIdMappingDevice(deviceInfo.getId(), deviceInfo);
            DevicesManager.getInstance().addCodeMappingDevice(deviceInfo.getDeviceCode(), deviceInfo);
        }
    }
}
