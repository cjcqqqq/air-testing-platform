package com.university.shenyang.air.testing.app.controller;

import com.university.shenyang.air.testing.app.cache.DevicesManager;
import com.university.shenyang.air.testing.app.command.*;
import com.university.shenyang.air.testing.app.pojo.UserDeviceInfo;
import com.university.shenyang.air.testing.app.pojo.UserReportInfo;
import com.university.shenyang.air.testing.app.redis.RedisClient;
import com.university.shenyang.air.testing.app.response.AppBaseResponse;
import com.university.shenyang.air.testing.app.response.GetUserDeviceListResponse;
import com.university.shenyang.air.testing.app.response.QueryReportResponse;
import com.university.shenyang.air.testing.app.response.WifiSettingResponse;
import com.university.shenyang.air.testing.app.service.*;
import com.university.shenyang.air.testing.common.context.BaseContextHandler;
import com.university.shenyang.air.testing.common.util.AESUtil;
import com.university.shenyang.air.testing.model.DayInfo;
import com.university.shenyang.air.testing.model.HourInfo;
import com.university.shenyang.air.testing.model.ReportInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "/app/device", method = {RequestMethod.GET, RequestMethod.POST}, produces = {"application/json;charset=UTF-8"})
public class UserDeviceController extends BaseController {
    /**
     * ReportController Logger
     */
    protected static final Logger logger = LoggerFactory.getLogger(UserDeviceController.class);

    @Autowired
    CUserDeviceMappingService cUserDeviceMappingService;

    @Autowired
    DeviceInfoService deviceInfoService;

    @Autowired
    ReportInfoService reportInfoService;

    @Autowired
    HourInfoService hourInfoService;

    @Autowired
    DayInfoService dayInfoService;

    @Autowired
    private RedisClient redisClient;

    @Value("${devicecode.aes.encryption.pwd}")
    private String aesPwd;

    // 设备在线状态判断有效时间范围单位分钟
    @Value("${device.online.effective.time:30}")
    private int effectiveTime;

    // 设置WIFI账号、密码的接口URL
    @Value("${wifisetting.url}")
    private String wifiSettingUrl;

    /**
     * 设备二维码验证
     */
    @RequestMapping(value = "/checkQRcode", method = RequestMethod.POST)
    @ResponseBody
    public AppBaseResponse checkQRcode(@Validated CheckQRcodeCommand checkQRcodeCommand, BindingResult bindingResult) {
        AppBaseResponse result = new AppBaseResponse();
        try {
            if (bindingResult.hasErrors()) {
                bindingResultFill(result, bindingResult);
                return result;
            } else {
                // 解析二维码
                String deviceCode = AESUtil.decrypt(checkQRcodeCommand.getQRcode(), aesPwd);
                if (deviceCode == null) {
                    return AppBaseResponse.DEVICE_INVALID;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AppBaseResponse.exception(e);
        }

        return result;
    }

    /**
     * 用户添加设备
     */
    @RequestMapping(value = "/addDevice", method = RequestMethod.POST)
    @ResponseBody
    public AppBaseResponse addDevice(@Validated UserAddDeviceCommand userAddDeviceCommand, BindingResult bindingResult) {
        AppBaseResponse result = new AppBaseResponse();
        try {
            if (bindingResult.hasErrors()) {
                bindingResultFill(result, bindingResult);
                return result;
            } else {
                logger.info("QRcode: ",userAddDeviceCommand.getQRcode());
                logger.info("aesPwd: ",aesPwd);
                // 解析二维码
                String deviceCode = AESUtil.decrypt(userAddDeviceCommand.getQRcode(), aesPwd);
                if (deviceCode == null) {
                    return AppBaseResponse.DEVICE_INVALID;
                }

                int insertResult = cUserDeviceMappingService.userAddDevice(BaseContextHandler.getUserID(), deviceCode, userAddDeviceCommand.getDeviceDesc());

                switch (insertResult){
                    case 1:
                        return AppBaseResponse.DEVICE_INVALID;
                    case 2:
                        return AppBaseResponse.USER_DEVICE_HAS_BEEN_ADDED;
                    case 3:
                        return AppBaseResponse.DEVICE_DESC_HAS_BEEN_USEED;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AppBaseResponse.exception(e);
        }

        return result;
    }

    /**
     * 获取用户设备列表
     */
    @RequestMapping(value = "/getUserDeviceList", method = RequestMethod.POST)
    @ResponseBody
    public GetUserDeviceListResponse getUserDeviceList() {
        GetUserDeviceListResponse result = new GetUserDeviceListResponse();

        List<UserDeviceInfo> deviceList = cUserDeviceMappingService.getUserDeviceList(BaseContextHandler.getUserID());
        result.setData(deviceList);

        return result;
    }

    /**
     * 设置默认设备
     */
    @RequestMapping(value = "/setDefault", method = RequestMethod.POST)
    @ResponseBody
    public AppBaseResponse setDefault(@Validated SetDefaultCommand setDefaultCommand, BindingResult bindingResult) {
        AppBaseResponse result = new AppBaseResponse();
        try {
            if (bindingResult.hasErrors()) {
                bindingResultFill(result, bindingResult);
                return result;
            } else {
                // 更新并获取更新记录数
                cUserDeviceMappingService.setDefault(BaseContextHandler.getUserID(),setDefaultCommand.getDeviceId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AppBaseResponse.exception(e);
        }

        return result;
    }

    /**
     * 删除用户设备
     */
    @RequestMapping(value = "/queryUserReport", method = RequestMethod.POST)
    @ResponseBody
    public QueryReportResponse queryUserReport(@Validated QueryReportCommand queryReportCommand, BindingResult bindingResult) {
        QueryReportResponse result = new QueryReportResponse();
        if (bindingResult.hasErrors()) {
            bindingResultFill(result, bindingResult);
            return result;
        } else {
            UserReportInfo userReportInfo = new UserReportInfo();
            String deviceCode = DevicesManager.getInstance().getDeviceCodeById(Long.parseLong(queryReportCommand.getDeviceId()));

            boolean isOnline = false;
            // 获取最新上报数据
            ReportInfo reportInfo = reportInfoService.queryLatestReportByDeviceCode(deviceCode);
            if (reportInfo != null) {
                userReportInfo.setFormaldehyde(String.valueOf(reportInfo.getFormaldehyde()));
                userReportInfo.setTvoc(String.valueOf(reportInfo.getTvoc()));
                userReportInfo.setCollectTime(reportInfo.getCollectTime());

                if (Math.abs((new Date()).getTime() - reportInfo.getCollectTime().getTime()) < effectiveTime * 1000) {
                    isOnline = true;
                }
            }

            if (isOnline) {
                userReportInfo.setIsOnline(1);
            } else {
                userReportInfo.setIsOnline(0);
            }

            List<HourInfo> hourInfos = hourInfoService.selectLatestByDeviceCode(deviceCode);
            userReportInfo.setHourInfos(hourInfos);
            List<DayInfo> dayInfos = dayInfoService.selectLatestByDeviceCode(deviceCode);
            userReportInfo.setDayinfos(dayInfos);
            result.setData(userReportInfo);
        }

        return result;
    }

    /**
     * 删除用户设备
     */
    @RequestMapping(value = "/deleteUserDevice", method = RequestMethod.POST)
    @ResponseBody
    public AppBaseResponse getReportByDeviceId(@Validated DeleteUserDeviceCommand deleteUserDeviceCommand, BindingResult bindingResult) {
        AppBaseResponse result = new AppBaseResponse();
        try {
            if (bindingResult.hasErrors()) {
                bindingResultFill(result, bindingResult);
                return result;
            } else {
                // 更新并获取更新记录数
                cUserDeviceMappingService.deleteByUserIdAndMappingId(BaseContextHandler.getUserID(),deleteUserDeviceCommand.getMappingId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AppBaseResponse.exception(e);
        }

        return result;
    }

    /**
     * 设置设备Wifi
     *
     * @param command
     * @param bindingResult
     * @return
     * @throws RuntimeException
     */
    @RequestMapping(value = "/wifiSetting", method = RequestMethod.POST)
    public WifiSettingResponse wifiSetting(@Validated WifiSettingCommand command, BindingResult bindingResult) throws RuntimeException {
        WifiSettingResponse result = new WifiSettingResponse();
        if (bindingResult.hasErrors()) {
            bindingResultFill(result, bindingResult);
        } else {
            //入参
            MultiValueMap<String, Object> gateWifiSettingCommand = new LinkedMultiValueMap<String, Object>();
            String deviceCode = DevicesManager.getInstance().getDeviceCodeById(Long.parseLong(command.getDeviceId()));
            gateWifiSettingCommand.add("deviceCode",deviceCode);
            gateWifiSettingCommand.add("account",command.getAccount());
            gateWifiSettingCommand.add("password",command.getPassword());

            RestTemplate restTemplate = new RestTemplate();
            result = restTemplate.postForObject(wifiSettingUrl, gateWifiSettingCommand, WifiSettingResponse.class);
        }

        return result;
    }
}
