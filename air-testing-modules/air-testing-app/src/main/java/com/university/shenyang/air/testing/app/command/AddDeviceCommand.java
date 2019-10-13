package com.university.shenyang.air.testing.app.command;

import javax.validation.constraints.NotNull;

public class AddDeviceCommand extends BaseCommand {
    @NotNull(message = "deviceCode cannot be empty")
    private String deviceCode;
    @NotNull(message = "deviceDesc cannot be empty")
    private String deviceDesc;
    private String address;
    @NotNull(message = "longitude cannot be empty")
    private float longitude;
    @NotNull(message = "latitude cannot be empty")
    private float latitude;
    @NotNull(message = "sim cannot be empty")
    private String sim;
    @NotNull(message = "protocol cannot be empty")
    private String protocol;
    @NotNull(message = "collectInterval cannot be empty")
    private int collectInterval;

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getDeviceDesc() {
        return deviceDesc;
    }

    public void setDeviceDesc(String deviceDesc) {
        this.deviceDesc = deviceDesc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
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

    public int getCollectInterval() {
        return collectInterval;
    }

    public void setCollectInterval(int collectInterval) {
        this.collectInterval = collectInterval;
    }
}
