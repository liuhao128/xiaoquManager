package com.qf.pojo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Building {
    private Integer id;
    private String garden;
    private String code;
    private String name;
    private Integer number;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date time;
}
