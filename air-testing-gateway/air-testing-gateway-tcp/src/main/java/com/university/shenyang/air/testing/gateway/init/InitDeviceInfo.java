package com.university.shenyang.air.testing.gateway.init;

import com.university.shenyang.air.testing.gateway.cache.DevicesManager;
import com.university.shenyang.air.testing.gateway.service.DeviceInfoService;
import com.university.shenyang.air.testing.model.DeviceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 设备信息初始化
 */
@Component
public class InitDeviceInfo implements CommandLineRunner {
    @Autowired
    DeviceInfoService deviceInfoService;

    @Override
    public void run(String... args) throws Exception {
        List<DeviceInfo> deviceInfoList = deviceInfoService.queryAll();
        for(DeviceInfo deviceInfo : deviceInfoList){
            DevicesManager.getInstance().addIdMappingDevice(deviceInfo.getId(), deviceInfo);
            DevicesManager.getInstance().addCodeMappingDevice(deviceInfo.getDeviceCode(), deviceInfo);
        }
    }
}
