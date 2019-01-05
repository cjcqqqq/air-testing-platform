package com.university.shenyang.air.testing.collector.controller;

import com.university.shenyang.air.testing.collector.service.DeviceInfoService;
import com.university.shenyang.air.testing.collector.service.ReportInfoService;
import com.university.shenyang.air.testing.collector.util.Constants;
import com.university.shenyang.air.testing.model.DeviceInfo;
import com.university.shenyang.air.testing.model.ReportInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping(value = "/data", method = {RequestMethod.GET, RequestMethod.POST}, produces = {"application/json;charset=UTF-8"})
public class DataTransferController {
    /**
     * DeviceController Logger
     */
    protected static final Logger logger = LoggerFactory.getLogger(DataTransferController.class);

    @Autowired
    DeviceInfoService deviceInfoService;
    @Autowired
    ReportInfoService reportInfoService;
    @Autowired
    public RedisTemplate<String, ReportInfo> redisTemplate;

    @Value("${data.transfer.batch.size:100}")
    private int batchSize;


    /**
     * 接口触发转存
     *
     * @return
     * @throws RuntimeException
     */
    @RequestMapping(value = "/transfer")
    public String transfer() throws RuntimeException {
        this.threadTo();
        return "success";
    }

    public void threadTo() {
        new Thread() {
            public void run() {
                try {
                    List<DeviceInfo> deviceInfoList = deviceInfoService.queryAll();
                    Calendar todayStart = Calendar.getInstance();
                    todayStart.set(Calendar.HOUR_OF_DAY, 0);
                    todayStart.set(Calendar.MINUTE, 0);
                    todayStart.set(Calendar.SECOND, 0);
                    todayStart.set(Calendar.MILLISECOND, 0);

                    long startTime = todayStart.getTime().getTime() - 1000 * 60 * 60 * 24 * 2;
                    long endTime = todayStart.getTime().getTime();

                    for (DeviceInfo deviceInfo : deviceInfoList) {
                        List<ReportInfo> reportInfoList = new ArrayList<>();
                        Set<ReportInfo> reportInfos = redisTemplate.opsForZSet().rangeByScore(Constants.REPORT_REDIS_KEY_PREFIX + deviceInfo.getDeviceCode(), startTime, endTime);
                        reportInfoList.addAll(reportInfos);

                        if (reportInfoList.size() > 0) {
                            if (reportInfoList.size() > batchSize) {
                                for (int i = 0; i < reportInfoList.size() / batchSize; i++) {
                                    reportInfoService.batchInsert(reportInfoList.subList(i * batchSize, (i + 1) * batchSize - 1));
                                }
                                if (reportInfoList.size() % batchSize != 0) {
                                    reportInfoService.batchInsert(reportInfoList.subList(reportInfoList.size() / batchSize * batchSize, reportInfoList.size() - 1));
                                }
                            } else {
                                reportInfoService.batchInsert(reportInfoList);
                            }
                        }

                        redisTemplate.opsForZSet().removeRangeByScore(Constants.REPORT_REDIS_KEY_PREFIX + deviceInfo.getDeviceCode(), startTime, endTime);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
