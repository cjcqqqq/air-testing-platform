package com.university.shenyang.air.testing.mapper;

import com.university.shenyang.air.testing.model.DeviceInfo;
import com.university.shenyang.air.testing.pojo.DeviceCodeMappingUsername;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

    @Select("select d.DEVICE_CODE as deviceCode,u.username as userName from device_info d LEFT JOIN user_info u on d.USER_ID = u.id")
    List<DeviceCodeMappingUsername> getDeviceCodeMappingUsername();

    @Select("SELECT d.ID AS id, d.DEVICE_CODE AS deviceCode, d.DEVICE_DESC AS deviceDesc, d.ADDRESS AS address, d.LONGITUDE AS longitude, d.LATITUDE AS latitude, d.SIM AS sim, d.PROTOCOL AS protocol, d.COLLECT_INTERVAL AS collectInterval, d.CREATETIME AS createtime, d.UPDATETIME AS updatetime, d.USER_ID AS userId FROM device_info d, user_info u WHERE d.USER_ID = u.id AND u.username = #{username} AND d.UPDATETIME > date_sub( now(), INTERVAL #{minutes} MINUTE )")
    List<DeviceInfo> selectByUsernameAndTime(@Param("username")String username, @Param("minutes")int minutes);
}