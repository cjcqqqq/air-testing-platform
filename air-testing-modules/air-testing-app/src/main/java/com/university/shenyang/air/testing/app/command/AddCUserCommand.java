package com.university.shenyang.air.testing.app.command;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class AddCUserCommand extends BaseCommand {
    @NotEmpty(message = "username cannot be empty")
    @Pattern(regexp = "[a-zA-Z0-9]{6,20}", message = "username must be a number or letter from 6 to 20 digits")
    private String username;
    @NotEmpty(message = "password cannot be empty")
    @Pattern(regexp = ".{6,20}", message = "password must be 6 to 20 digits.")
    private String password;
    @NotEmpty(message = "verifyCode cannot be empty")
    private String verifyCode;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
