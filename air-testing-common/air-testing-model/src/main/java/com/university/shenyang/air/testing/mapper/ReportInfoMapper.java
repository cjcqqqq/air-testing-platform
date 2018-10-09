package com.university.shenyang.air.testing.mapper;

import com.university.shenyang.air.testing.model.ReportInfo;
import com.university.shenyang.air.testing.model.ReportInfoKey;
import com.university.shenyang.air.testing.pojo.ReportQueryParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReportInfoMapper {
    int deleteByPrimaryKey(ReportInfoKey key);

    int batchInsert(List<ReportInfo> records);

    int insert(ReportInfo record);

    int insertSelective(ReportInfo record);

    ReportInfo selectByPrimaryKey(ReportInfoKey key);

    int updateByPrimaryKeySelective(ReportInfo record);

    int updateByPrimaryKey(ReportInfo record);

    ReportInfo selectLatestByDeviceCode(String deviceCode);

    List<ReportInfo> selectByDeviceCodeAndTime(ReportQueryParam reportQueryParam);
}