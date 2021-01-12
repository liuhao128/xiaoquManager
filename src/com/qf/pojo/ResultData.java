package com.qf.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/24 12:59
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultData<T> {
    private Integer code;
    private String msg;
    private Integer count;
    private List<T> data;
}
