package com.qf.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qf.dao.UserDao;
import com.qf.dao.impl.UserDaoImpl;
import com.qf.pojo.Pet;
import com.qf.pojo.ResultData;
import com.qf.pojo.User;
import com.qf.service.UserService;
import com.qf.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/26 10:16
 */
@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    UserService userService = new UserServiceImpl();

    //登陆验证
    public void selectOneByName(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求参数
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String rememberMe = req.getParameter("rememberMe");
        String vCode = req.getParameter("vCode");
        //从session中获取验证码
        String sCode = (String) req.getSession().getAttribute("text");
        //调用service的查询一条数据的方法
        User user = userService.selectOneByName(username);
        List<User> userList = new ArrayList<>();
        userList.add(user);
        //将结果集封装到ResultData中
        ResultData<User> resultData = new ResultData<>();
        if (sCode.equalsIgnoreCase(vCode)) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                if (rememberMe != null) {//表示勾选了记住我
                    //将用户名和密码存在Cookie中，并响应给前端
                    //创建Cookie对象，并保存用户名和密码
                    Cookie cookie = new Cookie("userMsg", username + ":" + password);
                    cookie.setPath(req.getContextPath());
                    cookie.setMaxAge(60 * 60 * 24);
                    resp.addCookie(cookie);
                }
                resultData.setCode(200);
                resultData.setData(userList);
                resultData.setMsg("登录成功");
                //将用户名和密码存到session中
                req.getSession().setAttribute("loginUser", user);
            } else {
                resultData.setCode(500);
                resultData.setMsg("登录失败");
            }
        } else {
            resultData.setCode(500);
            resultData.setMsg("登录失败");
        }

        //将resultData对象 转换成json字符串
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = mapper.writeValueAsString(resultData);
        //设置响应到前端的编码格式
        resp.setContentType("application/json;charset=utf-8");
        //将jsonstr响应到前端
        resp.getWriter().write(jsonStr);
    }

    //根据id查询一条数据
    public void one(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求参数
        String id = req.getParameter("id");
        //调用Service层查询单个的方法
        User user = userService.selectOneById(Integer.parseInt(id));
        List<User> userList = new ArrayList<>();
        userList.add(user);
        //将结果封装到ResultData中
        ResultData<User> resultData = new ResultData<>(200, "成功", 1, userList);
        //将resultData对象转换成json字符串
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(resultData);
        //设置响应的编码格式
        resp.setContentType("application/json;charset=utf-8");
        //将json数据响应给前端浏览器
        resp.getWriter().write(json);
    }

    //修改一条数据
    public void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userJson = req.getParameter("user");
        //将json字符串转换成Java对象
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(userJson, User.class);
        //调用service层的方法添加图书信息
        int count = userService.updateOne(user);
        //将结果封装到ResultData中
        ResultData<User> resultData = new ResultData<>();
        if (count > 0) {
            resultData.setCode(200);
            resultData.setMsg("修改成功");
        } else {
            resultData.setCode(500);
            resultData.setMsg("修改失败");
        }
        //将resultData对象转换成json字符串
        String json = mapper.writeValueAsString(resultData);
        //设置响应的编码格式
        resp.setContentType("application/json;charset=utf-8");
        //将json数据响应给前端浏览器
        resp.getWriter().write(json);
    }


}
