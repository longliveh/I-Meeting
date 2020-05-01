package com.example.auth.demo.entity.user;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import java.util.Date;

@Data
public class UserOpenInfo {

    private Integer department;

    private String common_id;

    private String name;

    private int sex;

    private Integer age;

}
