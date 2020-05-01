package com.example.auth.demo.service.admin.impl;

import com.example.auth.demo.entity.common.ImageInfo;
import com.example.auth.demo.entity.common.Room;
import com.example.auth.demo.mapper.admin.AdminMapper;
import com.example.auth.demo.service.admin.AdminService;
import com.example.auth.demo.service.face.FaceEngineService;
import com.example.auth.demo.utils.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import java.io.*;
import java.net.URLDecoder;
import java.util.Arrays;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private FaceEngineService faceEngineService;

    @Value("${filepath.faceimage}")
    private String facepath;

    @Value("${filepath.roomimage}")
    private String roompath;

    @Override
    public boolean updateVerifyInfo(int id, long userid, int state) {
        if (true)//adminMapper.changeVerifyState(id,userid,state)>0
        {
            try {
                String path = facepath;
                path += adminMapper.getVerifyImage(id, userid);
                System.out.println(path);
                File file = new File(path);
                FileInputStream inputStream = new FileInputStream(file);
                ImageInfo imageInfo = ImageUtil.getRGBData(inputStream);
                byte[] bytes = faceEngineService.extractFaceFeature(imageInfo);
                if(adminMapper.addUserFaceFeature(userid, bytes)<1)
                {
                    return false;
                }
                if(adminMapper.updateUserDetail(userid, state == 1 ? 1 : 0)<1)
                {
                    return false;
                }
                if(adminMapper.changeVerifyState(id, userid, state)<1)
                {
                    return false;
                }
                return true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
        return false;
    }

    @Override
    public boolean addRoom(Room room) {
        adminMapper.addRoom(room);
        return room.getRoom_id() > 0;
    }

    @Override
    public boolean updateRoomImagePath(int roomid) {
        File file = new File(roompath + roomid);
        String[] fileName = file.list();
        String imagepath = Arrays.asList(fileName).toString();
        return adminMapper.updateimgPath(roomid, imagepath) > 0;
    }
}
