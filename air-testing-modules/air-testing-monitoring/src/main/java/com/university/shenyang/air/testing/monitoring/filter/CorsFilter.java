package com.university.shenyang.air.testing.monitoring.filter;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * 跨域过滤器
 *
 * @author chenjc
 * @version
 * @since 2017年5月24日
 */
@Component
public class CorsFilter implements Filter
{
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException
    {
        HttpServletResponse response = (HttpServletResponse)res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Token");
        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig)
    {
    }

    @Override
    public void destroy()
    {
    }
}
