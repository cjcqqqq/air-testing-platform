package com.university.shenyang.air.testing.app.command;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UpdatePasswordCommand extends BaseCommand {
    @NotEmpty(message = "oldPassword cannot be empty")
    private String oldPassword;

    @NotEmpty(message = "newPassword cannot be empty")
    @Pattern(regexp = ".{6,20}", message = "newPassword must be 6 to 20 digits.")
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
