package com.university.shenyang.air.testing.app.command;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class AuthenticationCommand extends BaseCommand {
    @NotEmpty(message = "token cannot be empty")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
