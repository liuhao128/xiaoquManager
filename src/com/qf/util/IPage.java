package com.qf.util;

import java.util.List;

/**
 * @author RW daze
 * @date 2020/12/23 19:05
 */
public interface IPage<T> {
    /**
     *  获得当前正显示到的页数
     * @return 页数
     */
    public Integer getCurrentPage();

    /**
     * 获得一页有多少条
     * @return 条数
     */
    public Integer getItemPerPage();

    /**
     * 设置查询结果
     * @param results 查询出的结果
     */
    public void setResult(List<T> results);

    /**
     * 设置查询出来的对象数量
     * @param itemCount 对象数量
     */
    public void setItemCount(Integer itemCount);

    /**
     * 设置查询出来的总页数
     * @param pageCount 总页数
     */
    public void setPageCount(Integer pageCount);
}
