package com.qf.service;

import com.qf.pojo.PageBean;
import com.qf.pojo.Pet;

import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/29 15:13
 */
public interface PetService {
    //查询全部
    List<Pet> selectAll();
    //添加一条数据
    int addOne(Pet pet);
    //删除一条数据
    int deleteOne(int id);
    //根据id查询全部
    Pet selectOne(int id);
    //修改一条数据
    int updateOne(Pet pet);
    //分页查询
    PageBean<Pet> selectByLimit(String keywords, String currentPage, String pageSize);
}
