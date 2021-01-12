package com.qf.pojo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.sql.Time;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Pet {
    private Integer id;
    private String image;
    private String person;
    private String name;
    private String color;
    private String info;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date adoptTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @JsonIgnore
    private String file;
}
