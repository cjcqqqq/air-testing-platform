package com.university.shenyang.air.testing.monitoring.command;

import javax.validation.constraints.NotNull;

public class QueryDeviceByUserAndMinutesCommand extends BaseCommand {
    @NotNull(message = "username cannot be empty")
    private String username;
    @NotNull(message = "minutes cannot be empty")
    private String minutes;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }
}
