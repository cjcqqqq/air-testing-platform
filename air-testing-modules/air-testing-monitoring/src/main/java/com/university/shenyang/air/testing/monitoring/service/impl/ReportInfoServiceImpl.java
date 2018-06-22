package com.university.shenyang.air.testing.monitoring.service.impl;

import com.university.shenyang.air.testing.mapper.ReportInfoMapper;
import com.university.shenyang.air.testing.model.DeviceInfo;
import com.university.shenyang.air.testing.model.ReportInfo;
import com.university.shenyang.air.testing.monitoring.command.QueryReportByDeviceCodeAndTimeCommand;
import com.university.shenyang.air.testing.monitoring.pojo.ReportInfoSim;
import com.university.shenyang.air.testing.monitoring.pojo.ReportTypeInfo;
import com.university.shenyang.air.testing.monitoring.service.DeviceInfoService;
import com.university.shenyang.air.testing.monitoring.service.ReportInfoService;
import com.university.shenyang.air.testing.monitoring.util.Constants;
import com.university.shenyang.air.testing.pojo.ReportQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
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
    public  List<ReportInfo> queryAllReportByDeviceCode(String deviceCode)
    {
        List<ReportInfo> result = new ArrayList<>();

        Set<ReportInfo> set = redisTemplate.opsForZSet().reverseRange(Constants.REPORT_REDIS_KEY_PREFIX + deviceCode, 0, 10);
        result.addAll(set);

        return result;
    }

    @Override
    public List<ReportInfo> queryReportByDeviceCodeAndTime(QueryReportByDeviceCodeAndTimeCommand command) {
        List<ReportInfo> reportList = null;
        ReportQueryParam reportQueryParam = new ReportQueryParam();
        reportQueryParam.setDeviceCode(command.getDeviceCode());
        reportQueryParam.setStartTime(command.getStartTime());
        reportQueryParam.setEndTime(command.getEndTime());
        reportQueryParam.setSkipCount(command.getPageSize()*(command.getPageNum() - 1));
        reportQueryParam.setPageSize(command.getPageSize());
        reportList = reportInfoMapper.selectByDeviceCodeAndTime(reportQueryParam);
        return reportList;
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
    public List<ReportInfo> queryAllHourDeviceLatestReport() {
//        List<ReportInfo> result = new ArrayList<>();
        List<DeviceInfo> deviceInfoList = deviceInfoService.queryAll();
//        Calendar todayStart = Calendar.getInstance();
//        todayStart.set(Calendar.HOUR_OF_DAY, 0);
//        todayStart.set(Calendar.MINUTE, 0);
//        todayStart.set(Calendar.SECOND, 0);
//        todayStart.set(Calendar.MILLISECOND, 0);
//
//        long startTime = todayStart.getTime().getTime() - 1000 * 60 * 60 * 24 ;
//        long endTime = todayStart.getTime().getTime();


        List<ReportInfo> reportInfoList = new ArrayList<>();
        for (DeviceInfo deviceInfo : deviceInfoList) {
//            List<ReportInfo> reportInfoList = new ArrayList<>();
            Set<ReportInfo> set = redisTemplate.opsForZSet().rangeByScore(Constants.REPORT_REDIS_KEY_PREFIX + deviceInfo.getDeviceCode(),  0, 10);
            if(set != null) {
                reportInfoList.addAll(set);
            }
//            ReportInfo record = null;
//            ReportInfo record = new ReportInfo();
//            if (reportInfoList != null && reportInfoList.size() > 0) {
//                for (int i = 0; i < reportInfoList.size(); i++) {
////                    record.setSim(reportInfoList.get(0).getSim());
//                    record.setPm1_0(record.getPm1_0() + reportInfoList.get(i).getPm1_0() / reportInfoList.size());
//                    record.setPm2_5(record.getPm2_5() + reportInfoList.get(i).getPm2_5() / reportInfoList.size());
//                    record.setPm10(record.getPm10() + reportInfoList.get(i).getPm10() / reportInfoList.size());
//                    record.setFormaldehyde(record.getFormaldehyde() + reportInfoList.get(i).getFormaldehyde() / reportInfoList.size());
//                    record.setTemperature(record.getTemperature() + reportInfoList.get(i).getTemperature() / reportInfoList.size());
//                    record.setHumidity(record.getHumidity() + reportInfoList.get(i).getHumidity() / reportInfoList.size());
//                    record.setCo(record.getCo() + reportInfoList.get(i).getCo() / reportInfoList.size());
//                    record.setCo2(record.getCo2() + reportInfoList.get(i).getCo2() / reportInfoList.size());
//                    record.setNo(record.getNo() + reportInfoList.get(i).getNo() / reportInfoList.size());
//                    record.setNo2(record.getNo2() + reportInfoList.get(i).getNo2() / reportInfoList.size());
//                    record.setO3(record.getO3() + reportInfoList.get(i).getO3() / reportInfoList.size());
//                    record.setSo2(record.getSo2() + reportInfoList.get(i).getSo2() / reportInfoList.size());
//                    record.setTvoc(record.getTvoc() + reportInfoList.get(i).getTvoc() / reportInfoList.size());
//                    record.setWindSpeed(record.getWindSpeed() + reportInfoList.get(i).getWindSpeed() / reportInfoList.size());
//
////                    record.setWindDirection(reportInfoList.get(0).getWindDirection());
////                    record.setLongitude(reportInfoList.get(0).getLongitude());
////                    record.setLatitude(reportInfoList.get(0).getLatitude());
////                    record.setElectricity(reportInfoList.get(0).getElectricity());
//
//                }
//                record.setSim( deviceInfo.getSim());
//                record.setWindDirection(reportInfoList.get(0).getWindDirection());
//                record.setLongitude(reportInfoList.get(0).getLongitude());
//                record.setLatitude(reportInfoList.get(0).getLatitude());
//                record.setElectricity(reportInfoList.get(0).getElectricity());
//
//            }
//               result.add(record);


        }

        return reportInfoList;

    }





    @Override
    public List<ReportInfoSim> queryAllDeviceLatestSim() {
        List<ReportInfoSim> result = new ArrayList<>();

        List<ReportInfo> reportInfos = queryAllDeviceLatestReport();

        if(reportInfos != null) {
            for (ReportInfo reportInfo : reportInfos) {
                ReportInfoSim reportInfoSim = new ReportInfoSim();
                reportInfoSim.setLng(reportInfo.getLongitude());
                reportInfoSim.setLat(reportInfo.getLatitude());
                reportInfoSim.setCount(reportInfo.getSim());

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
