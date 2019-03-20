package com.university.shenyang.air.testing.collector.task;

import com.university.shenyang.air.testing.collector.pojo.UpdateDeviceInfoResult;
import com.university.shenyang.air.testing.collector.service.DeviceInfoService;
import com.university.shenyang.air.testing.model.DeviceInfo;
import com.university.shenyang.air.testing.pojo.DeviceCodeMappingUsername;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备信息初始化
 */
@Component
public class SyncDeviceInfoTask {
    @Autowired
    DeviceInfoService deviceInfoService;

    @Value("${sync.deviceinfo.url}")
    private String syncUrl;

    @Scheduled(cron = "${sync.deviceinfo.cron:0 0/5 * * * ?}")
    public void updateDeviceInfo() {
        // 加载设备信息
        List<DeviceInfo> deviceInfoList = deviceInfoService.queryAll();
        List<String> deviceCodeList = new ArrayList<>();
        for(DeviceInfo row : deviceInfoList)
        {
            deviceCodeList.add(row.getDeviceCode());
        }
        RestTemplate restT = new RestTemplate();
        //通过Jackson JSON processing library直接将返回值绑定到对象
        UpdateDeviceInfoResult updateDeviceInfoResult = restT.getForObject(syncUrl, UpdateDeviceInfoResult.class);
        if(updateDeviceInfoResult != null && updateDeviceInfoResult.getData() != null && updateDeviceInfoResult.getData().size() > 0)
        {
            for(DeviceInfo row : updateDeviceInfoResult.getData())
            {
                if(!deviceCodeList.contains(row.getDeviceCode())){
                    deviceInfoService.insertDeviceInfo(row);
                }
            }
        }


    }
}
