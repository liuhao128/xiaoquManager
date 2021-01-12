package com.qf.service.impl;

import com.qf.dao.PersonDao;
import com.qf.dao.impl.PersonDaoImpl;
import com.qf.pojo.Building;
import com.qf.pojo.PageBean;
import com.qf.pojo.Person;
import com.qf.service.PersonService;

import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/29 18:21
 */
public class PersonServiceImpl implements PersonService {
    PersonDao personDao = new PersonDaoImpl();
    @Override
    public List<Person> selectAll() {
        return personDao.selectAll();
    }

    @Override
    public int addOne(Person person) {
        return personDao.addOne(person);
    }

    @Override
    public int deleteOne(int id) {
        return personDao.deleteOne(id);
    }

    @Override
    public Person selectOne(int id) {
        return personDao.selectOne(id);
    }

    @Override
    public int updateOne(Person person) {
        return personDao.updateOne(person);
    }

    @Override
    public PageBean<Person> selectByLimit(String keywords, String currentPage, String pageSize) {
        int cPage=1;
        if (currentPage != null) {
            cPage = Integer.parseInt(currentPage);
        }
        int pSize = 3;
        if (pageSize != null) {
            pSize = Integer.parseInt(pageSize);
        }
        //调用dao方法,获得分页查询的数据
        List<Person> personList = personDao.selectByLimit(keywords,cPage, pSize);
        //总条数total
        int total = personDao.count();
        //总页数pages
        int pages = total % pSize  == 0 ? total / pSize : total / pSize + 1;
        PageBean<Person> pageBean = new PageBean<>(personList,total,pages,cPage,pSize);
        return pageBean;
    }
}
