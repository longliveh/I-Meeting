package com.example.auth.demo.controller.book;


import com.example.auth.demo.domain.ResultCode;
import com.example.auth.demo.domain.ResultJson;
import com.example.auth.demo.entity.common.ImageInfo;
import com.example.auth.demo.entity.user.UserOpenInfo;
import com.example.auth.demo.service.common.CommonService;
import com.example.auth.demo.service.face.FaceEngineService;
import com.example.auth.demo.service.face.UserFaceInfoService;
import com.example.auth.demo.service.user.impl.FilePathService;
import com.example.auth.demo.utils.ImageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@Api(description = "上传审核信息")
@RequestMapping("/api/v1/user")
public class UserVerifyController {

    public final static Logger logger = LoggerFactory.getLogger(UserVerifyController.class);

    @Autowired
    private FilePathService filePathService;

    @Autowired
    private CommonService commonService;


    @Autowired
    private FaceEngineService faceEngineService;

    @GetMapping(value = "/demo")
    public String demo() {
        return "demo";
    }


    @PostMapping(value = "/testfaceimg")
    @ApiOperation(value = "上传审核信息")
    public ResultJson testfaceimg(@RequestParam("file") MultipartFile file)
    {

        ImageInfo imageInfo = null;
        try {
            imageInfo = ImageUtil.getRGBData(file.getInputStream());
            byte[] data = faceEngineService.extractFaceFeature(imageInfo);
            if (data!=null)
            {
                return ResultJson.ok();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ResultJson.failure(ResultCode.NO_FACE_DETECTED);
    }

    @PostMapping(value = "/verify")
    @ApiOperation(value = "上传审核信息")
    public ResultJson faceAdd(@RequestParam("file") MultipartFile file, UserOpenInfo userOpenInfo, HttpServletRequest request) {
        try {
            if (file == null) {
                return ResultJson.failure(ResultCode.BAD_REQUEST, "file is null");
            }
            if (org.apache.commons.lang3.StringUtils.isAnyBlank(userOpenInfo.getCommon_id(), userOpenInfo.getName()) || (userOpenInfo.getSex() != 1 && userOpenInfo.getSex() != 2) || userOpenInfo.getAge() == null) {
                return ResultJson.failure(ResultCode.BAD_REQUEST);
            }
            filePathService.uploadFace(file,userOpenInfo,(long)request.getAttribute("user_id"));

        } catch (IOException e) {
            logger.error("file getInputStream() Exception");
            return ResultJson.failure(ResultCode.BAD_REQUEST, "file getInputStream() Exception");
        }
        return ResultJson.ok();
    }


    @GetMapping("/departments")
    @ApiOperation(value = "获取部门列表")
    public ResultJson getDepartments() {
        return ResultJson.ok(commonService.getDepartments());
    }



}
