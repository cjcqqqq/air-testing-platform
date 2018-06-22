package com.university.shenyang.air.testing.monitoring.controller;



import com.university.shenyang.air.testing.model.DayInfo;
import com.university.shenyang.air.testing.monitoring.command.QueryOneDayInfoByDeviceCodeCommand;
import com.university.shenyang.air.testing.monitoring.dto.QueryOneDayInfoByDeviceCodeDto;
import com.university.shenyang.air.testing.monitoring.service.DayInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/day", method = {RequestMethod.DELETE,RequestMethod.GET, RequestMethod.POST,RequestMethod.PUT}, produces = {"application/json;charset=UTF-8"})
public class DayController extends BaseController {
    /**
     * ReportController Logger
     */
    protected static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    DayInfoService dayInfoService;

    /**
     * 查询最新上报数据
     *
     * @param command
     * @param bindingResult
     * @return
     * @throws RuntimeException
     */



    @RequestMapping(value = "/queryOneDay")
    public QueryOneDayInfoByDeviceCodeDto queryOneDay(@Validated QueryOneDayInfoByDeviceCodeCommand command, BindingResult bindingResult) throws RuntimeException {
        QueryOneDayInfoByDeviceCodeDto result = new QueryOneDayInfoByDeviceCodeDto();
        if (bindingResult.hasErrors()) {
            bindingResultFill(result, bindingResult);
        } else {
            List<DayInfo> deviceInfos = dayInfoService.selectLatestByDeviceCode(command.getDeviceCode());
            result.setData(deviceInfos);
            result.setResultCode(200);
            result.setMsg(new String[]{"success"});

        }
        return result;
    }


}
