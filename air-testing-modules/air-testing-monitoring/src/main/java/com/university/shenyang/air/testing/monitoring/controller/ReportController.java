package com.university.shenyang.air.testing.monitoring.controller;

import com.sun.javafx.collections.MappingChange;
import com.university.shenyang.air.testing.monitoring.command.QueryAllDeviceLatestInfoByTypeCommand;
import com.university.shenyang.air.testing.monitoring.command.QueryAllDeviceLatestSimCommand;
import com.university.shenyang.air.testing.monitoring.command.QueryLatestReportCommand;
import com.university.shenyang.air.testing.monitoring.dto.QueryAllDeviceLatestSimDto;
import com.university.shenyang.air.testing.monitoring.dto.QueryLatestInfoByTypeDto;
import com.university.shenyang.air.testing.monitoring.dto.QueryLatestReportDto;
import com.university.shenyang.air.testing.monitoring.service.ReportInfoService;
// import  com.university.shenyang.air.testing.monitoring.filter.CorsFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
//import javax.servlet.http.HttpServletResponse;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Filter;

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
    public QueryLatestReportDto getLatestReport(@Validated QueryLatestReportCommand command, BindingResult bindingResult  , HttpServletResponse response ) throws RuntimeException {
        QueryLatestReportDto result = new QueryLatestReportDto();
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

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
    public QueryAllDeviceLatestSimDto getAllDeviceLatestSim(@Validated QueryAllDeviceLatestSimCommand command, BindingResult bindingResult , HttpServletRequest rep, HttpServletResponse res, FilterChain chain) throws RuntimeException, IOException, ServletException {
//        CorsFilter corsFilter = new CorsFilter();
//        corsFilter.doFilter(rep,res,chain);


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
