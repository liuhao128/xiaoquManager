package com.qf.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 作者：SmallWood
 * 时间：2020/12/25 15:55
 */
public class BaseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求参数，method
        String methodName = req.getParameter("method");
        if(methodName == null || "".equals(methodName)){
            resp.sendRedirect(req.getContextPath()+"/login.jsp");
            return ;
        }
        //获取子类的类对象
        Class<?> c = this.getClass();
        try {
            //利用反射调用子类的方法
            Method method = c.getMethod(methodName,HttpServletRequest.class,HttpServletResponse.class);
            Object returnValue = method.invoke(c.newInstance(),req,resp);
            if(returnValue != null){
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(returnValue);
                resp.setContentType("application/json;charset=utf-8");
                resp.getWriter().write(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }

    public <T> T convertBean(Class<T> c, Map<String, String[]> map ){
        try {
            T t = c.newInstance();
            //如果有日期类型,需要自定义日期类型转化器
            ConvertUtils.register(new Converter() {
                @Override
                public Object convert(Class aClass, Object o) {
                    //自定义转换规则  将字符串类型的日期转换成Date
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        return sdf.parse(o.toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }, Date.class);
            BeanUtils.populate(t,map);
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
