package com.university.shenyang.air.testing.app.response;

import com.university.shenyang.air.testing.common.pojo.UserInfo;
import com.university.shenyang.air.testing.common.response.BaseResponse;

/**
 * Created by chenjc on 2019/06/30.
 */
public class AppBaseResponse extends BaseResponse {
    public static final AppBaseResponse DEVICE_INVALID = new AppBaseResponse(2001, "Device invalid");

    public static final AppBaseResponse USER_DEVICE_HAS_BEEN_ADDED = new AppBaseResponse(2002, "User device has been added");

    public static final AppBaseResponse DEVICE_DESC_HAS_BEEN_USEED = new AppBaseResponse(2003, "Device description has been used");

    public static final AppBaseResponse DEVICE_IS_NOT_LOGIN = new AppBaseResponse(2004, "Device is not login");

    public AppBaseResponse() {
        super();
    }

    public AppBaseResponse(int code, String message) {
        super(code, message);
    }

    public static AppBaseResponse exception(Exception e) {
        return new AppBaseResponse(404, e.getMessage());
    }
}
