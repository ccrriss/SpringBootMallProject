package com.chrisjia.mall.filter;

import com.chrisjia.mall.common.Constant;
import com.chrisjia.mall.exception.ExceptionEnum;
import com.chrisjia.mall.model.pojo.User;
import com.chrisjia.mall.service.UserService;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class UserFilter implements Filter {
    @Resource
    private UserService userService;

    public static User currentUser;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest)servletRequest).getSession();
        currentUser = (User)session.getAttribute(Constant.USER);
        servletResponse.setContentType("application/json;charset=UTF-8");
        if(currentUser == null) {
            HttpServletResponse res = (HttpServletResponse)servletResponse;
            res.setContentType("application/json");
            PrintWriter out = res.getWriter();
            out.write(ExceptionEnum.USER_HAS_NOT_LOGIN_EXCEPTION.toString());
            out.flush();
            out.close();
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
