package com.example.auth.demo.mapper.user;


import com.example.auth.demo.entity.face.UserFaceInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFaceInfoMapper  {

    int insertUserFaceInfo(UserFaceInfo userFaceInfo);

    List<UserFaceInfo> selectList(UserFaceInfo userFaceInfo);

}
