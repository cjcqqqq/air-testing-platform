package com.university.shenyang.air.testing.gateway.procotol.device;


import com.university.shenyang.air.testing.gateway.cache.DevicesManager;
import com.university.shenyang.air.testing.gateway.common.kit.Convert;
import com.university.shenyang.air.testing.gateway.common.kit.Packet;
import com.university.shenyang.air.testing.gateway.common.kit.lang.ArraysUtils;
import com.university.shenyang.air.testing.gateway.procotol.Command;
import com.university.shenyang.air.testing.model.DeviceInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by chenjc on 2017/05/03.
 */
public abstract class DeviceCommand extends Command {

    /**
     * 终端消息通用应答
     *
     * @param packet
     * @return
     */
    public Packet terminalMessageCommonAnswer(Packet packet) {
        Packet answer = new Packet();
        answer.setCommandId(packet.getCommandId());
        answer.setAnswerId(0x01);
        answer.setEncrypt(packet.getEncrypt());
        answer.setUniqueMark(packet.getUniqueMark());

        byte[] content = null;
        // 判断是否是终端校时指令
        if(packet.getCommandId() == 0x08){
            // 如果是终端校时指令应答内容为系统当前时间
            byte year = Byte.valueOf(new SimpleDateFormat("yy").format(Calendar.getInstance().getTime()));
            byte month = Byte.valueOf(new SimpleDateFormat("MM").format(Calendar.getInstance().getTime()));
            byte day = Byte.valueOf(new SimpleDateFormat("dd").format(Calendar.getInstance().getTime()));
            byte hour = Byte.valueOf(new SimpleDateFormat("HH").format(Calendar.getInstance().getTime()));
            byte minute = Byte.valueOf(new SimpleDateFormat("mm").format(Calendar.getInstance().getTime()));
            byte second = Byte.valueOf(new SimpleDateFormat("ss").format(Calendar.getInstance().getTime()));

            content = new byte[]{year, month, day, hour, minute, second};
        } else if(packet.getCommandId() == 0x01){
            content = new byte[14];
            // 终端请求指令时间
            ArraysUtils.arrayappend(content, 0, packet.getContent());

            // 系统当前时间
            byte year = Byte.valueOf(new SimpleDateFormat("yy").format(Calendar.getInstance().getTime()));
            byte month = Byte.valueOf(new SimpleDateFormat("MM").format(Calendar.getInstance().getTime()));
            byte day = Byte.valueOf(new SimpleDateFormat("dd").format(Calendar.getInstance().getTime()));
            byte hour = Byte.valueOf(new SimpleDateFormat("HH").format(Calendar.getInstance().getTime()));
            byte minute = Byte.valueOf(new SimpleDateFormat("mm").format(Calendar.getInstance().getTime()));
            byte second = Byte.valueOf(new SimpleDateFormat("ss").format(Calendar.getInstance().getTime()));

            byte[] systemTime =  new byte[]{year, month, day, hour, minute, second};
            ArraysUtils.arrayappend(content, 6, systemTime);

            // 设置采集周期
            DeviceInfo deviceInfo = DevicesManager.getInstance().getDeviceByCode(packet.getUniqueMark());
            ArraysUtils.arrayappend(content, 12, Convert.intTobytes(deviceInfo.getCollectInterval(), 2));
        } else {
            // 如果非终端校时指令应答内容为终端请求指令时间
            content = ArraysUtils.subarrays(packet.getContent(), 0, 6);
        }

        answer.setContent(content);
        return answer;
    }

}
