package com.university.shenyang.air.testing.monitoring.pojo;

public class ReportTypeInfo {
    private String type;  // 空气指标类别 1：温度 2：pm2.5 。。。

    private String typeValue;

    private Float longitude;

    private Float latitude;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
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
}