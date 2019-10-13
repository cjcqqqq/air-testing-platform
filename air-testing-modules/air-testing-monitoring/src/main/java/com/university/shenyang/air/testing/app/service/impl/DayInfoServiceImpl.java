package com.university.shenyang.air.testing.app.service.impl;

import com.university.shenyang.air.testing.mapper.DayInfoMapper;
import com.university.shenyang.air.testing.model.DayInfo;
import com.university.shenyang.air.testing.app.service.DayInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by cjcqqqq on 2017/5/16.
 */
@Service("dayInfoService")
public class DayInfoServiceImpl implements DayInfoService {
    @Autowired
    private DayInfoMapper dayInfoMapper;

    @Override
    public int batchInsert(List<DayInfo> records) {

        return dayInfoMapper.batchInsert(records);
    }
    @Override
    public  int insert(DayInfo record) {

        return dayInfoMapper.insert(record);
    }

    @Override
    public  List<DayInfo> selectLatestByDeviceCode(String deviceCode) {
        List<DayInfo> result = dayInfoMapper.selectLatestByDeviceCode(deviceCode);

        return result;

    }
}
