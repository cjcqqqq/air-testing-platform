package com.university.shenyang.air.testing.mapper;

import com.university.shenyang.air.testing.model.CUserDeviceMapping;
import com.university.shenyang.air.testing.model.DeviceInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;
import java.util.Map;

@Mapper
public interface CUserDeviceMappingMapper {
    int deleteByPrimaryKey(Long id);

//    int insert(CUserDeviceMapping record);

    @Insert("INSERT into c_user_device_mapping(USER_ID, DEVICE_ID, DEVICE_DESC, IS_DEFAULT) VALUES(#{userId}, #{deviceId}, #{deviceDesc}, (CASE when (select count(1) from c_user_device_mapping t where t.USER_ID = #{userId}) = 0 THEN 1 ELSE 0 end))")
    int insert(CUserDeviceMapping record);


    /**
     * 调用存储过程，执行用户添加设备
     * @param params={userId=用户ID,deviceCode=设备编码,deviceDesc=设备描述}
     * @return params={o_result=执行结果：0:OK , 1:设备不存在, 2:设备已添加, 3:设备名称已存在}
     */
    @Select({ "call p_userAddDevice(#{userId,mode=IN,jdbcType=TINYINT},"
            + "#{deviceCode,mode=IN,jdbcType=VARCHAR},"
            + "#{deviceDesc,mode=IN,jdbcType=VARCHAR},"
            + "#{result,mode=OUT,jdbcType=TINYINT})" })
    @Options(statementType= StatementType.CALLABLE)
    void userAddDevice(Map<String,Object> params);

    @Select("select ID, USER_ID userId, DEVICE_ID deviceId, DEVICE_DESC deviceDesc, IS_DEFAULT isDefault from c_user_device_mapping where USER_ID = #{userId} ORDER BY ID")
    List<CUserDeviceMapping> getUserDeviceList(@Param("userId")String userId);

    @Update("update c_user_device_mapping set IS_DEFAULT = (case DEVICE_ID when #{deviceId} then 1 else 0 end) where user_id = #{userId}")
    int setDefault(@Param("userId")String userId, @Param("deviceId")String deviceId);

    @Delete("delete from c_user_device_mapping where user_id = #{userId} and id = #{mappingId}")
    int deleteByUserIdAndMappingId(@Param("userId")String userId, @Param("mappingId")String mappingId);

    int insertSelective(CUserDeviceMapping record);

    CUserDeviceMapping selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CUserDeviceMapping record);

    int updateByPrimaryKey(CUserDeviceMapping record);

}