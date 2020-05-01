package com.example.auth.demo.domain.auth;

import com.example.auth.demo.entity.user.UserDetail;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ResponseUserToken {
    private String token;
    private UserDetail userDetail;
}
