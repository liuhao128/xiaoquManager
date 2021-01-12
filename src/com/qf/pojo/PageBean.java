package com.qf.pojo;

import com.qf.util.IPage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/18 19:12
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageBean<T>{
    //分页数据
    List<T> list;
    //总条数
    int total;
    //总页数
    int pages;
    //当前页
    int currentPage;
    //毎一页的条数
    int pageSize;

}
