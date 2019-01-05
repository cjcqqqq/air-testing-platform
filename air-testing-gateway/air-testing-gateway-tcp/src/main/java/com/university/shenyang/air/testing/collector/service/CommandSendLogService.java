package com.university.shenyang.air.testing.collector.service;

import com.university.shenyang.air.testing.model.CommandSendLog;

/**
 * Created by cjcqqqq on 2017/5/15.
 */
public interface CommandSendLogService {
    int insert(CommandSendLog record);

    int updateByCodeCommandIdAndTime(CommandSendLog record);
}
