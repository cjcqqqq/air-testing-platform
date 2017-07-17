package com.university.shenyang.air.testing.monitoring.dto;

/**
 * Created by cjcqqqq on 2017/5/17.
 */
public class BaseDto {
    private int resultCode;
    private String[] msg;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String[] getMsg() {
        return msg;
    }

    public void setMsg(String[] msg) {
        this.msg = msg;
    }
}
