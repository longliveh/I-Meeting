package com.example.auth.demo.service.user.impl;

import com.example.auth.demo.domain.ResultCode;
import com.example.auth.demo.domain.ResultJson;
import com.example.auth.demo.domain.auth.ResponseUserToken;
import com.example.auth.demo.entity.user.UserDetail;
import com.example.auth.demo.entity.user.UserLogin;
import com.example.auth.demo.exception.CustomException;
import com.example.auth.demo.mapper.user.UserMapper;
import com.example.auth.demo.service.user.AuthService;
import com.example.auth.demo.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;


@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtTokenUtil;
    private final UserMapper userMapper;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, @Qualifier("CustomUserDetailsService") UserDetailsService userDetailsService, JwtUtils jwtTokenUtil, UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetail register(UserLogin userLogin) {
        final String phonenum = userLogin.getLoginid();
        Integer res = userMapper.findByPhonenum(phonenum);
        if(res != null) {
            throw new CustomException(ResultJson.failure(ResultCode.BAD_REQUEST, "用户已存在"));
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        final String rawPassword = userLogin.getPassword();
        UserDetail userDetail = new UserDetail(userLogin.getId(),UUID.randomUUID().toString().substring(0,10),phonenum,encoder.encode(rawPassword),1,new Date());
        userMapper.regist(userDetail);
        if (userDetail.getId()==0)
        {
            throw new CustomException(ResultJson.failure(ResultCode.SERVICE_REGIST_ERROR,"数据库插入失败"));
        }
        return userDetail;
    }

    @Override
    public ResponseUserToken login(String loginid, String password) {
        //用户验证
        final Authentication authentication = authenticate(loginid, password);
        //存储认证信息
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //生成token
        final UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        final String token = jwtTokenUtil.generateAccessToken(userDetail);
        //存储token
        //jwtTokenUtil.putToken(username, token);
        return new ResponseUserToken(token, userDetail);

    }

    @Override
    public void logout(String token) {
        token = token.substring(tokenHead.length());
        String userName = jwtTokenUtil.getUsernameFromToken(token);
        jwtTokenUtil.deleteToken(userName);
    }

    @Override
    public ResponseUserToken refresh(String oldToken) {
        String token = oldToken.substring(tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(token);
        UserDetail userDetail = (UserDetail) userDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.canTokenBeRefreshed(token, userDetail.getLastPasswordResetDate())){
            token =  jwtTokenUtil.refreshToken(token);
            return new ResponseUserToken(token, userDetail);
        }
        return null;
    }

    @Override
    public UserDetail getUserDetailById(long id) {
        return userMapper.findById(id);
    }

    private Authentication authenticate(String username, String password) {
        try {
            //该方法会去调用userDetailsService.loadUserByUsername()去验证用户名和密码，如果正确，则存储该用户名密码到“security 的 context中”
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException | BadCredentialsException e) {
            throw new CustomException(ResultJson.failure(ResultCode.LOGIN_ERROR, e.getMessage()));
        }
    }
}
