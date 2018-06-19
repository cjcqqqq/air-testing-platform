package com.university.shenyang.air.testing.monitoring.command;

import org.hibernate.validator.constraints.NotEmpty;

public class AddDeviceCommand extends BaseCommand {
    @NotEmpty(message = "deviceCode cannot be empty")
    private String deviceCode;
    @NotEmpty(message = "deviceDesc cannot be empty")
    private String deviceDesc;
    private String address;
    @NotEmpty(message = "longitude cannot be empty")
    private Float longitude;
    @NotEmpty(message = "latitude cannot be empty")
    private Float latitude;
    @NotEmpty(message = "sim cannot be empty")
    private String sim;
    @NotEmpty(message = "protocol cannot be empty")
    private String protocol;
    @NotEmpty(message = "collectInterval cannot be empty")
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

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
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
