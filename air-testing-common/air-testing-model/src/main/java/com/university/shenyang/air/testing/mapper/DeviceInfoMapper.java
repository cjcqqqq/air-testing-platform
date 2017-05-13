package com.university.shenyang.air.testing.mapper;

import com.university.shenyang.air.testing.model.DeviceInfo;

import java.util.List;

/**
 * Created by chenjc on 2017/5/7.
 */
public interface DeviceInfoMapper {
    /**
     * 获取所有设备信息
     *
     * @return
     */
    List<DeviceInfo> selectALL();
}
