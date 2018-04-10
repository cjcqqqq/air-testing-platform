//package com.university.shenyang.air.testing.monitoring.filter;
//
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// *
// * 跨域过滤器
// *
// * @author zhangyu
// * @version
// * @since 2017年5月24日
// */
//public class CorsFilter extends OncePerRequestFilter {
//
//    static final String ORIGIN = "Origin";
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain filterChain) throws ServletException, IOException {
//
//        String origin = request.getHeader(ORIGIN);
//
//        response.setHeader("Access-Control-Allow-Origin", "*");//* or origin as u prefer
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.setHeader("Access-Control-Allow-Methods", "PUT, POST, GET, OPTIONS, DELETE");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "content-type, authorization");
//
//        if (request.getMethod().equals("OPTIONS")) {
//            response.setStatus(HttpServletResponse.SC_OK);
//        } else {
//            filterChain.doFilter(request, response);
//        }
//
//    }
//}