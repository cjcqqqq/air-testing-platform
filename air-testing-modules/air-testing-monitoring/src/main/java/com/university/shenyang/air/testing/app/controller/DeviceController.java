package com.university.shenyang.air.testing.app.controller;


import com.university.shenyang.air.testing.model.DeviceInfo;
import com.university.shenyang.air.testing.app.command.AddDeviceCommand;
import com.university.shenyang.air.testing.app.command.QueryAllDeviceCommand;
import com.university.shenyang.air.testing.app.command.QueryDeviceByUserAndMinutesCommand;
import com.university.shenyang.air.testing.app.command.QueryOneDeviceCommand;
import com.university.shenyang.air.testing.app.response.QueryAllDeviceDto;
import com.university.shenyang.air.testing.app.response.QueryOneDeviceDto;
import com.university.shenyang.air.testing.app.service.DeviceInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/device", method = {RequestMethod.DELETE,RequestMethod.GET, RequestMethod.POST,RequestMethod.PUT}, produces = {"application/json;charset=UTF-8"})
public class DeviceController extends BaseController {
    /**
     * ReportController Logger
     */
    protected static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    DeviceInfoService deviceInfoService;

    /**
     * 查询最新上报数据
     *
     * @param command
     * @param bindingResult
     * @return
     * @throws RuntimeException
     */

    @RequestMapping(value = "/getAllLatestDevice")
    public QueryAllDeviceDto getAllLatestDevice(@Validated QueryAllDeviceCommand command, BindingResult bindingResult) throws RuntimeException {
        QueryAllDeviceDto result = new QueryAllDeviceDto();
        if (bindingResult.hasErrors()) {
            bindingResultFill(result, bindingResult);
        } else {
            List<DeviceInfo> deviceInfos = deviceInfoService.queryAll();
            result.setData(deviceInfos);
            result.setResultCode(200);
            result.setMsg(new String[]{"success"});
        }
        return result;
    }

    @RequestMapping(value = "/queryOneDevice")
    public QueryOneDeviceDto getAllLatestDevice(@Validated QueryOneDeviceCommand command, BindingResult bindingResult) throws RuntimeException {
        QueryOneDeviceDto result = new QueryOneDeviceDto();
        if (bindingResult.hasErrors()) {
            bindingResultFill(result, bindingResult);
        } else {
            DeviceInfo deviceInfo = deviceInfoService.queryOneDeviceByDeviceCode(command.getDeviceCode());
            result.setData(deviceInfo);
            result.setResultCode(200);
            result.setMsg(new String[]{"success"});

        }
        return result;
    }

    @RequestMapping(value = "/deleteOneDevice/{id}")
    public void deleteOneDevice(@PathVariable("id") long id) throws RuntimeException {

        deviceInfoService.deleteByPrimaryKey(id);
    }



    @RequestMapping(value = "/insertOneDevice")
    public int insertDevice(@Validated AddDeviceCommand addDeviceCommand
    )throws RuntimeException {
        return deviceInfoService.insertDeviceInfo(addDeviceCommand);
    }

    /**
     * 根据用户名和更新时间查询设备信息
     *
     * @param command
     * @param bindingResult
     * @return
     * @throws RuntimeException
     */

    @RequestMapping(value = "/queryDeviceByUserAndTime")
    public QueryAllDeviceDto queryDeviceByUserAndTime(@Validated QueryDeviceByUserAndMinutesCommand command, BindingResult bindingResult) throws RuntimeException {
        QueryAllDeviceDto result = new QueryAllDeviceDto();
        if (bindingResult.hasErrors()) {
            bindingResultFill(result, bindingResult);
        } else {
            List<DeviceInfo> deviceInfos = deviceInfoService.selectByUsernameAndTime(command.getUsername(), Integer.valueOf(command.getMinutes()));
            result.setData(deviceInfos);
            result.setResultCode(200);
            result.setMsg(new String[]{"success"});
        }
        return result;
    }
  }
