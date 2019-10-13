package com.university.shenyang.air.testing.app.service;

import com.university.shenyang.air.testing.model.DeviceInfo;
import com.university.shenyang.air.testing.app.command.AddDeviceCommand;

import java.util.List;

/**
 * Created by cjcqqqq on 2017/5/15.
 */
public interface DeviceInfoService {
    List<DeviceInfo> queryAll();

    DeviceInfo queryOneDeviceByDeviceCode(String deviceCode);

    int deleteByPrimaryKey(Long id);

    int insertDeviceInfo(AddDeviceCommand record);

    List<DeviceInfo> selectByUsernameAndTime(String username, int minutes);

}
