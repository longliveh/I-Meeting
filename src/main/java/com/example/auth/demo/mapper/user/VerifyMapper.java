package com.example.auth.demo.mapper.user;

import com.example.auth.demo.entity.common.Department;
import com.example.auth.demo.entity.user.VerifyInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VerifyMapper {
    int addVerifyInfo(VerifyInfo verifyInfo);
    List<Department> getDepartments();
}
