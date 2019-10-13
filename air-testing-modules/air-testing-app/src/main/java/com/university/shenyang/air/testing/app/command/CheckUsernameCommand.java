package com.university.shenyang.air.testing.app.command;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class CheckUsernameCommand extends BaseCommand {
    @NotEmpty(message = "username cannot be empty")
    @Pattern(regexp = "[a-zA-Z0-9]{6,20}", message = "username must be a number or letter from 6 to 20 digits")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
