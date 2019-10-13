package com.university.shenyang.air.testing.app.service;

import com.university.shenyang.air.testing.app.command.UserAddDeviceCommand;
import com.university.shenyang.air.testing.app.pojo.UserDeviceInfo;
import com.university.shenyang.air.testing.model.CUserDeviceMapping;
import com.university.shenyang.air.testing.model.DeviceInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by chenjc on 2019/6/30.
 */
public interface CUserDeviceMappingService {
    int userAddDevice(String userId, String deviceCode, String deviceDesc);

    List<UserDeviceInfo> getUserDeviceList(String userId);

    int setDefault(String userId, String deviceId);

    int deleteByUserIdAndMappingId(String userId, String mappingId);
}
