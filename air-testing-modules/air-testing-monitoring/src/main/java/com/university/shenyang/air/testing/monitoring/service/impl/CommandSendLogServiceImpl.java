package com.university.shenyang.air.testing.monitoring.service.impl;

import com.university.shenyang.air.testing.mapper.CommandSendLogMapper;
import com.university.shenyang.air.testing.model.CommandSendLog;
import com.university.shenyang.air.testing.monitoring.service.CommandSendLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by cjcqqqq on 2017/5/16.
 */
@Service("commandSendLogService")
public class CommandSendLogServiceImpl implements CommandSendLogService {
    @Autowired
    private CommandSendLogMapper commandSendLogMapper;


    @Override
    public int insert(CommandSendLog record) {
        return commandSendLogMapper.insert(record);
    }

    @Override
    public int updateByCodeCommandIdAndTime(CommandSendLog record) {
        return commandSendLogMapper.updateByCodeCommandIdAndTime(record);
    }
}
