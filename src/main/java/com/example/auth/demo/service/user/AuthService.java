package com.example.auth.demo.service.user;

import com.example.auth.demo.domain.auth.ResponseUserToken;
import com.example.auth.demo.entity.user.UserLogin;
import com.example.auth.demo.entity.user.UserDetail;

/**
 * @author: JoeTao
 * createAt: 2018/9/17
 */
public interface AuthService {
    /**
     * 注册用户
     * @param userLogin
     * @return
     */
    UserDetail register(UserLogin userLogin);

    /**
     * 登陆
     * @param username
     * @param password
     * @return
     */
    ResponseUserToken login(String username, String password);

    /**
     * 登出
     * @param token
     */
    void logout(String token);

    /**
     * 刷新Token
     * @param oldToken
     * @return
     */
    ResponseUserToken refresh(String oldToken);

    /**
     * 根据id获取用户信息
     * @param id
     * @return
     */
    UserDetail getUserDetailById(long id);
}
