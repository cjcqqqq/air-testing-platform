package com.university.shenyang.air.testing.app.task;

import com.university.shenyang.air.testing.model.ReportInfo;
import com.university.shenyang.air.testing.app.service.DeviceInfoService;
import com.university.shenyang.air.testing.app.service.HourInfoService;
import com.university.shenyang.air.testing.app.util.Constants;
import com.university.shenyang.air.testing.model.DeviceInfo;

import com.university.shenyang.air.testing.model.HourInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by cjcqqqq on 2017/5/18.
 */
@Component
public class HourDataTransferTask {
    @Autowired
    DeviceInfoService deviceInfoService;
    @Autowired
    HourInfoService hourInfoService;
    @Autowired
    public RedisTemplate<String, ReportInfo> redisTemplate;

    @Value("${hour.cal.insertmysql.batch.size:100}")
    private int batchSize;

    @Scheduled(cron = "${hour.cal.cron:0 59 * * * ?}")
    public void dataTransfer() {
        try {
            // 获取全部设备信息
            List<DeviceInfo> deviceInfoList = deviceInfoService.queryAll();
            // 获取当前时间之前的整点时间
            Calendar currentHour = Calendar.getInstance();
//            currentHour.set(Calendar.HOUR_OF_DAY, 0);
            currentHour.set(Calendar.MINUTE, 0);
            currentHour.set(Calendar.SECOND, 0);
            currentHour.set(Calendar.MILLISECOND, 0);

            //  从当前时间之前的整点时间再往前推一个小时的毫秒值
            long startTime = currentHour.getTime().getTime() - 1000 * 60 * 60;
            //  获取当前时间之前的整点时间的毫秒值
            long endTime = currentHour.getTime().getTime();

            // 各设备一小时均值结果
            List<HourInfo> hourInfoList = new ArrayList<>();

            // 遍历所以设备分别计算这些设备一小时上报数据的均值
            for (DeviceInfo deviceInfo : deviceInfoList) {
                // 从redis中获取指定设备一个小时的上报数据
                List<ReportInfo> reportInfoList = new ArrayList<>();
                Set<ReportInfo> reportInfos = redisTemplate.opsForZSet().rangeByScore(Constants.REPORT_REDIS_KEY_PREFIX + deviceInfo.getDeviceCode(), startTime, endTime);
                reportInfoList.addAll(reportInfos);

                // 判断从redis中是否取到数据
                if (reportInfoList != null && reportInfoList.size() > 0) {
                    int pm1_0 = 0;
                    int pm2_5 = 0;
                    int pm10 = 0;
                    float formaldehyde = 0F;
                    float temperature = 0F;
                    int humidity = 0;
                    int co = 0;
                    int co2 = 0;
                    int no = 0;
                    int no2 = 0;
                    int o3 = 0;
                    int so2 = 0;
                    int tvoc = 0;
                    int windSpeed = 0;
                    int windDirection = 0;
                    int electricity = 0;
                    // 遍历设备一个小时的上报数据，计算各指标的均值
                    for (ReportInfo reportInfo : reportInfoList) {
                        pm1_0 += reportInfo.getPm1_0();
                        pm2_5 += reportInfo.getPm2_5();
                        pm10 += reportInfo.getPm10();
                        formaldehyde += reportInfo.getFormaldehyde();
                        temperature += reportInfo.getTemperature();
                        humidity += reportInfo.getHumidity();
                        co += reportInfo.getCo();
                        co2 += reportInfo.getCo2();
                        no += reportInfo.getNo();
                        no2 += reportInfo.getNo2();
                        o3 += reportInfo.getO3();
                        so2 += reportInfo.getSo2();
                        tvoc += reportInfo.getTvoc();
                        windSpeed += reportInfo.getWindSpeed();
                        windDirection += reportInfo.getWindDirection();
                        electricity += reportInfo.getElectricity();
                    }
                    HourInfo record = new HourInfo();
                    record.setDeviceCode(deviceInfo.getDeviceCode());
                    record.setCollectTime(new Date(startTime));
                    record.setSim(deviceInfo.getSim());
                    record.setPm1_0(pm1_0 / reportInfoList.size());
                    record.setPm2_5(pm2_5 / reportInfoList.size());
                    record.setPm10(pm10 / reportInfoList.size());
                    record.setFormaldehyde(formaldehyde / reportInfoList.size());
                    record.setTemperature(temperature / reportInfoList.size());
                    record.setHumidity(humidity / reportInfoList.size());
                    record.setCo(co / reportInfoList.size());
                    record.setCo2(co2 / reportInfoList.size());
                    record.setNo(no / reportInfoList.size());
                    record.setNo2(no2 / reportInfoList.size());
                    record.setO3(o3 / reportInfoList.size());
                    record.setSo2(so2 / reportInfoList.size());
                    record.setTvoc(tvoc / reportInfoList.size());
                    record.setWindSpeed(windSpeed / reportInfoList.size());
                    record.setWindDirection(windDirection / reportInfoList.size());
                    record.setLongitude(deviceInfo.getLongitude());
                    record.setLatitude(deviceInfo.getLatitude());
                    record.setElectricity(electricity / reportInfoList.size());
                    hourInfoList.add(record);
                }else {
                    int pm1_0 = 0;
                    int pm2_5 = 0;
                    int pm10 = 0;
                    float formaldehyde = 0F;
                    float temperature = 0F;
                    int humidity = 0;
                    int co = 0;
                    int co2 = 0;
                    int no = 0;
                    int no2 = 0;
                    int o3 = 0;
                    int so2 = 0;
                    int tvoc = 0;
                    int windSpeed = 0;
                    int windDirection = 0;
                    int electricity = 0;

                    HourInfo record = new HourInfo();
                    record.setDeviceCode(deviceInfo.getDeviceCode());
                    record.setCollectTime(new Date(startTime));
                    record.setSim(deviceInfo.getSim());
                    record.setPm1_0(pm1_0);
                    record.setPm2_5(pm2_5);
                    record.setPm10(pm10 );
                    record.setFormaldehyde(formaldehyde);
                    record.setTemperature(temperature );
                    record.setHumidity(humidity);
                    record.setCo(co);
                    record.setCo2(co2);
                    record.setNo(no);
                    record.setNo2(no2);
                    record.setO3(o3);
                    record.setSo2(so2);
                    record.setTvoc(tvoc);
                    record.setWindSpeed(windSpeed);
                    record.setWindDirection(windDirection);
                    record.setLongitude(deviceInfo.getLongitude());
                    record.setLatitude(deviceInfo.getLatitude());
                    record.setElectricity(electricity);
                    hourInfoList.add(record);
                }

            }
            if (hourInfoList.size() > 0) {
                if (hourInfoList.size() > batchSize) {
                    for (int i = 0; i < hourInfoList.size() / batchSize; i++) {
                        hourInfoService.batchInsert(hourInfoList.subList(i * batchSize, (i + 1) * batchSize - 1));
                    }
                    if (hourInfoList.size() % batchSize != 0) {
                        hourInfoService.batchInsert(hourInfoList.subList(hourInfoList.size() / batchSize * batchSize, hourInfoList.size() - 1));
                    }
                } else {
                    hourInfoService.batchInsert(hourInfoList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
