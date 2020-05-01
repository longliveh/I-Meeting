package com.example.auth.demo.service.face.impl;

import com.example.auth.demo.entity.face.UserFaceInfo;
import com.example.auth.demo.mapper.user.UserFaceInfoMapper;
import com.example.auth.demo.service.face.UserFaceInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserFaceInfoServiceImpl implements UserFaceInfoService {


    @Autowired
    private UserFaceInfoMapper userFaceInfoMapper;

    @Override
    public int addUserFaceInfo(UserFaceInfo userFaceInfo) {
        return userFaceInfoMapper.insertUserFaceInfo(userFaceInfo);
    }
}
