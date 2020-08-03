/*
 *  Copyright (C) 2018  Wanghaobin<463540703@qq.com>

 *  AG-Enterprise 企业版源码
 *  郑重声明:
 *  如果你从其他途径获取到，请告知老A传播人，奖励1000。
 *  老A将追究授予人和传播人的法律责任!

 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.

 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.university.shenyang.air.testing.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.university.shenyang.air.testing.common.annotation.IgnoreUserToken;
import com.university.shenyang.air.testing.common.configuration.UserAuthConfig;
import com.university.shenyang.air.testing.common.context.BaseContextHandler;
import com.university.shenyang.air.testing.common.exception.auth.UserInvalidException;
import com.university.shenyang.air.testing.common.pojo.UserInfo;
import com.university.shenyang.air.testing.common.response.BaseResponse;
import com.university.shenyang.air.testing.common.exception.auth.NonLoginException;
import com.university.shenyang.air.testing.common.util.IUserAuthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户token拦截认证
 *
 * @author chenjincheng
 * @version 2017/9/10
 */
public class UserAuthRestInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = LoggerFactory.getLogger(UserAuthRestInterceptor.class);

    @Autowired
    private IUserAuthUtil userAuthUtil;

    @Autowired
    private UserAuthConfig userAuthConfig;

    @Value("${auth.user.limit-expire:3600}")
    private int limitExpire;

    @Value("${userauth.whitelist.uris}")
    private List<String> whiteList;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        if(whiteList != null && whiteList.contains(request.getRequestURI())){
            return true;
        }

        // 配置该注解，说明不进行用户拦截
        IgnoreUserToken ignoreUserToken = handlerMethod.getMethodAnnotation(IgnoreUserToken.class);
        if (ignoreUserToken != null) {
            return super.preHandle(request, response, handler);
        } else {
            String token = request.getHeader(userAuthConfig.getTokenHeader());
            if (StringUtils.isEmpty(token)) {
                if (request.getCookies() != null) {
                    for (Cookie cookie : request.getCookies()) {
                        if (cookie.getName().equals(userAuthConfig.getTokenHeader())) {
                            token = cookie.getValue();
                        }
                    }
                }
            }

            try {
                if (token == null || StringUtils.isEmpty(token)) {
                    throw new NonLoginException("User token is empty!");
                }
                UserInfo userInfo = userAuthUtil.getInfoFromToken(token);
                BaseContextHandler.setToken(token);
                BaseContextHandler.setUsername(userInfo.getUsername());
                BaseContextHandler.setUserID(String.valueOf(userInfo.getId()));

                userAuthUtil.setUserInfo(token, userInfo, limitExpire);
            }catch(NonLoginException ex){
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
//                logger.error(ex.getMessage(),ex);
                response.setContentType("UTF-8");
                response.getOutputStream().println(JSON.toJSONString(new BaseResponse(ex.getStatus(), ex.getMessage())));
                return false;
            }

            return super.preHandle(request, response, handler);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        BaseContextHandler.remove();
        super.afterCompletion(request, response, handler, ex);
    }
}
