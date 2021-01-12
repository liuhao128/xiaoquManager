package com.qf.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qf.pojo.Building;
import com.qf.pojo.House;
import com.qf.pojo.PageBean;
import com.qf.pojo.ResultData;
import com.qf.service.BuildingService;
import com.qf.service.impl.BuildingServiceImpl;

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
 * 时间：2020/12/29 15:51
 */
@WebServlet("/building/*")
@MultipartConfig(maxFileSize = 2*1024*1024 , maxRequestSize = 10 * 1024 * 1024)
public class BuildingServlet extends BaseServlet {
    BuildingService buildingService = new BuildingServiceImpl();
    //查询全部
    public void selectAll (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取搜索关键字参数
        String keywords = req.getParameter("keywords");
        //获取分页参数
        String page = req.getParameter("page");
        String limit = req.getParameter("limit");
        //调用service层的方法查询所有的图书信息
        PageBean<Building> pageBean = buildingService.selectByLimit(keywords,page, limit);
        //将结果集封装到ResultData中
        ResultData<Building> resultData = new ResultData<>(0,"成功",pageBean.getTotal(),pageBean.getList());
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
        String buildingJson = req.getParameter("building");
        //将json字符串转换成Java对象
        ObjectMapper mapper = new ObjectMapper();
        Building building = mapper.readValue(buildingJson, Building.class);
        //调用service层的方法添加图书信息
        int count = buildingService.addOne(building);
        //将结果封装到ResultData中
        ResultData<Building> resultData = new ResultData<>();
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
        Building building = buildingService.selectOne(Integer.parseInt(id));
        List<Building> buildingList = new ArrayList<>();
        buildingList.add(building);
        //将结果封装到ResultData中
        ResultData<Building> resultData = new ResultData<>(200,"成功",1,buildingList);
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
        String buildingJson = req.getParameter("building");
        //将json字符串转换成Java对象
        ObjectMapper mapper = new ObjectMapper();
        Building building = mapper.readValue(buildingJson, Building.class);
        //调用service层的方法添加图书信息
        int count = buildingService.updateOne(building);
        //将结果封装到ResultData中
        ResultData<Building> resultData = new ResultData<>();
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
        int count = buildingService.deleteOne(Integer.parseInt(id));
        //将结果集封装到ResultData中
        ResultData<Building> resultData = new ResultData<>();
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
