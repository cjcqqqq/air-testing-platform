package com.university.shenyang.air.testing.monitoring.service.impl;

import com.university.shenyang.air.testing.mapper.DeviceInfoMapper;
import com.university.shenyang.air.testing.model.DeviceInfo;
import com.university.shenyang.air.testing.monitoring.command.AddDeviceCommand;
import com.university.shenyang.air.testing.monitoring.service.DeviceInfoService;
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
    public DeviceInfo queryOneDeviceByDeviceCode(String deviceCode) {
        DeviceInfo result = null;
        if(result == null){
            result = deviceInfoMapper.selectByDeviceCode(deviceCode);
        }
        return result;

    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return  deviceInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertDeviceInfo(AddDeviceCommand addDeviceCommand) {
        DeviceInfo record = new DeviceInfo();
        record.setDeviceCode(addDeviceCommand.getDeviceCode());
        record.setDeviceDesc(addDeviceCommand.getDeviceDesc());
        record.setAddress(addDeviceCommand.getAddress());
        record.setLongitude(addDeviceCommand.getLongitude());
        record.setLatitude(addDeviceCommand.getLatitude());
        record.setSim(addDeviceCommand.getSim());
        record.setProtocol(addDeviceCommand.getProtocol());
        record .setCollectInterval(addDeviceCommand.getCollectInterval());
        record.setCreatetime(new Date());
        record.setUpdatetime(new Date());

        return deviceInfoMapper.insert(record);

    }

    @Override
    public List<DeviceInfo> selectByUsernameAndTime(String username, int minutes) {
        return deviceInfoMapper.selectByUsernameAndTime(username, minutes);
    }


}
