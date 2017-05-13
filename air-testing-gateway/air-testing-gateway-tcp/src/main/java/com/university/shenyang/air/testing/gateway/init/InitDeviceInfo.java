package com.university.shenyang.air.testing.gateway.init;

import com.university.shenyang.air.testing.gateway.cache.DevicesManager;
import com.university.shenyang.air.testing.gateway.util.Constants;
import com.university.shenyang.air.testing.model.DeviceInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 设备信息初始化
 */
@Component
public class InitDeviceInfo {
    // 获取设备id列表
    @Value("${air.testing.deviceinfo.ids}")
    private String ids;

    // 获取设备标识码列表
    @Value("${air.testing.deviceinfo.codes}")
    private String codes;

    // 获取设备标识码列表
    @Value("${air.testing.deviceinfo.longitudes}")
    private String longitudes;

    // 获取设备标识码列表
    @Value("${air.testing.deviceinfo.latitudes}")
    private String latitudes;

    // 获取设备标识码列表
    @Value("${air.testing.deviceinfo.sims}")
    private String sims;

    // 获取设备标识码列表
    @Value("${air.testing.deviceinfo.protocols}")
    private String protocols;

    @PostConstruct
    public void initDate() {
        // 注意设备ID的个数应于code个数一致
        // 设备ID列表以逗号分隔，必须是数字，并且不能超出long范围

        String[] idArray = ids.split(",");
        String[] codeArray = codes.split(",");
        String[] longitudeArray = longitudes.split(",");
        String[] latitudeArray = latitudes.split(",");
        String[] simArray = sims.split(",");
        String[] protocolArray = protocols.split(",");
        for(int i = 0; i < idArray.length; i++){
            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.setId(Long.parseLong(idArray[i]));
            if(codeArray != null && codeArray.length > i){
                deviceInfo.setDeviceCode(codeArray[i]);
            }
            if(longitudeArray != null && longitudeArray.length > i){
                deviceInfo.setLongitude(Long.parseLong(longitudeArray[i]));
            }
            if(latitudeArray != null && latitudeArray.length > i){
                deviceInfo.setLatitude(Long.parseLong(latitudeArray[i]));
            }
            if(simArray != null && simArray.length > i){
                deviceInfo.setSim(simArray[i]);
            }
            if(protocolArray != null && protocolArray.length > i){
                deviceInfo.setProtocol(protocolArray[i]);
            }else
            {
                deviceInfo.setProtocol(Constants.PROTOCOL_GB);
            }

            DevicesManager.getInstance().addIdMappingDevice(Long.parseLong(idArray[i]), deviceInfo);
            if(codeArray != null && codeArray.length > i){
                DevicesManager.getInstance().addCodeMappingDevice(codeArray[i], deviceInfo);
            }
        }

    }
}
