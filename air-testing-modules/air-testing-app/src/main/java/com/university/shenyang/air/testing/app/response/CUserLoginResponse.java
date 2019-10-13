package com.university.shenyang.air.testing.app.response;

import com.university.shenyang.air.testing.common.response.BaseResponse;

/**
 * Created by cjcqqqq on 2017/5/17.
 */
public class CUserLoginResponse extends AppBaseResponse {
    private String token;

    public CUserLoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
