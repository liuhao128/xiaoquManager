package com.qf.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qf.pojo.Car;
import com.qf.pojo.PageBean;
import com.qf.pojo.Pet;
import com.qf.pojo.ResultData;
import com.qf.service.PetService;
import com.qf.service.impl.PetServiceImpl;
import com.qf.util.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：SmallWood
 * 时间：2020/12/30 0:24
 */
@WebServlet("/pet/*")
@MultipartConfig(maxFileSize = 2*1024*1024 , maxRequestSize = 10 * 1024 * 1024)
public class PetServlet extends BaseServlet {
    PetService petService = new PetServiceImpl();
    //查询全部
    public void selectAll (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取搜索关键字参数
        String keywords = req.getParameter("keywords");
        //获取分页参数
        String page = req.getParameter("page");
        String limit = req.getParameter("limit");
        //调用service层的方法查询所有的图书信息
        PageBean<Pet> pageBean = petService.selectByLimit(keywords,page, limit);
        //将结果集封装到ResultData中
        ResultData<Pet> resultData = new ResultData<>(0,"成功",pageBean.getTotal(),pageBean.getList());
        //将resultData对象转换成json字符串
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(resultData);
        //设置响应的编码格式
        resp.setContentType("application/json;charset=utf-8");
        //将json数据相应给前端浏览器
        resp.getWriter().write(json);
    }

    //上传一张图片
    public void upload(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String realPath = "e:\\upload";
        Part part = req.getPart("file");
        //获取上传的文件名
        String fileName = part.getSubmittedFileName();
        if(!fileName.equals("")){
            //获取新的文件名称
            fileName = FileUtils.getNewFileName(fileName);
            //获取新的文件路径
            String filePath = FileUtils.getNewFilePath(realPath,fileName);
            part.write(filePath + "\\" + fileName);
            //获取数据库保存的路径
            String savePath = FileUtils.getSavePath(filePath, fileName);

            Map<String,Object> map = new HashMap<>();
            map.put("code",0);
            map.put("msg","上传成功");
            map.put("image",savePath);

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(map);
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().write(json);
        }
    }

    //添加一条数据
    public void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String petJson = req.getParameter("pet");
        //将json字符串转换成Java对象
        ObjectMapper mapper = new ObjectMapper();
        Pet pet = mapper.readValue(petJson, Pet.class);
        //调用service层的方法添加图书信息
        int count = petService.addOne(pet);
        //将结果封装到ResultData中
        ResultData<Pet> resultData = new ResultData<>();
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
        Pet pet = petService.selectOne(Integer.parseInt(id));
        List<Pet> petList = new ArrayList<>();
        petList.add(pet);
        //将结果封装到ResultData中
        ResultData<Pet> resultData = new ResultData<>(200,"成功",1,petList);
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
        String petJson = req.getParameter("pet");
        //将json字符串转换成Java对象
        ObjectMapper mapper = new ObjectMapper();
        Pet pet = mapper.readValue(petJson, Pet.class);
        //调用service层的方法添加图书信息
        int count = petService.updateOne(pet);
        //将结果封装到ResultData中
        ResultData<Pet> resultData = new ResultData<>();
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
        int count = petService.deleteOne(Integer.parseInt(id));
        //将结果集封装到ResultData中
        ResultData<Car> resultData = new ResultData<>();
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
