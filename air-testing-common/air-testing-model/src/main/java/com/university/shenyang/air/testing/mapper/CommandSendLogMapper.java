package com.university.shenyang.air.testing.mapper;

import com.university.shenyang.air.testing.model.CommandSendLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommandSendLogMapper {
    int deleteByPrimaryKey(Long logId);

    int insert(CommandSendLog record);

    int insertSelective(CommandSendLog record);

    CommandSendLog selectByPrimaryKey(Long logId);

    int updateByPrimaryKeySelective(CommandSendLog record);

    int updateByPrimaryKey(CommandSendLog record);

    int updateByCodeCommandIdAndTime(CommandSendLog record);
}