package com.university.shenyang.air.testing.model;

import java.io.Serializable;

/**
 * Created by chenjc on 2017/05/03.
 * 设备信息
 */
public class DeviceInfo implements  Serializable{
    private Long id; //设备id
    private String deviceCode; //设备标识码
    private Long longitude; // 经度 以度为单位的经度值乘以10的6次方，精确到百万分之一度。 一代设备使用
    private Long latitude; // 纬度 以度为单位的纬度值乘以10的6次方，精确到百万分之一度。 一代设备使用
    private String sim; //sim卡号
    private String protocol;//协议类型，对应字典里面的编码。
    private String remoteAddress; //无连接的时候为"";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public Long getLongitude() {
        return longitude;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    public Long getLatitude() {
        return latitude;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public String getSim() {
        return sim;
    }

    public void setSim(String sim) {
        this.sim = sim;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }
}
