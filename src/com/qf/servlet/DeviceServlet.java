package com.qf.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qf.pojo.Building;
import com.qf.pojo.Device;
import com.qf.pojo.PageBean;
import com.qf.pojo.ResultData;
import com.qf.service.DeviceService;
import com.qf.service.impl.DeviceServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/30 9:35
 */
@WebServlet("/device/*")
@MultipartConfig(maxFileSize = 2*1024*1024 , maxRequestSize = 10 * 1024 * 1024)
public class DeviceServlet extends BaseServlet{
    DeviceService deviceService = new DeviceServiceImpl();
    //查询全部
    public void selectAll (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取搜索关键字参数
        String keywords = req.getParameter("keywords");
        //获取分页参数
        String page = req.getParameter("page");
        String limit = req.getParameter("limit");
        //调用service层的方法查询所有的图书信息
        PageBean<Device> pageBean = deviceService.selectByLimit(keywords,page, limit);
        //将结果集封装到ResultData中
        ResultData<Device> resultData = new ResultData<>(0,"成功",pageBean.getTotal(),pageBean.getList());
        //将resultData对象转换成json字符串
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(resultData);
        //设置响应的编码格式
        resp.setContentType("application/json;charset=utf-8");
        //将json数据相应给前端浏览器
        resp.getWriter().write(json);
    }

    //添加一条数据
    public void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String deviceJson = req.getParameter("device");
        //将json字符串转换成Java对象
        ObjectMapper mapper = new ObjectMapper();
        Device device = mapper.readValue(deviceJson, Device.class);
        //调用service层的方法添加图书信息
        int count = deviceService.addOne(device);
        //将结果封装到ResultData中
        ResultData<Device> resultData = new ResultData<>();
        if (count > 0) {
            resultData.setCode(200);
            resultData.setMsg("添加成功");
        } else {
            resultData.setCode(500);
            resultData.setMsg("添加失败");
        }
        //将resultData对象转换成json字符串
        String json = mapper.writeValueAsString(resultData);
        //设置响应的编码格式
        resp.setContentType("application/json;charset=utf-8");
        //将json数据响应给前端浏览器
        resp.getWriter().write(json);
    }

    //根据id查询一条数据
    public void one(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求参数
        String id = req.getParameter("id");
        //调用Service层查询单个的方法
        Device device = deviceService.selectOne(Integer.parseInt(id));
        List<Device> deviceList = new ArrayList<>();
        deviceList.add(device);
        //将结果封装到ResultData中
        ResultData<Device> resultData = new ResultData<>(200,"成功",1,deviceList);
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
        String deviceJson = req.getParameter("device");
        //将json字符串转换成Java对象
        ObjectMapper mapper = new ObjectMapper();
        Device device = mapper.readValue(deviceJson, Device.class);
        //调用service层的方法添加图书信息
        int count = deviceService.updateOne(device);
        //将结果封装到ResultData中
        ResultData<Device> resultData = new ResultData<>();
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

    //删除一条数据
    public void delete (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求参数
        String id = req.getParameter("id");
        //调用service层方法
        int count = deviceService.deleteOne(Integer.parseInt(id));
        //将结果集封装到ResultData中
        ResultData<Device> resultData = new ResultData<>();
        if (count > 0) {
            resultData.setCode(200);
            resultData.setMsg("删除成功");
        }else {
            resultData.setCode(500);
            resultData.setMsg("删除失败");
        }
        //将resultData对象转换成json字符串
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(resultData);
        //设置响应的编码格式
        resp.setContentType("application/json;charset=utf-8");
        //将json数据响应给前端浏览器
        resp.getWriter().write(json);
    }
}
