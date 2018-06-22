package com.university.shenyang.air.testing.mapper;

import com.university.shenyang.air.testing.model.HourInfo;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HourInfoMapper {
    int batchInsert(List<HourInfo> records);

    int insert(HourInfo record);

    List<HourInfo> selectLatestByDeviceCode(String deviceCode);
}
