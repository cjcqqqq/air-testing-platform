package com.university.shenyang.air.testing.app.controller;

import com.university.shenyang.air.testing.app.command.*;
import com.university.shenyang.air.testing.app.response.*;
import com.university.shenyang.air.testing.app.redis.RedisClient;
import com.university.shenyang.air.testing.app.service.CUserInfoService;
import com.university.shenyang.air.testing.app.util.Constants;
import com.university.shenyang.air.testing.app.util.RandomValidateCodeUtil;
import com.university.shenyang.air.testing.common.annotation.IgnoreUserToken;
import com.university.shenyang.air.testing.common.context.BaseContextHandler;
import com.university.shenyang.air.testing.common.pojo.UserInfo;
import com.university.shenyang.air.testing.common.response.BaseResponse;
import com.university.shenyang.air.testing.common.util.IUserAuthUtil;
import com.university.shenyang.air.testing.model.CUserInfo;
import org.apache.tomcat.util.buf.HexUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.util.Random;

@RestController
@RequestMapping(value = "/app/user", method = {RequestMethod.GET, RequestMethod.POST}, produces = {"application/json;charset=UTF-8"})
public class UserController extends BaseController {
    /**
     * ReportController Logger
     */
    protected static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    CUserInfoService cUserInfoService;

    @Autowired
    private IUserAuthUtil userAuthUtil;

    @Autowired
    private RedisClient redisClient;

    @Value("${password.encryption.salt}")
    private String salt;

    @Value("${auth.user.limit-expire:3600}")
    private int limitExpire;

    /**
     * 检查用户名是否存在
     */
    @RequestMapping(value = "/checkUsername", method = RequestMethod.POST)
    @ResponseBody
    @IgnoreUserToken
    public BaseResponse checkUsername(@Validated CheckUsernameCommand checkUsernameCommand, BindingResult bindingResult) {
        BaseResponse result = new BaseResponse();
        try {
            if (bindingResult.hasErrors()) {
                bindingResultFill(result, bindingResult);
                return result;
            } else {
                CUserInfo cUserInfo = cUserInfoService.queryByUsername(checkUsernameCommand.getUsername());
                if (cUserInfo != null) {
                    return BaseResponse.USERNAME_ALREADY_EXISTS;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.exception(e);
        }

        return result;
    }

    /**
     * 生成验证码
     */
    @RequestMapping(value = "/getVerify")
    @IgnoreUserToken
    public void getVerify(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        try {
            response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            RandomValidateCodeUtil randomValidateCode = new RandomValidateCodeUtil();
            String randomString = randomCode("0123456789abcdefghijklmnopqrstuvwxyz", 4);

            if (username != null) {
                redisClient.set(Constants.VERIFYCODE_REDIS_KEY_PREFIX + username, randomString, 60);
            }

            randomValidateCode.getRandcode(request, response, randomString);//输出验证码图片方法
        } catch (Exception e) {
            logger.error("获取验证码失败>>>> ", e);
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    @IgnoreUserToken
    public BaseResponse register(@Validated AddCUserCommand addCUserCommand, BindingResult bindingResult) {
        BaseResponse result = new BaseResponse();
        try {
            if (bindingResult.hasErrors()) {
                bindingResultFill(result, bindingResult);
                return result;
            } else {
                String redisCode = redisClient.get(Constants.VERIFYCODE_REDIS_KEY_PREFIX + addCUserCommand.getUsername());
                if (!addCUserCommand.getVerifyCode().equals(redisCode)) {
                    return BaseResponse.VERIFY_CODE_INVALID;
                }
                AddCUserCommand record = new AddCUserCommand();
                record.setUsername(addCUserCommand.getUsername());
                record.setPassword(md5(addCUserCommand.getPassword() + salt));

                cUserInfoService.insertCUserInfo(record);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.exception(e);
        }

        return result;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    @IgnoreUserToken
    public BaseResponse login(@Validated CUserLoginCommand cUserLoginCommand, BindingResult bindingResult) {
        BaseResponse result = new BaseResponse();
        CUserInfo cUserInfo = null;
        try {
            if (bindingResult.hasErrors()) {
                bindingResultFill(result, bindingResult);
                return result;
            } else {
                //0. 验证验证码
                String redisCode = redisClient.get(Constants.VERIFYCODE_REDIS_KEY_PREFIX + cUserLoginCommand.getUsername());

                if (!cUserLoginCommand.getVerifyCode().equals(redisCode)) {
                    return BaseResponse.VERIFY_CODE_INVALID;
                }

                //1. 验证用户名密码
                cUserInfo = cUserInfoService.queryByUsername(cUserLoginCommand.getUsername());
                if (cUserInfo == null) {
                    return BaseResponse.USERNAME_PASSWORD_INVALID;
                }
                if (!cUserInfo.getPassword().equalsIgnoreCase(md5(cUserLoginCommand.getPassword() + salt))) {
                    return BaseResponse.USERNAME_PASSWORD_INVALID;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.exception(e);
        }

        //2. 生成token
        String token = genToken();

        //3. 缓存用户
        userAuthUtil.setUserInfo(token, toDTO(cUserInfo), limitExpire);
//        redisClient.set(token, cUserInfo, tokenTimeout);

        return new CUserLoginResponse(token);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse logout() {
        BaseResponse result = new BaseResponse();
        try {
            String token = BaseContextHandler.getToken();
            userAuthUtil.deleteInfoByToken(token);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.exception(e);
        }

        return result;
    }

    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse updatePassword(@Validated UpdatePasswordCommand updatePasswordCommand, BindingResult bindingResult) {
        BaseResponse result = new BaseResponse();
        try {
            if (bindingResult.hasErrors()) {
                bindingResultFill(result, bindingResult);
                return result;
            } else {
                //1. 验证用户名密码
                CUserInfo cUserInfo = cUserInfoService.queryByUsername(BaseContextHandler.getUsername());
                if (cUserInfo == null) {
                    return BaseResponse.USER_DOES_NOT_EXIST;
                }
                if (!cUserInfo.getPassword().equalsIgnoreCase(md5(updatePasswordCommand.getOldPassword() + salt))) {
                    return BaseResponse.USERNAME_PASSWORD_INVALID;
                }

                cUserInfoService.updatePassword(Long.parseLong(BaseContextHandler.getUserID()), md5(updatePasswordCommand.getNewPassword() + salt));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.exception(e);
        }

        return result;
    }

    @RequestMapping(value = "/authentication", method = RequestMethod.POST)
    @ResponseBody
//    @IgnoreUserToken
    public AuthenticationResponse authentication() {
        AuthenticationResponse result = new AuthenticationResponse();

        UserInfo userInfo = new UserInfo();
        userInfo.setId(Long.parseLong(BaseContextHandler.getUserID()));
        userInfo.setUsername(BaseContextHandler.getUsername());

        result.setUserInfo(userInfo);

        return result;
    }

    private String genToken() {
        return randomCode("0123456789abcdefghijklmnopqrstuvwxyz", 32);
    }

    private String randomCode(String s, int size) {
        StringBuilder result = new StringBuilder(size);

        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int loc = random.nextInt(s.length());
            result.append(s.charAt(loc));
        }
        return result.toString();
    }

    private String md5(String password) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5.digest(password.getBytes("utf-8"));
            return HexUtils.toHexString(md5Bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private UserInfo toDTO(CUserInfo cUserInfo) {
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(cUserInfo, userInfo);
        return userInfo;
    }
}
