package com.example.auth.demo.JSONApi;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;


@Builder
@Data
public class UserLoginApi {
    @ApiModelProperty(value = "用户名", required = true)
    @Size(min=1, max=20)
    private String loginid;
    @ApiModelProperty(value = "密码", required = true)
    @Size(min=6, max=20)
    private String password;

}
