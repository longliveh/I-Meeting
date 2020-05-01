package com.example.auth.demo.service.common.impl;

import com.example.auth.demo.entity.common.Department;
import com.example.auth.demo.mapper.user.VerifyMapper;
import com.example.auth.demo.service.common.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private VerifyMapper verifyMapper;

    @Override
    public List<Department> getDepartments() {
        return verifyMapper.getDepartments();
    }
}
