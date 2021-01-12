package com.qf.dao;

import com.qf.pojo.Person;
import com.qf.pojo.Pet;

import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/29 13:41
 */
public interface PetDao {
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
    List<Pet> selectByLimit(String keywords,int currentPage, int pageSize);
    //查询总条数
    int count();
}
