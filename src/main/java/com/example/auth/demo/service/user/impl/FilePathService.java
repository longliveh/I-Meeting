package com.example.auth.demo.service.user.impl;

import com.example.auth.demo.entity.user.UserOpenInfo;
import com.example.auth.demo.entity.user.VerifyInfo;
import com.example.auth.demo.mapper.user.VerifyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.UUID;

@Service
public class FilePathService {
    @Autowired
    private VerifyMapper verifyMapper;

    @Value("${filepath.faceimage}")
    private String faceimagepath;

    @Value("${filepath.roomimage}")
    private String roomimage;

    public Boolean uploadFace(@RequestParam("file") MultipartFile file, UserOpenInfo userOpenInfo, long id) throws IOException {
        if (!file.isEmpty()) {
            // 获取文件名称,包含后缀
            String fileAllName = file.getOriginalFilename();
            String prefix = "." + fileAllName.substring(fileAllName.lastIndexOf(".") + 1);
            String fileName = "" + id + "_" + userOpenInfo.getName()+ "_" + (userOpenInfo.getSex() == 1 ? "男" : "女");

            // 存放在这个路径下：该路径是该工程目录下的static文件下：(注：该文件可能需要自己创建)
            // 放在static下的原因是，存放的是静态文件资源，即通过浏览器输入本地服务器地址，加文件名时是可以访问到的
            String path = faceimagepath;
            //String path = "./secret/userfaceimg/";

            // 该方法是对文件写入的封装，在util类中，导入该包即可使用，后面会给出方法
            fileupload(file.getBytes(), path+ id + "/", fileName, prefix);

            // 接着创建对应的实体类，将以下路径进行添加，然后通过数据库操作方法写入
            VerifyInfo verifyInfo = VerifyInfo.builder()
                    .faceimgpath( id + "/" + fileName + prefix)
                    .department_id(userOpenInfo.getDepartment())
                    .sex(userOpenInfo.getSex())
                    .common_id(userOpenInfo.getCommon_id())
                    .age(userOpenInfo.getAge())
                    .name(userOpenInfo.getName())
                    .id(id)
                    .build();
            return verifyMapper.addVerifyInfo(verifyInfo) > 0;
        }
        return false;
    }


    public Boolean uploadRoomImg(@RequestParam("file") MultipartFile file, int roomid) throws IOException {
        if (!file.isEmpty()) {
            // 获取文件名称,包含后缀
            String fileAllName = file.getOriginalFilename();
            String prefix = "." + fileAllName.substring(fileAllName.lastIndexOf(".") + 1);
            String fileName = "" + roomid + "_" + UUID.randomUUID().toString().substring(0,6);

            // 存放在这个路径下：该路径是该工程目录下的static文件下：(注：该文件可能需要自己创建)
            // 放在static下的原因是，存放的是静态文件资源，即通过浏览器输入本地服务器地址，加文件名时是可以访问到的
            String path = roomimage;
            //String path = "./secret/userfaceimg/";

            // 该方法是对文件写入的封装，在util类中，导入该包即可使用，后面会给出方法
            fileupload(file.getBytes(), path+ roomid + "/", fileName, prefix);

            // 接着创建对应的实体类，将以下路径进行添加，然后通过数据库操作方法写入
            return true;
        }
        return false;
    }



    public static void fileupload(byte[] file, String filePath, String fileName, String prefix) throws IOException {
        //目标目录
        File targetfile = new File(filePath);
        if (!targetfile.exists()) {
            targetfile.mkdirs();
        }

        //二进制流写入
        FileOutputStream out = new FileOutputStream(filePath + fileName+prefix);
        out.write(file);
        out.flush();
        out.close();
    }

}
