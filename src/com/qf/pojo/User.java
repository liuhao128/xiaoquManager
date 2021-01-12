package com.qf.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qf.util.QueryName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 作者：SmallWood
 * 时间：2020/12/15 17:38
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String username;
    private String password;
    private String sex;
    private String phone;
    private String email;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date birthday;
}
