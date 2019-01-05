package com.university.shenyang.air.testing.collector.controller;

import com.university.shenyang.air.testing.collector.cache.DevicesManager;
import com.university.shenyang.air.testing.collector.command.CollectIntervalSettingCommand;
import com.university.shenyang.air.testing.collector.command.ParamQueryCommand;
import com.university.shenyang.air.testing.collector.command.WifiSettingCommand;
import com.university.shenyang.air.testing.collector.common.kit.Convert;
import com.university.shenyang.air.testing.collector.common.kit.Packet;
import com.university.shenyang.air.testing.collector.common.kit.lang.ArraysUtils;
import com.university.shenyang.air.testing.collector.dto.CollectIntervalSettingDto;
import com.university.shenyang.air.testing.collector.dto.ParamQueryDto;
import com.university.shenyang.air.testing.collector.dto.WifiSettingDto;
import com.university.shenyang.air.testing.collector.pojo.DeviceParam;
import com.university.shenyang.air.testing.collector.service.CommandSendLogService;
import com.university.shenyang.air.testing.collector.service.DeviceInfoService;
import com.university.shenyang.air.testing.collector.util.Constants;
import com.university.shenyang.air.testing.model.CommandSendLog;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


@RestController
@RequestMapping(value = "/device", method = {RequestMethod.GET, RequestMethod.POST}, produces = {"application/json;charset=UTF-8"})
public class DeviceController extends BaseController {
    /**
     * DeviceController Logger
     */
    protected static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    CommandSendLogService commandSendLogService;

    @Autowired
    DeviceInfoService deviceInfoService;

    @Autowired
    public RedisTemplate<String, DeviceParam> redisTemplate;


    /**
     * 设置设备Wifi
     *
     * @param command
     * @param bindingResult
     * @return
     * @throws RuntimeException
     */
    @RequestMapping(value = "/wifiSetting")
    public WifiSettingDto wifiSetting(@Validated WifiSettingCommand command, BindingResult bindingResult) throws RuntimeException {
        WifiSettingDto result = new WifiSettingDto();
        if (bindingResult.hasErrors()) {
            bindingResultFill(result, bindingResult);
        } else {
            // 获取登入链路上下文
            ChannelHandlerContext loginCtx = DevicesManager.getInstance().getCtxByDeviceCode(command.getDeviceCode());
            if (loginCtx == null) {
                result.setResultCode(400);
                result.setMsg(new String[]{"Device are not login"});
            } else {
                Packet packet = new Packet();
                packet.setUniqueMark(command.getDeviceCode().trim());
                packet.setCommandId(0x80);
                packet.setAnswerId(0xfe);
                packet.setEncrypt(1);

                byte[] content = new byte[46];

                Date nowTime = Calendar.getInstance().getTime();
                // 如果是终端校时指令应答内容为系统当前时间
                byte year = Byte.valueOf(new SimpleDateFormat("yy").format(nowTime));
                byte month = Byte.valueOf(new SimpleDateFormat("MM").format(nowTime));
                byte day = Byte.valueOf(new SimpleDateFormat("dd").format(nowTime));
                byte hour = Byte.valueOf(new SimpleDateFormat("HH").format(nowTime));
                byte minute = Byte.valueOf(new SimpleDateFormat("mm").format(nowTime));
                byte second = Byte.valueOf(new SimpleDateFormat("ss").format(nowTime));

                byte[] time = new byte[]{year, month, day, hour, minute, second};

                ArraysUtils.arrayappend(content, 0, time);
                try {
                    ArraysUtils.arrayappend(content, 6, ArraysUtils.fillArrayTail(command.getAccount().getBytes("ASCII"), 20, (byte) 0x00));
                    ArraysUtils.arrayappend(content, 26, ArraysUtils.fillArrayTail(command.getPassword().getBytes("ASCII"), 20, (byte) 0x00));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                packet.setContent(content);
                loginCtx.writeAndFlush(packet);
                CommandSendLog record = new CommandSendLog();
                record.setCommandId(0x80);
                record.setDeviceCode(command.getDeviceCode());
                record.setSendtime(nowTime);
                record.setUpdatetime(nowTime);
                record.setCommandContent(Convert.bytesToHexString(packet.getContent()));
                record.setCommandStatus(0);
                commandSendLogService.insert(record);

                result.setResultCode(200);
                result.setMsg(new String[]{"success"});
            }
        }

        return result;
    }

    /**
     * 设置设备采集间隔
     *
     * @param command
     * @param bindingResult
     * @return
     * @throws RuntimeException
     */
//    @RequestMapping(value = "/collectIntervalSetting")
//    public CollectIntervalSettingDto wifiSetting(@Validated CollectIntervalSettingCommand command, BindingResult bindingResult) throws RuntimeException {
//        CollectIntervalSettingDto result = new CollectIntervalSettingDto();
//
//        if (bindingResult.hasErrors()) {
//            bindingResultFill(result, bindingResult);
//        } else {
//            // 获取登入链路上下文
//            ChannelHandlerContext loginCtx = DevicesManager.getInstance().getCtxByDeviceCode(command.getDeviceCode());
//            if (loginCtx == null) {
//                result.setResultCode(400);
//                result.setMsg(new String[]{"Device are not login"});
//            } else {
//                Packet packet = new Packet();
//                packet.setUniqueMark(command.getDeviceCode().trim());
//                packet.setCommandId(0x81);
//                packet.setAnswerId(0xfe);
//                packet.setEncrypt(1);
//
//                byte[] content = new byte[8];
//
//                Date nowTime = Calendar.getInstance().getTime();
//                // 如果是终端校时指令应答内容为系统当前时间
//                byte year = Byte.valueOf(new SimpleDateFormat("yy").format(nowTime));
//                byte month = Byte.valueOf(new SimpleDateFormat("MM").format(nowTime));
//                byte day = Byte.valueOf(new SimpleDateFormat("dd").format(nowTime));
//                byte hour = Byte.valueOf(new SimpleDateFormat("HH").format(nowTime));
//                byte minute = Byte.valueOf(new SimpleDateFormat("mm").format(nowTime));
//                byte second = Byte.valueOf(new SimpleDateFormat("ss").format(nowTime));
//
//                byte[] time = new byte[]{year, month, day, hour, minute, second};
//
//                ArraysUtils.arrayappend(content, 0, time);
//                ArraysUtils.arrayappend(content, 6, Convert.intTobytes(command.getInterval(), 2));
//
//                packet.setContent(content);
//                loginCtx.writeAndFlush(packet);
//
//                CommandSendLog record = new CommandSendLog();
//                record.setCommandId(0x81);
//                record.setDeviceCode(command.getDeviceCode());
//                record.setSendtime(nowTime);
//                record.setUpdatetime(nowTime);
//                record.setCommandContent(Convert.bytesToHexString(packet.getContent()));
//                record.setCommandStatus(0);
//                commandSendLogService.insert(record);
//
//                result.setResultCode(200);
//                result.setMsg(new String[]{"success"});
//            }
//        }
    /**
     * 设置设备采集间隔
     *
     * @param command
     * @param bindingResult
     * @return
     * @throws RuntimeException
     */
    @RequestMapping(value = "/collectIntervalSetting")
    public CollectIntervalSettingDto collectIntervalSetting(@Validated CollectIntervalSettingCommand command, BindingResult bindingResult) throws RuntimeException {
        CollectIntervalSettingDto result = new CollectIntervalSettingDto();

        if (bindingResult.hasErrors()) {
            bindingResultFill(result, bindingResult);
        } else {
            int updateResult = deviceInfoService.updateDeviceCollectInterval(command.getDeviceCode(), command.getInterval());

            if(updateResult > 0){
                // 获取登入链路上下文
                ChannelHandlerContext loginCtx = DevicesManager.getInstance().getCtxByDeviceCode(command.getDeviceCode());
                if (loginCtx != null) {
                    loginCtx.close();
                }
            }
            result.setResultCode(200);
            result.setMsg(new String[]{"success"});
        }

        return result;
    }

    /**
     * 设置设备采集间隔
     *
     * @param command
     * @param bindingResult
     * @return
     * @throws RuntimeException
     */
    @RequestMapping(value = "/paramQuery")
    public ParamQueryDto paramQuery(@Validated ParamQueryCommand command, BindingResult bindingResult) throws RuntimeException {
        ParamQueryDto result = new ParamQueryDto();

        if (bindingResult.hasErrors()) {
            bindingResultFill(result, bindingResult);
        } else {
            // 获取登入链路上下文
            ChannelHandlerContext loginCtx = DevicesManager.getInstance().getCtxByDeviceCode(command.getDeviceCode());
            if (loginCtx == null) {
                result.setResultCode(400);
                result.setMsg(new String[]{"Device are not login"});
            } else {
                Packet packet = new Packet();
                packet.setUniqueMark(command.getDeviceCode().trim());
                packet.setCommandId(0x8f);
                packet.setAnswerId(0xfe);
                packet.setEncrypt(1);

                byte[] content = new byte[6];

                Date nowTime = Calendar.getInstance().getTime();
                // 如果是终端校时指令应答内容为系统当前时间
                byte year = Byte.valueOf(new SimpleDateFormat("yy").format(nowTime));
                byte month = Byte.valueOf(new SimpleDateFormat("MM").format(nowTime));
                byte day = Byte.valueOf(new SimpleDateFormat("dd").format(nowTime));
                byte hour = Byte.valueOf(new SimpleDateFormat("HH").format(nowTime));
                byte minute = Byte.valueOf(new SimpleDateFormat("mm").format(nowTime));
                byte second = Byte.valueOf(new SimpleDateFormat("ss").format(nowTime));

                byte[] time = new byte[]{year, month, day, hour, minute, second};

                ArraysUtils.arrayappend(content, 0, time);

                packet.setContent(content);
                loginCtx.writeAndFlush(packet);

                CommandSendLog record = new CommandSendLog();
                record.setCommandId(0x8f);
                record.setDeviceCode(command.getDeviceCode());
                record.setSendtime(nowTime);
                record.setUpdatetime(nowTime);
                record.setCommandContent(Convert.bytesToHexString(packet.getContent()));
                record.setCommandStatus(0);
                commandSendLogService.insert(record);

                DeviceParam deviceParam = null;
                for (int i = 0; i < 50; i++) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    deviceParam = redisTemplate.opsForValue().get(Constants.PARAM_REDIS_KEY_PREFIX + command.getDeviceCode() + "_" + Convert.bytesToDateString(time));
                    if (deviceParam != null) {
                        result.setResultCode(200);
                        result.setData(deviceParam);
                        result.setMsg(new String[]{"success"});
                        break;
                    }
                }

                if (deviceParam == null) {
                    result.setResultCode(201);
                    result.setMsg(new String[]{"device no response"});
                }
            }
        }

        return result;
    }
}
