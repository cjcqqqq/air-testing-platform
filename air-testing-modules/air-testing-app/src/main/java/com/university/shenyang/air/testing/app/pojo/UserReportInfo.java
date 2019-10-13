package com.university.shenyang.air.testing.app.pojo;

import com.university.shenyang.air.testing.model.DayInfo;
import com.university.shenyang.air.testing.model.HourInfo;

import java.util.Date;
import java.util.List;

public class UserReportInfo {
    private Integer isOnline;

    private Date collectTime;

    private String formaldehyde;

    private String tvoc;

    private List<HourInfo> hourInfos;

    private List<DayInfo> dayinfos;

    public String getFormaldehyde() {
        return formaldehyde;
    }

    public void setFormaldehyde(String formaldehyde) {
        this.formaldehyde = formaldehyde;
    }

    public String getTvoc() {
        return tvoc;
    }

    public void setTvoc(String tvoc) {
        this.tvoc = tvoc;
    }

    public Integer getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Integer isOnline) {
        this.isOnline = isOnline;
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

    public List<HourInfo> getHourInfos() {
        return hourInfos;
    }

    public void setHourInfos(List<HourInfo> hourInfos) {
        this.hourInfos = hourInfos;
    }

    public List<DayInfo> getDayinfos() {
        return dayinfos;
    }

    public void setDayinfos(List<DayInfo> dayinfos) {
        this.dayinfos = dayinfos;
    }
}