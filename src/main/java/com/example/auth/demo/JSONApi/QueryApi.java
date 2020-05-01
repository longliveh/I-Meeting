package com.example.auth.demo.JSONApi;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class QueryApi {
    private Num_limit num_limit;
    private Date querydate;
    private String type;
    private List<String> device;

    @Data
    public class Num_limit{
        private Integer min;
        private Integer max;
    }
}
