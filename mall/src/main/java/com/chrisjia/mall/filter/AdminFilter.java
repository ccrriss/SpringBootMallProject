package com.chrisjia.mall.filter;

import com.chrisjia.mall.common.ApiRestResponse;
import com.chrisjia.mall.common.Constant;
import com.chrisjia.mall.exception.ExceptionClass;
import com.chrisjia.mall.exception.ExceptionEnum;
import com.chrisjia.mall.model.pojo.User;
import com.chrisjia.mall.service.UserService;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

public class AdminFilter implements Filter {
    @Resource
    private UserService userService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest)servletRequest).getSession();
        User user = (User)session.getAttribute(Constant.USER);
        // User has not login yet
        if(user == null){
            HttpServletResponse res = (HttpServletResponse)servletResponse;
            res.setContentType("application/json");
            PrintWriter out = res.getWriter();
            out.write(ExceptionEnum.USER_HAS_NOT_LOGIN_EXCEPTION.toString());
            out.flush();
            out.close();
            return;
        }
        // User is not admin or not
        if(userService.checkAdminPermission(user)){
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            HttpServletResponse res = (HttpServletResponse)servletResponse;
            res.setContentType("application/json");
            PrintWriter out = res.getWriter();
            out.write(ExceptionEnum.USER_LOGIN_NO_ADMIN_PERMISSION.toString());
            out.flush();
            out.close();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
