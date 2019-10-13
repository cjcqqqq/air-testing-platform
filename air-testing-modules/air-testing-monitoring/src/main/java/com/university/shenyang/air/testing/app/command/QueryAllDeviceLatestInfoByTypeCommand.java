package com.university.shenyang.air.testing.app.command;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by cjcqqqq on 2017/5/17.
 */
public class QueryAllDeviceLatestInfoByTypeCommand extends BaseCommand {
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @NotEmpty(message = "type cannot be empty")
    private String type;  // 空气指标类别 1：温度 2：pm2.5 。。。

}
