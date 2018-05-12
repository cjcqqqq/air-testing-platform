package com.university.shenyang.air.testing.monitoring.controller;

import com.university.shenyang.air.testing.monitoring.command.QueryAllDeviceLatestInfoByTypeCommand;
import com.university.shenyang.air.testing.monitoring.command.QueryAllDeviceLatestSimCommand;
import com.university.shenyang.air.testing.monitoring.command.QueryLatestReportCommand;
import com.university.shenyang.air.testing.monitoring.dto.QueryAllDeviceLatestSimDto;
import com.university.shenyang.air.testing.monitoring.dto.QueryLatestInfoByTypeDto;
import com.university.shenyang.air.testing.monitoring.dto.QueryLatestReportDto;
import com.university.shenyang.air.testing.monitoring.service.ReportInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/report", method = {RequestMethod.GET, RequestMethod.POST}, produces = {"application/json;charset=UTF-8"})
public class ReportController extends BaseController {
    /**
     * ReportController Logger
     */
    protected static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    ReportInfoService reportInfoService;

    /**
     * 查询最新上报数据
     *
     * @param command
     * @param bindingResult
     * @return
     * @throws RuntimeException
     */
    @RequestMapping(value = "/getLatestReport")
    public QueryLatestReportDto getLatestReport(@Validated QueryLatestReportCommand command, BindingResult bindingResult) throws RuntimeException {
        QueryLatestReportDto result = new QueryLatestReportDto();
        if (bindingResult.hasErrors()) {
            bindingResultFill(result, bindingResult);
        } else {
            result.setData(reportInfoService.queryLatestReportByDeviceCode(command.getDeviceCode()));
            result.setResultCode(200);

           result.setMsg(new String[]{"success"});

        }

        return result;
    }

    @RequestMapping(value = "/getAllDeviceLatestSim")
    public QueryAllDeviceLatestSimDto getAllDeviceLatestSim(@Validated QueryAllDeviceLatestSimCommand command, BindingResult bindingResult) throws RuntimeException {
        QueryAllDeviceLatestSimDto result = new QueryAllDeviceLatestSimDto();
        if (bindingResult.hasErrors()) {
            bindingResultFill(result, bindingResult);
        } else {
            result.setData(reportInfoService.queryAllDeviceLatestSim());
            result.setResultCode(200);
            result.setMsg(new String[]{"success"});
        }

        return result;
    }

    @RequestMapping(value = "/getAllDeviceLatestInfo")
    public QueryLatestInfoByTypeDto getAllDeviceLatestInfo(@Validated QueryAllDeviceLatestInfoByTypeCommand command, BindingResult bindingResult) throws RuntimeException {
        QueryLatestInfoByTypeDto result = new QueryLatestInfoByTypeDto();
        if (bindingResult.hasErrors()) {
            bindingResultFill(result, bindingResult);
        } else {
            result.setData(reportInfoService.queryAllDeviceLatestInfoByType(command.getType()));
            result.setResultCode(200);
            result.setMsg(new String[]{"success"});
        }

        return result;
    }
}
