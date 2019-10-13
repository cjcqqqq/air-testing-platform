package com.university.shenyang.air.testing.app.controller;

import com.university.shenyang.air.testing.common.response.BaseResponse;
import org.springframework.validation.BindingResult;

public class BaseController {

    public void bindingResultFill(BaseResponse dto, BindingResult bindingResult){
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
