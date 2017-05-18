package com.university.shenyang.air.testing.gateway.controller;

import com.university.shenyang.air.testing.gateway.cache.DevicesManager;
import com.university.shenyang.air.testing.gateway.command.CollectIntervalSettingCommand;
import com.university.shenyang.air.testing.gateway.command.WifiSettingCommand;
import com.university.shenyang.air.testing.gateway.common.kit.Convert;
import com.university.shenyang.air.testing.gateway.common.kit.Packet;
import com.university.shenyang.air.testing.gateway.common.kit.lang.ArraysUtils;
import com.university.shenyang.air.testing.gateway.dto.CollectIntervalSettingDto;
import com.university.shenyang.air.testing.gateway.dto.WifiSettingDto;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


@RestController
@RequestMapping(value = "/device", method = {RequestMethod.GET, RequestMethod.POST}, produces = {"application/json;charset=UTF-8"})
public class DeviceController extends BaseController {
    /**
     * DeviceController Logger
     */
    protected static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

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
                packet.setUniqueMark(command.getDeviceCode());
                packet.setCommandId(0x80);
                packet.setAnswerId(0xfe);
                packet.setEncrypt(1);

                byte[] content = new byte[46];

                // 如果是终端校时指令应答内容为系统当前时间
                byte year = Byte.valueOf(new SimpleDateFormat("yy").format(Calendar.getInstance().getTime()));
                byte month = Byte.valueOf(new SimpleDateFormat("MM").format(Calendar.getInstance().getTime()));
                byte day = Byte.valueOf(new SimpleDateFormat("dd").format(Calendar.getInstance().getTime()));
                byte hour = Byte.valueOf(new SimpleDateFormat("HH").format(Calendar.getInstance().getTime()));
                byte minute = Byte.valueOf(new SimpleDateFormat("mm").format(Calendar.getInstance().getTime()));
                byte second = Byte.valueOf(new SimpleDateFormat("ss").format(Calendar.getInstance().getTime()));

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
    @RequestMapping(value = "/collectIntervalSetting")
    public CollectIntervalSettingDto wifiSetting(@Validated CollectIntervalSettingCommand command, BindingResult bindingResult) throws RuntimeException {
        CollectIntervalSettingDto result = new CollectIntervalSettingDto();

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
                packet.setUniqueMark(command.getDeviceCode());
                packet.setCommandId(0x81);
                packet.setAnswerId(0xfe);
                packet.setEncrypt(1);

                byte[] content = new byte[8];

                // 如果是终端校时指令应答内容为系统当前时间
                byte year = Byte.valueOf(new SimpleDateFormat("yy").format(Calendar.getInstance().getTime()));
                byte month = Byte.valueOf(new SimpleDateFormat("MM").format(Calendar.getInstance().getTime()));
                byte day = Byte.valueOf(new SimpleDateFormat("dd").format(Calendar.getInstance().getTime()));
                byte hour = Byte.valueOf(new SimpleDateFormat("HH").format(Calendar.getInstance().getTime()));
                byte minute = Byte.valueOf(new SimpleDateFormat("mm").format(Calendar.getInstance().getTime()));
                byte second = Byte.valueOf(new SimpleDateFormat("ss").format(Calendar.getInstance().getTime()));

                byte[] time = new byte[]{year, month, day, hour, minute, second};

                ArraysUtils.arrayappend(content, 0, time);
                ArraysUtils.arrayappend(content, 6, Convert.intTobytes(command.getInterval(), 2));

                packet.setContent(content);
                loginCtx.writeAndFlush(packet);

                result.setResultCode(200);
                result.setMsg(new String[]{"success"});
            }
        }

        return result;
    }
}
