package com.university.shenyang.air.testing.gateway.cache;

import com.university.shenyang.air.testing.model.DeviceInfo;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chenjc on 2017/05/03.
 */
public class DevicesManager {
    private static DevicesManager instance = new DevicesManager();

    private DevicesManager() {
    }

    public static DevicesManager getInstance() {
        return instance;
    }

//    /**
//     * key:remoteAddress
//     * value:ChannelHandlerContext,channel和handler的上下文，性能最优
//     */
//    private static Map<String, ChannelHandlerContext> channels = new ConcurrentHashMap<>();

    /**
     * key:deviceCode
     * value:ChannelHandlerContext,channel和handler的上下文，性能最优
     */
    private static Map<String, ChannelHandlerContext> deviceCodeMappingChannel = new ConcurrentHashMap<>();


    /**
     * key:设备ID
     * value ：DeviceInfo
     */
    private static Map<Long, DeviceInfo> idMappingDevice = new ConcurrentHashMap<>();

    /**
     * key:设备标识码
     * value ：DeviceInfo
     */
    private static Map<String, DeviceInfo> codeMappingDevice = new ConcurrentHashMap<>();

    /**
     * key:channel的remoteAddress，设备链路地址
     * value：设备标识码
     */
    private static Map<String, String> channelMappingDeviceCode = new ConcurrentHashMap<>();

//    public void addChannel(String remoteAddress, ChannelHandlerContext ctx) {
//        channels.put(remoteAddress, ctx);
//    }
//
//    public ChannelHandlerContext getCtxByRemoteAddress(String remoteAddress) {
//        return channels.get(remoteAddress);
//    }
//
//    // TODO
//    public void removeChannel(String remoteAddress) {
//        channels.remove(remoteAddress);
//    }


    public void addDeviceCodeMappingChannel(String deviceCode, ChannelHandlerContext ctx) {
        deviceCodeMappingChannel.put(deviceCode, ctx);
    }

    public ChannelHandlerContext getCtxByDeviceCode(String deviceCode) {
        return deviceCodeMappingChannel.get(deviceCode);
    }

    // TODO
    public void removeDeviceCodeMappingChannel(String deviceCode) {
        deviceCodeMappingChannel.remove(deviceCode);
    }

    public void addIdMappingDevice(long id, DeviceInfo deviceInfo) {
        idMappingDevice.put(id, deviceInfo);
    }

    public DeviceInfo getDeviceById(long id) {
        return idMappingDevice.get(id);
    }

    // TODO
    public void removeDeviceById(long id) {
        idMappingDevice.remove(id);
    }

    public void addCodeMappingDevice(String code, DeviceInfo deviceInfo) {
        codeMappingDevice.put(code, deviceInfo);
    }

    public DeviceInfo getDeviceByCode(String code) {
        return codeMappingDevice.get(code);
    }

    // TODO
    public void removeDeviceByyCode(String code) {
        codeMappingDevice.remove(code);
    }

    public void addChannelMappingDeviceCode(String remoteAddress, String deviceCode) {
        channelMappingDeviceCode.put(remoteAddress, deviceCode);
    }

    public String getDeviceCodeByRemoteAddress(String remoteAddress) {
        if (channelMappingDeviceCode.get(remoteAddress) != null) {
            return channelMappingDeviceCode.get(remoteAddress);
        }
        return null;
    }

    public void removeChannelMappingDeviceCode(String remoteAddress) {
        channelMappingDeviceCode.remove(remoteAddress);
    }


    /**
     * 根据设备识别码查询ID
     *
     * @param deviceCode 设备识别码
     * @return ID，失败返回-1
     */
    public long getIdByDeviceCode(String deviceCode) {
        for (Map.Entry<Long, DeviceInfo> entity : idMappingDevice.entrySet()) {
            DeviceInfo device = entity.getValue();
            if (device != null && device.getDeviceCode().equals(deviceCode)) {
                return device.getId();
            }
        }
        return -1;
    }

}
