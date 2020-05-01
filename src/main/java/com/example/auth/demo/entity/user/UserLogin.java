package com.example.auth.demo.entity.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;


@Builder()
@Data
public class UserLogin {
    private long id;

    private String loginid;

    private String password;

//    public UserLogin(UserLoginApi userLoginApi,long id) {
//        this.loginid = userLoginApi.getLoginid();
//        this.password = userLoginApi.getPassword();
//        this.id = id;
//    }
}
