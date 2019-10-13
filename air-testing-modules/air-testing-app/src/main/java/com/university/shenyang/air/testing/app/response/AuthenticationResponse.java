package com.university.shenyang.air.testing.app.response;

import com.university.shenyang.air.testing.common.pojo.UserInfo;
import com.university.shenyang.air.testing.common.response.BaseResponse;

/**
 * Created by chenjc on 2019/06/30.
 */
public class AuthenticationResponse extends AppBaseResponse {
    private UserInfo userInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
