package com.university.shenyang.air.testing.app.controller;



import com.university.shenyang.air.testing.model.HourInfo;
import com.university.shenyang.air.testing.app.command.QueryOneHourInfoByDeviceCodeCommand;
import com.university.shenyang.air.testing.app.response.QueryOneHourInfoByDeviceCodeDto;
import com.university.shenyang.air.testing.app.service.HourInfoService;
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
@RequestMapping(value = "/hour", method = {RequestMethod.DELETE,RequestMethod.GET, RequestMethod.POST,RequestMethod.PUT}, produces = {"application/json;charset=UTF-8"})
public class HourController extends BaseController {
    /**
     * ReportController Logger
     */
    protected static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    HourInfoService hourInfoService;

    /**
     * 查询最新上报数据
     *
     * @param command
     * @param bindingResult
     * @return
     * @throws RuntimeException
     */



    @RequestMapping(value = "/queryOneHour")
    public QueryOneHourInfoByDeviceCodeDto queryOneHour(@Validated QueryOneHourInfoByDeviceCodeCommand command, BindingResult bindingResult) throws RuntimeException {
        QueryOneHourInfoByDeviceCodeDto result = new QueryOneHourInfoByDeviceCodeDto();
        if (bindingResult.hasErrors()) {
            bindingResultFill(result, bindingResult);
        } else {
            List<HourInfo> deviceInfos = hourInfoService.selectLatestByDeviceCode(command.getDeviceCode());
            result.setData(deviceInfos);
            result.setResultCode(200);
            result.setMsg(new String[]{"success"});

        }
        return result;
    }


}
