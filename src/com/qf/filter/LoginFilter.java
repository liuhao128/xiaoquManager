package com.qf.filter;

import com.qf.pojo.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 作者：SmallWood
 * 时间：2020/12/16 14:35
 **/
@WebFilter("/*")
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //强制转换
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        //获取访问路径
        String requestURI = req.getRequestURI();
        //判断放行的路径
        if (requestURI.contains("") || requestURI.contains("/register") || requestURI.contains("/verify") || requestURI.contains("/img") || (requestURI.contains("/js")&&!requestURI.contains("/jsp")) || requestURI.contains("/css")) {
            filterChain.doFilter(req,resp);
        }else {//表示拦截
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("loginUser");
            if (user == null) {//表示没登录
                resp.sendRedirect(req.getContextPath()+"/login.html");
            }else {
                filterChain.doFilter(req,resp);
            }
        }

    }
}
