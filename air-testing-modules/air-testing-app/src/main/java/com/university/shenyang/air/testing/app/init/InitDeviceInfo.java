package com.university.shenyang.air.testing.app.init;

import com.university.shenyang.air.testing.app.cache.DevicesManager;
import com.university.shenyang.air.testing.app.service.DeviceInfoService;
import com.university.shenyang.air.testing.model.DeviceInfo;
import com.university.shenyang.air.testing.pojo.DeviceCodeMappingUsername;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
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
        // 加载设备信息
        List<DeviceInfo> deviceInfoList = deviceInfoService.queryAll();
        for(DeviceInfo deviceInfo : deviceInfoList){
            DevicesManager.getInstance().addIdMappingDevice(deviceInfo.getId(), deviceInfo);
        }
    }
}
