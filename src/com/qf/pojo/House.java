package com.qf.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 作者：SmallWood
 * 时间：2020/12/28 15:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class House {
    private Integer id;
    private String garden;
    private String buildingName;
    private String code;
    private String houseName;
    private String householderName;
    private String phone;
    private Integer houseNumber;
    private Integer unit;
    private Integer floor;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date time;

}
