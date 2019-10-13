package com.university.shenyang.air.testing.app.cache;

import com.university.shenyang.air.testing.model.DeviceInfo;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chenjc on 2017/05/03.
 */
public class DevicesManager {
    private static DevicesManager instance = new DevicesManager();

    private DevicesManager() {
    }

    public static DevicesManager getInstance() {
        return instance;
    }

    /**
     * key:设备ID
     * value ：DeviceInfo
     */
    private static Map<Long, DeviceInfo> idMappingDevice = new ConcurrentHashMap<>();

    public void addIdMappingDevice(long id, DeviceInfo deviceInfo) {
        idMappingDevice.put(id, deviceInfo);
    }

    public DeviceInfo getDeviceById(long id) {
        return idMappingDevice.get(id);
    }

    public String getDeviceCodeById(long id) {
        String deviceCode = null;
        DeviceInfo deviceInfo = idMappingDevice.get(id);
        if(deviceInfo != null){
            deviceCode = deviceInfo.getDeviceCode();
        }
        return deviceCode;
    }

    // TODO
    public void removeDeviceById(long id) {
        idMappingDevice.remove(id);
    }

    /**
     * 根据设备识别码查询ID
     *
     * @param deviceCode 设备识别码
     * @return ID，失败返回-1
     */
    public long getIdByDeviceCode(String deviceCode) {
        for (Map.Entry<Long, DeviceInfo> entity : idMappingDevice.entrySet()) {
            DeviceInfo device = entity.getValue();
            if (device != null && device.getDeviceCode().equals(deviceCode)) {
                return device.getId();
            }
        }
        return -1;
    }

}
