package com.example.auth.demo.entity.user;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class VerifyInfo {
    private long id;

    private Integer department_id;

    private String faceimgpath;

    private String common_id;

    private String name;

    private int sex;

    private Integer age;


}
