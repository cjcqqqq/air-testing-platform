package com.university.shenyang.air.testing.mapper;

import com.university.shenyang.air.testing.model.CUserInfo;
import com.university.shenyang.air.testing.pojo.DeviceCodeMappingUsername;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CUserInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CUserInfo record);

    @Select("select ID, USERNAME, PASSWORD, MOBILE, EMAIL, WEIXIN, QQ, CREATETIME, UPDATETIME from c_user_info where USERNAME = #{username}")
    CUserInfo selectByUsername(@Param("username") String username);

    int insertSelective(CUserInfo record);

    CUserInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CUserInfo record);

    int updateByPrimaryKey(CUserInfo record);

    @Update("update c_user_info set password = #{password} where id = #{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password);
}