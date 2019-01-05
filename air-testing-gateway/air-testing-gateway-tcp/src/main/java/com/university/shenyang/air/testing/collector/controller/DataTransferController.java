package com.university.shenyang.air.testing.collector.controller;

import com.university.shenyang.air.testing.collector.cache.DevicesManager;
import com.university.shenyang.air.testing.collector.command.WifiSettingCommand;
import com.university.shenyang.air.testing.collector.dto.WifiSettingDto;
import com.university.shenyang.air.testing.collector.service.DeviceInfoService;
import com.university.shenyang.air.testing.collector.service.ReportInfoService;
import com.university.shenyang.air.testing.collector.util.Constants;
import com.university.shenyang.air.testing.model.DeviceInfo;
import com.university.shenyang.air.testing.model.ReportInfo;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


@RestController
@RequestMapping(value = "/data", method = {RequestMethod.GET, RequestMethod.POST}, produces = {"application/json;charset=UTF-8"})
public class DataTransferController extends BaseController {
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
     * 设置设备Wifi
     *
     * @param command
     * @param bindingResult
     * @return
     * @throws RuntimeException
     */
    @RequestMapping(value = "/transfer")
    public WifiSettingDto wifiSetting(@Validated WifiSettingCommand command, BindingResult bindingResult) throws RuntimeException {
        WifiSettingDto result = new WifiSettingDto();
        if (bindingResult.hasErrors()) {
            bindingResultFill(result, bindingResult);
        } else {
            // 获取登入链路上下文
            ChannelHandlerContext loginCtx = DevicesManager.getInstance().getCtxByDeviceCode(command.getDeviceCode());
            if (loginCtx == null) {
                result.setResultCode(400);
                result.setMsg(new String[]{"start data transfer"});
            } else {
                this.threadTo();
                result.setResultCode(200);
                result.setMsg(new String[]{"success"});
            }
        }

        return result;
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
