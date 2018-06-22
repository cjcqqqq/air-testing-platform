package com.university.shenyang.air.testing.mapper;

import com.university.shenyang.air.testing.model.DayInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DayInfoMapper {
    int batchInsert(List<DayInfo> records);

    int insert(DayInfo record);

    List<DayInfo> selectLatestByDeviceCode(String deviceCode);
}
