package com.university.shenyang.air.testing.common.response;

import java.io.Serializable;

/**
 * Created by cjcqqqq on 2017/5/17.
 */
public class BaseResponse implements Serializable {
    public static final BaseResponse USERNAME_ALREADY_EXISTS = new BaseResponse(1001, "username already exists");

    public static final BaseResponse VERIFY_CODE_INVALID = new BaseResponse(1002, "verifyCode invalid");

    public static final BaseResponse USERNAME_PASSWORD_INVALID = new BaseResponse(1003, "username or password invalid");

    public static final BaseResponse USER_DOES_NOT_EXIST = new BaseResponse(1004, "User does not exist");


    // 用户token异常
    public static final Integer EX_USER_INVALID_CODE = 40101;
    public static final Integer EX_USER_PASS_INVALID_CODE = 40001;
    public static final Integer EX_USER_FORBIDDEN_CODE = 40131;
    // 客户端token异常
    public static final Integer EX_CLIENT_INVALID_CODE = 40301;
    public static final Integer EX_CLIENT_FORBIDDEN_CODE = 40331;
    // 业务异常返回码
    public static final Integer EX_BUSINESS_BASE_CODE = 30101;

    public static final BaseResponse SUCCESS = new BaseResponse();

    private int resultCode;
    private String[] msg;

    public BaseResponse() {
        this.resultCode = 200;
        this.msg = new String[]{"success"};
    }

    public BaseResponse(int code, String message) {
        this.resultCode = code;
        this.msg = new String[]{message};
    }

    public static BaseResponse exception(Exception e) {
        return new BaseResponse(404, e.getMessage());
    }

    public int getResultCode() {
        return resultCode;
    }

     public void setResultCode(int resultCode) {
       this.resultCode = resultCode;
   }
    public String[] getMsg() {
     return msg;
   }

   public void setMsg(String[] msg) {
       this.msg = msg;
    }

}
