package com.university.shenyang.air.testing.app.command;

import org.hibernate.validator.constraints.NotEmpty;

public class DeleteUserDeviceCommand extends BaseCommand {
    @NotEmpty(message = "mappingId cannot be empty")
    private String mappingId;

    public String getMappingId() {
        return mappingId;
    }

    public void setMappingId(String mappingId) {
        this.mappingId = mappingId;
    }
}
