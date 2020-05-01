package com.example.auth.demo.controller.admin;


import com.example.auth.demo.JSONApi.DoVerifyInfo;
import com.example.auth.demo.domain.ResultCode;
import com.example.auth.demo.domain.ResultJson;
import com.example.auth.demo.service.admin.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(description = "管理员api")
@RequestMapping("/api/v1/admin")
public class VerifyController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/doVerifyInfo")
    @ApiOperation(value = "审核用户上传的信息",notes = "state 0->待审核 1->审核通过 2->审核驳回")
    public ResultJson doVerifyInfo(@RequestBody DoVerifyInfo verifyInfo) {
        if (verifyInfo.getState() < 0 || verifyInfo.getState() > 2) {
            return ResultJson.failure(ResultCode.BAD_REQUEST,"参数错误");
        }
        if (adminService.updateVerifyInfo(verifyInfo.getId(),verifyInfo.getUserid(), verifyInfo.getState()))
        {
            return ResultJson.ok();
        }
        return ResultJson.failure(ResultCode.SERVER_ERROR,"审核失败");
    }
}
