package com.university.shenyang.air.testing.gateway.controller;

import com.university.shenyang.air.testing.gateway.command.BaseCommand;
import com.university.shenyang.air.testing.gateway.dto.BaseDto;
import org.springframework.validation.BindingResult;

public class BaseController {

    public void bindingResultFill(BaseDto dto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            dto.setResultCode(400);
            String[] msg = new String[bindingResult.getErrorCount()];
            for (int i = 0; i < msg.length; i++) {
                msg[i] = bindingResult.getAllErrors().get(i).getDefaultMessage();
            }
            dto.setMsg(msg);
        }
    }
}
