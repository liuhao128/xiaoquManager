package com.qf.pojo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Device {
    private Integer id;
    private String garden;
    private String code;
    private String name;
    private String brand;
    private Double price;
    private Integer number;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date date;
    private Integer year;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;
}
