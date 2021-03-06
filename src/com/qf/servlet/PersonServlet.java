package com.qf.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qf.dao.PersonDao;
import com.qf.dao.impl.PersonDaoImpl;
import com.qf.pojo.*;
import com.qf.service.PersonService;
import com.qf.service.impl.PersonServiceImpl;
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
 * 时间：2020/12/29 19:06
 */
@WebServlet("/person/*")
@MultipartConfig(maxFileSize = 2*1024*1024 , maxRequestSize = 10 * 1024 * 1024)
public class PersonServlet extends BaseServlet{
    PersonService personService = new PersonServiceImpl();
    //查询全部
    public void selectAll (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取搜索关键字参数
        String keywords = req.getParameter("keywords");
        //获取分页参数
        String page = req.getParameter("page");
        String limit = req.getParameter("limit");
        //调用service层的方法查询所有的图书信息
        PageBean<Person> pageBean = personService.selectByLimit(keywords,page, limit);
        //将结果集封装到ResultData中
        ResultData<Person> resultData = new ResultData<>(0,"成功",pageBean.getTotal(),pageBean.getList());
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
        String personJson = req.getParameter("person");
        //将json字符串转换成Java对象
        ObjectMapper mapper = new ObjectMapper();
        Person person = mapper.readValue(personJson, Person.class);
        //调用service层的方法添加图书信息
        int count = personService.addOne(person);
        //将结果封装到ResultData中
        ResultData<Person> resultData = new ResultData<>();
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
        Person person = personService.selectOne(Integer.parseInt(id));
        List<Person> personList = new ArrayList<>();
        personList.add(person);
        //将结果封装到ResultData中
        ResultData<Person> resultData = new ResultData<>(200,"成功",1,personList);
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
        String personJson = req.getParameter("person");
        //将json字符串转换成Java对象
        ObjectMapper mapper = new ObjectMapper();
        Person person = mapper.readValue(personJson, Person.class);
        //调用service层的方法添加图书信息
        int count = personService.updateOne(person);
        //将结果封装到ResultData中
        ResultData<Person> resultData = new ResultData<>();
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
        int count = personService.deleteOne(Integer.parseInt(id));
        //将结果集封装到ResultData中
        ResultData<Person> resultData = new ResultData<>();
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
