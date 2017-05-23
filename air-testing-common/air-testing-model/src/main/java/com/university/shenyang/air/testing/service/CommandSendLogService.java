package com.university.shenyang.air.testing.service;

import com.university.shenyang.air.testing.model.CommandSendLog;
import com.university.shenyang.air.testing.model.ReportInfo;

import java.util.List;

/**
 * Created by cjcqqqq on 2017/5/15.
 */
public interface CommandSendLogService {
    int insert(CommandSendLog record);

    int updateByCodeCommandIdAndTime(CommandSendLog record);
}
