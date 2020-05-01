package com.example.auth.demo.controller.admin;


import com.example.auth.demo.JSONApi.RoomApi;
import com.example.auth.demo.domain.ResultCode;
import com.example.auth.demo.domain.ResultJson;
import com.example.auth.demo.entity.common.Room;
import com.example.auth.demo.service.admin.AdminService;
import com.example.auth.demo.service.user.impl.FilePathService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(description = "管理会议室")
@RequestMapping("/api/v1/admin")
public class ManageRoomController {

    @Autowired
    private AdminService adminService;


    @Autowired
    private FilePathService filePathService;

    @PostMapping("/addroom")
    public ResultJson addRoom(@RequestBody RoomApi roomApi) {
        Room room = new Room(roomApi);
        adminService.addRoom(room);
        return ResultJson.ok(room);
    }

    //多文件上传
    @PostMapping("/addroomimage")
    public ResultJson handleFileUpload(@RequestParam(value = "files",required = true)MultipartFile[] multipartFiles,int roomid){
        System.out.println(multipartFiles.length);
        if (multipartFiles.length>0) {
            for (MultipartFile multipartFile : multipartFiles) {
                try {
                    filePathService.uploadRoomImg(multipartFile,roomid);
                    adminService.updateRoomImagePath(roomid);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ResultJson.ok();
    }
}
