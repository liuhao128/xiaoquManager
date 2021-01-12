package com.qf.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 作者：SmallWood
 * 时间：2020/12/31 11:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EchartsData {
    private String[] titles;
    private String[] xData;
    private List<EData> eData;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EData{
        private String name;
        private String type;
        private String[] data;
    }
}
