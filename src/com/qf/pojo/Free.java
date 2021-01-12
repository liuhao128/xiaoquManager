package com.qf.pojo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Free {
    private Integer id;
    private String garden;
    private String code;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;
}
