package com.university.shenyang.air.testing.monitoring.service.impl;

import com.university.shenyang.air.testing.mapper.ReportInfoMapper;
import com.university.shenyang.air.testing.model.DeviceInfo;
import com.university.shenyang.air.testing.model.ReportInfo;
import com.university.shenyang.air.testing.monitoring.pojo.ReportInfoSim;
import com.university.shenyang.air.testing.monitoring.pojo.ReportTypeInfo;
import com.university.shenyang.air.testing.monitoring.service.DeviceInfoService;
import com.university.shenyang.air.testing.monitoring.service.ReportInfoService;
import com.university.shenyang.air.testing.monitoring.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    private DeviceInfoService deviceInfoService;


    @Override
    public int batchInsert(List<ReportInfo> records) {
        return reportInfoMapper.batchInsert(records);
    }


    @Override
    public ReportInfo queryLatestReportByDeviceCode(String deviceCode) {
        ReportInfo result = null;

        // 获取最新上报数据
        result = redisTemplate.opsForValue().get(Constants.LATEST_REPORT_REDIS_KEY_PREFIX + deviceCode);

        // 如果从redis最新数据中没有获取到数据，则从redis的当天数据中获取最新一条数据
        if(result == null){
            Set<ReportInfo> set = redisTemplate.opsForZSet().reverseRange(Constants.REPORT_REDIS_KEY_PREFIX + deviceCode, 0, 0);
            if(set != null && set.size() > 0){
                if(set.iterator().hasNext()){
                    result = set.iterator().next();
                }
            }
        }

        // 如果还是没有获取到数据，则从mysql中获取最新一条数据
        if(result == null){
            result = reportInfoMapper.selectLatestByDeviceCode(deviceCode);
        }
        return result;
    }

    @Override
    public List<ReportInfo> queryAllDeviceLatestReport() {
        List<ReportInfo> result = new ArrayList<>();
        List<DeviceInfo> deviceInfos = deviceInfoService.queryAll();

        for(DeviceInfo deviceInfo : deviceInfos){
            ReportInfo reportInfo = redisTemplate.opsForValue().get(Constants.LATEST_REPORT_REDIS_KEY_PREFIX + deviceInfo.getDeviceCode());
            if(reportInfo != null) {
                if("1.0".equals(deviceInfo.getProtocol())){
                    reportInfo.setLongitude(deviceInfo.getLongitude());
                    reportInfo.setLatitude(deviceInfo.getLatitude());
                }
                result.add(reportInfo);
            }
        }

        return result;
    }

    @Override
    public List<ReportInfoSim> queryAllDeviceLatestSim() {
        List<ReportInfoSim> result = new ArrayList<>();

        List<ReportInfo> reportInfos = queryAllDeviceLatestReport();

        if(reportInfos != null) {
            for (ReportInfo reportInfo : reportInfos) {
                ReportInfoSim reportInfoSim = new ReportInfoSim();
                reportInfoSim.setLatitude(reportInfo.getLatitude());
                reportInfoSim.setLongitude(reportInfo.getLongitude());
                reportInfoSim.setSim(reportInfo.getSim());

                result.add(reportInfoSim);
            }
        }

        return result;
    }

    @Override
    public List<ReportTypeInfo> queryAllDeviceLatestInfoByType(String type) {
        List<ReportTypeInfo> result = new ArrayList<>();

        List<ReportInfo> reportInfos = queryAllDeviceLatestReport();

        if(reportInfos != null) {
            for (ReportInfo reportInfo : reportInfos) {
                ReportTypeInfo reportTypeInfo = new ReportTypeInfo();
                reportTypeInfo.setLatitude(reportInfo.getLatitude());
                reportTypeInfo.setLongitude(reportInfo.getLongitude());
                reportTypeInfo.setType(type);
                 // 温度
                if("1".equals(type)){
                    reportTypeInfo.setTypeValue(String.valueOf(reportInfo.getTemperature()));
                }
                 // pm2.5
                if("2".equals(type)){
                    reportTypeInfo.setTypeValue(String.valueOf(reportInfo.getPm2_5()));
                }
                 // pm10
                if("3".equals(type)){
                    reportTypeInfo.setTypeValue(String.valueOf(reportInfo.getPm10()));
                }
                // Co
                if("4".equals(type)){
                    reportTypeInfo.setTypeValue(String.valueOf(reportInfo.getCo()));
                }
                // Co2
                if("5".equals(type)){
                    reportTypeInfo.setTypeValue(String.valueOf(reportInfo.getCo2()));
                }
                // No
                if("6".equals(type)){
                    reportTypeInfo.setTypeValue(String.valueOf(reportInfo.getNo()));
                }
                // No2
                if("7".equals(type)){
                    reportTypeInfo.setTypeValue(String.valueOf(reportInfo.getNo2()));
                }
                // O3
                if("8".equals(type)){
                    reportTypeInfo.setTypeValue(String.valueOf(reportInfo.getO3()));
                }
                // Pm1_0
                if("9".equals(type)){
                    reportTypeInfo.setTypeValue(String.valueOf(reportInfo.getPm1_0()));
                }
                // So2
                if("10".equals(type)){
                    reportTypeInfo.setTypeValue(String.valueOf(reportInfo.getSo2()));
                }
                // Formaldehyde
                if("11".equals(type)){
                    reportTypeInfo.setTypeValue(String.valueOf(reportInfo.getFormaldehyde()*1000));
                }
                // Humidity
                if("12".equals(type)){
                    reportTypeInfo.setTypeValue(String.valueOf(reportInfo.getHumidity()));
                }
                // Tvoc
                if("13".equals(type)){
                    reportTypeInfo.setTypeValue(String.valueOf(reportInfo.getTvoc()));
                }


                result.add(reportTypeInfo);
            }
        }

        return result;
    }

}
