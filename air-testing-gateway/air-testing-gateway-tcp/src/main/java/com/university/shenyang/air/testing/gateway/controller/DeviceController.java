package com.university.shenyang.air.testing.gateway.controller;

import com.university.shenyang.air.testing.gateway.cache.DevicesManager;
import com.university.shenyang.air.testing.gateway.common.kit.Packet;
import com.university.shenyang.air.testing.gateway.common.kit.lang.ArraysUtils;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


@RestController
@RequestMapping(value = "/Device", method = {RequestMethod.GET, RequestMethod.POST}, produces = {"application/json;charset=UTF-8"})
public class DeviceController {
    /**
     * DeviceController Logger
     */
    protected static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    /**
     *
     * @param account
     * @param password
     * @return
     * @throws RuntimeException
     */
    @RequestMapping(value = "/add")
    public String add(@RequestParam String deviceCode, @RequestParam String account, @RequestParam String password) throws RuntimeException {
        if(deviceCode == null || "".equals(deviceCode)){
            return "deviceCode cannot be empty";
        }
        if(account == null || "".equals(account) || password == null || "".equals(password)){
            return "Account and password cannot be empty";
        }

        // 获取登入链路上下文
        ChannelHandlerContext loginCtx = DevicesManager.getInstance().getCtxByDeviceCode(deviceCode);
        if(loginCtx == null){
            return "Device are not login";
        }else {
            Packet packet = new Packet();
            packet.setUniqueMark(deviceCode);
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
                ArraysUtils.arrayappend(content, 6, ArraysUtils.fillArrayTail(account.getBytes("ASCII"), 20, (byte) 0x00));
                ArraysUtils.arrayappend(content, 26, ArraysUtils.fillArrayTail(password.getBytes("ASCII"), 20, (byte) 0x00));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            packet.setContent(content);
            loginCtx.writeAndFlush(packet);
        }
        return "success";
    }
}
