package com.university.shenyang.air.testing.monitoring.service.impl;
import com.university.shenyang.air.testing.mapper.HourInfoMapper;
import com.university.shenyang.air.testing.model.HourInfo;
import com.university.shenyang.air.testing.monitoring.service.HourInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by cjcqqqq on 2017/5/16.
 */
@Service("hourInfoService")
public class HourInfoServiceImpl implements HourInfoService {
    @Autowired
    private HourInfoMapper hourInfoMapper;

    @Override
    public int batchInsert(List<HourInfo> records) {

        return hourInfoMapper.batchInsert(records);
    }
    @Override
    public  int insert(HourInfo record) {

        return hourInfoMapper.insert(record);
    }

}
