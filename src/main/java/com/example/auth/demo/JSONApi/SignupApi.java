package com.example.auth.demo.JSONApi;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class SignupApi {
    @ApiModelProperty(value = "手机号", required = true)
    @Size(min=1, max=20)
    private String phonenum;
    @ApiModelProperty(value = "密码", required = true)
    @Size(min=6, max=20)
    private String password;
    @ApiModelProperty(value = "验证码", required = true)
    @Size(min=1, max=20)
    private String smscode;
}
