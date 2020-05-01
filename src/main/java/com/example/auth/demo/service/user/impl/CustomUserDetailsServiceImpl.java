package com.example.auth.demo.service.user.impl;


import com.example.auth.demo.entity.user.UserDetail;
import com.example.auth.demo.mapper.user.UserMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 登陆身份认证
 * @author: JoeTao
 * createAt: 2018/9/14
 */
@Component(value="CustomUserDetailsService")
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private final UserMapper userMapper;

    public CustomUserDetailsServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetail loadUserByUsername(String login) throws UsernameNotFoundException {
        UserDetail userDetail = userMapper.findByLoginId(login);
        if (userDetail == null) {
            throw new UsernameNotFoundException(String.format("No userDetail found with username '%s'.", login));
        }
        return userDetail;
    }
}
