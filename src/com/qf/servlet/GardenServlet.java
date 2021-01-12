package com.qf.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qf.pojo.EchartsData;
import com.qf.pojo.Garden;
import com.qf.pojo.PageBean;
import com.qf.pojo.ResultData;
import com.qf.service.GardenService;
import com.qf.service.impl.GardenServiceImpl;
import com.qf.util.ExcelUtils;
import com.qf.util.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.*;

/**
 * 作者：SmallWood
 * 时间：2020/12/25 15:57
 */
@WebServlet("/garden/*")
@MultipartConfig(maxFileSize = 2*1024*1024 , maxRequestSize = 10 * 1024 * 1024)
public class GardenServlet extends BaseServlet {
    GardenService gardenService = new GardenServiceImpl();
    //查询全部
    public void selectAll (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取搜索关键字参数
        String keywords = req.getParameter("keywords");
        //获取分页参数
        String page = req.getParameter("page");
        String limit = req.getParameter("limit");
        //调用service层的方法查询所有的图书信息
        PageBean<Garden> pageBean = gardenService.selectByLimit(keywords,page, limit);
        //将结果集封装到ResultData中
        ResultData<Garden> resultData = new ResultData<>(0,"成功",pageBean.getTotal(),pageBean.getList());
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
        String gardenJson = req.getParameter("garden");
        //将json字符串转换成Java对象
        ObjectMapper mapper = new ObjectMapper();
        Garden garden = mapper.readValue(gardenJson, Garden.class);
        //调用service层的方法添加图书信息
        int count = gardenService.addOne(garden);
        //将结果封装到ResultData中
        ResultData<Garden> resultData = new ResultData<>();
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
        Garden garden = gardenService.selectOne(Integer.parseInt(id));
        List<Garden> gardenList = new ArrayList<>();
        gardenList.add(garden);
        //将结果封装到ResultData中
        ResultData<Garden> resultData = new ResultData<>(200,"成功",1,gardenList);
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
        String gardenJson = req.getParameter("garden");
        //将json字符串转换成Java对象
        ObjectMapper mapper = new ObjectMapper();
        Garden garden = mapper.readValue(gardenJson, Garden.class);
        //调用service层的方法添加图书信息
        int count = gardenService.updateOne(garden);
        //将结果封装到ResultData中
        ResultData<Garden> resultData = new ResultData<>();
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
        int count = gardenService.deleteOne(Integer.parseInt(id));
        //将结果集封装到ResultData中
        ResultData<Garden> resultData = new ResultData<>();
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

    /**
     * 导入Excel
     */
    public ResultData importData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取文件上传的内容
        Part part = req.getPart("file");
        //获取文件上传的名称的后缀
        String fileName = part.getSubmittedFileName();
        String fileExt = fileName.substring(fileName.lastIndexOf(".")+1);
        //调用工具类方法导入数据
        List<String[]> datas = ExcelUtils.importData(fileExt, part.getInputStream(), 1);
        //将list集合中的数据插入到数据库
        int count = gardenService.addGardens(datas);
        ResultData resultData = new ResultData();
        if(count > 0 ){
            resultData.setCode(200);
            resultData.setMsg("导入成功");
        }else{
            resultData.setCode(800);
            resultData.setMsg("导入失败");
        }
        return resultData;
    }
    /**
     * 导出Excel
     */
    public void exportData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取所有的book信息，并转换成List<String[]>
        List<String[]> datas = gardenService.exportBooks();
        //设置MIME类型
        resp.setContentType("application/vnd.ms-excel;charset=utf-8");
        //设置响应头，告知浏览器是下载操作
        resp.setHeader("Content-Disposition","attachment;filename=table.xls");
        String[] titles = {"图书编号","图书名称","图书作者","图书价格","图书库存","上架时间","图书状态","图书图片"};
        //调用工具中的导出方法
        ExcelUtils.export(titles,datas,resp.getOutputStream());
    }

    public EchartsData echartsData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //模拟EchartsData的数据(以后这个数据是从数据获取)
        EchartsData echartsData = new EchartsData();
        String[] titles = {"手机","电脑","配件","衣服"};
        //添加标题数据
        echartsData.setTitles(titles);
        String[] xData = {"一季度","二季度","三季度","四季度"};
        //添加x轴数据
        echartsData.setXData(xData);

        List<EchartsData.EData> eDatas = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < titles.length; i++) {
            EchartsData.EData eData = new EchartsData.EData();
            eData.setName(titles[i]);
            eData.setType("line");
            String[] datas = {String.valueOf(r.nextInt(1000)),String.valueOf(r.nextInt(1000)),String.valueOf(r.nextInt(1000)),String.valueOf(r.nextInt(1000))};
            eData.setData(datas);
            eDatas.add(eData);
        }
        //添加图表上的数据
        echartsData.setEData(eDatas);
        return echartsData;
    }

}
