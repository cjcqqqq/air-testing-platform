package com.university.shenyang.air.testing.monitoring.service.impl;

import com.university.shenyang.air.testing.mapper.ReportInfoMapper;
import com.university.shenyang.air.testing.model.ReportInfo;
import com.university.shenyang.air.testing.monitoring.service.ReportInfoService;
import com.university.shenyang.air.testing.monitoring.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by cjcqqqq on 2017/5/16.
 */
@Service("reportInfoService")
public class ReportInfoServiceImpl implements ReportInfoService {
    @Autowired
    private ReportInfoMapper reportInfoMapper;

    @Autowired
    public RedisTemplate<String, ReportInfo> redisTemplate;

    @Override
    public int batchInsert(List<ReportInfo> records) {
        return reportInfoMapper.batchInsert(records);
    }

    @Override
    public ReportInfo queryLatestReportByDeviceCode(String deviceCode) {
        ReportInfo result = null;

        Set<ReportInfo> set = redisTemplate.opsForZSet().reverseRange(Constants.REPORT_REDIS_KEY_PREFIX + deviceCode, 0, 0);
        if(set != null && set.size() > 0){
            if(set.iterator().hasNext()){
                result = set.iterator().next();
            }
        }
        if(result == null){
            result = reportInfoMapper.selectLatestByDeviceCode(deviceCode);
        }
        return result;
    }
}
