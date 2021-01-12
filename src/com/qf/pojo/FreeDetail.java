package com.qf.pojo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class FreeDetail {
    private Integer id;
    private String garden;
    private String project;
    private String owner;
    private Double fromMoney;
    private Double actualMoney;
    private String info;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date freeTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
