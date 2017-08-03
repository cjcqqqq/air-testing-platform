package com.university.shenyang.air.testing.mapper;

import com.university.shenyang.air.testing.model.DeviceInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DeviceInfoMapper {
    List<DeviceInfo> selectAll();

    int deleteByPrimaryKey(Long id);

    int insert(DeviceInfo record);

    int insertSelective(DeviceInfo record);

    DeviceInfo selectByPrimaryKey(Long id);

    DeviceInfo selectByDeviceCode(String deviceCode);

    int updateByPrimaryKeySelective(DeviceInfo record);

    int updateByPrimaryKey(DeviceInfo record);
}