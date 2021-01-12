package com.qf.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qf.util.QueryIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 作者：SmallWood
 * 时间：2020/12/25 15:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Garden {
    private Integer id;
    private String code;
    private String name;
    private String address;
    private Double area;
    private Integer building;
    private String green;
    private String image;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date time;
    @JsonIgnore
    private String file;

}
