package com.university.shenyang.air.testing.model;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by chenjc on 2017/05/03.
 * 设备上报信息
 */
public class ReportInfo implements Serializable {
    private String deviceCode; // 设备识别码17位 4个字节
    private Date collectTime; // 采集时间 6个字节
    private String sim; //sim卡号 20位 4个字节
    private Integer pm1_0; // pm1.0 单位:微克/立方米 整数 4个字节
    private Integer pm2_5; // pm2.5 单位:微克/立方米 整数 4个字节
    private Integer pm10;      // pm10  单位:微克/立方米 整数 4个字节
    private Integer formaldehyde; // 甲醛 0.00-9.99 * 100 2个字节
    private Integer temperature; // 温度 0-255 * 10 偏移500 0表示-50摄氏度 2个字节
    private Integer humidity; // 湿度 0-100 1个字节
    private Integer co; // 一氧化碳 预留4个字节
    private Integer co2; // 二氧化碳 预留4个字节
    private Integer no; // 一氧化氮 预留4个字节
    private Integer no2; // 二氧化氮 预留4个字节
    private Integer o3; // 臭氧 预留4个字节
    private Integer so2; // 二氧化硫 预留4个字节
    private Integer tvoc; // 有机气态物质 预留4个字节
    private Integer windSpeed; // 风速 预留4个字节
    private Integer windDirection; // 风向 预留2个字节
    private Long longitude; // 经度 以度为单位的经度值乘以10的6次方，精确到百万分之一度。 4个字节
    private Long latitude; // 纬度 以度为单位的纬度值乘以10的6次方，精确到百万分之一度。 4个字节
    private Integer electricity; // 太阳能电源电量0-100 1个字节

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getSim() {
        return sim;
    }

    public void setSim(String sim) {
        this.sim = sim;
    }

    public Integer getPm1_0() {
        return pm1_0;
    }

    public void setPm1_0(int pm1_0) {
        this.pm1_0 = pm1_0;
    }

    public Integer getPm2_5() {
        return pm2_5;
    }

    public void setPm2_5(int pm2_5) {
        this.pm2_5 = pm2_5;
    }

    public Integer getPm10() {
        return pm10;
    }

    public void setPm10(int pm10) {
        this.pm10 = pm10;
    }

    public Integer getFormaldehyde() {
        return formaldehyde;
    }

    public void setFormaldehyde(int formaldehyde) {
        this.formaldehyde = formaldehyde;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public Integer getCo() {
        return co;
    }

    public void setCo(int co) {
        this.co = co;
    }

    public Integer getCo2() {
        return co2;
    }

    public void setCo2(int co2) {
        this.co2 = co2;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public Integer getNo2() {
        return no2;
    }

    public void setNo2(int no2) {
        this.no2 = no2;
    }

    public Integer getO3() {
        return o3;
    }

    public void setO3(int o3) {
        this.o3 = o3;
    }

    public Integer getSo2() {
        return so2;
    }

    public void setSo2(int so2) {
        this.so2 = so2;
    }

    public Integer getTvoc() {
        return tvoc;
    }

    public void setTvoc(int tvoc) {
        this.tvoc = tvoc;
    }

    public Integer getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Integer getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(int windDirection) {
        this.windDirection = windDirection;
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

    public Integer getElectricity() {
        return electricity;
    }

    public void setElectricity(int electricity) {
        this.electricity = electricity;
    }

}
