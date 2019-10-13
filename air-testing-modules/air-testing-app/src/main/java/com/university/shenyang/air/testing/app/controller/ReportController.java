package com.university.shenyang.air.testing.app.controller;

import com.university.shenyang.air.testing.app.command.*;
import com.university.shenyang.air.testing.app.response.*;
import com.university.shenyang.air.testing.app.service.ReportInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public QueryLatestReportResponse getLatestReport(@Validated QueryLatestReportCommand command, BindingResult bindingResult) throws RuntimeException {
        QueryLatestReportResponse result = new QueryLatestReportResponse();
        if (bindingResult.hasErrors()) {
            bindingResultFill(result, bindingResult);
        } else {
            result.setData(reportInfoService.queryLatestReportByDeviceCode(command.getDeviceCode()));
            result.setResultCode(200);

           result.setMsg(new String[]{"success"});

        }

        return result;
    }

    @RequestMapping(value = "/getAllLatestReport")
    public QueryAllLatestReportResponse getAllLatestReport(@Validated QueryAllLatestReportCommand command, BindingResult bindingResult) throws RuntimeException {
        QueryAllLatestReportResponse result = new QueryAllLatestReportResponse();
        if (bindingResult.hasErrors()) {
            bindingResultFill(result, bindingResult);
        } else {
            result.setData(reportInfoService.queryAllDeviceLatestReport());
            result.setResultCode(200);
            result.setMsg(new String[]{"success"});
        }

        return result;
    }



    @RequestMapping(value = "/getAllDeviceLatestSim")
    public QueryAllDeviceLatestSimResponse getAllDeviceLatestSim(@Validated QueryAllDeviceLatestSimCommand command, BindingResult bindingResult) throws RuntimeException {
        QueryAllDeviceLatestSimResponse result = new QueryAllDeviceLatestSimResponse();
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
    public QueryLatestInfoByTypeResponse getAllDeviceLatestInfo(@Validated QueryAllDeviceLatestInfoByTypeCommand command, BindingResult bindingResult) throws RuntimeException {
        QueryLatestInfoByTypeResponse result = new QueryLatestInfoByTypeResponse();
        if (bindingResult.hasErrors()) {
            bindingResultFill(result, bindingResult);
        } else {
            result.setData(reportInfoService.queryAllDeviceLatestInfoByType(command.getType()));
            result.setResultCode(200);
            result.setMsg(new String[]{"success"});
        }

        return result;
    }





    @RequestMapping(value = "/getAllReportByDeviceCode")
    public QueryLatestReportByDeviceCodeResponse getAllReportByDeviceCode(@Validated queryAllReportByDeviceCodeBaseCommand command, BindingResult bindingResult) throws RuntimeException {
        QueryLatestReportByDeviceCodeResponse result = new QueryLatestReportByDeviceCodeResponse();
        if (bindingResult.hasErrors()) {
            bindingResultFill(result, bindingResult);
        } else {
            result.setData(reportInfoService.queryAllReportByDeviceCode(command.getDeviceCode()));
            result.setResultCode(200);

            result.setMsg(new String[]{"success"});

        }

        return result;
    }

    @RequestMapping(value = "/queryReportByDeviceAndTime")
    public QueryReportByDeviceCodeAndTimeResponse queryReportByDeviceAndTime(@Validated QueryReportByDeviceCodeAndTimeCommand command, BindingResult bindingResult) throws RuntimeException {
        QueryReportByDeviceCodeAndTimeResponse result = new QueryReportByDeviceCodeAndTimeResponse();
        if (bindingResult.hasErrors()) {
            bindingResultFill(result, bindingResult);
        } else {
            result.setData(reportInfoService.queryReportByDeviceCodeAndTime(command));
            result.setResultCode(200);

            result.setMsg(new String[]{"success"});

        }

        return result;
    }

}
