package com.example.auth.demo.entity.user;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
public class UserDetail implements UserDetails {
    private long id;
    private String nickname;        //昵称
    private String password;
    private String phonenum;        //登录账号
    private String common_id;       //登录账号
    private int role;
    private Date lastPasswordResetDate;
    private int isVerify;           //是否提交通过审核

    public UserDetail(long id, String nickname, String phonenum,String password, int role, Date lastPasswordResetDate) {
        this.id = id;
        this.nickname = nickname;
        this.phonenum = phonenum;
        this.password = password;
        this.role = role;
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public UserDetail(long id, String phonenum, int role) {
        this.id = id;
        this.phonenum = phonenum;
        this.role = role;
    }

    //返回分配给用户的角色列表
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(""+role));
        return authorities;
    }

    public long getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return phonenum;
    }

    /**
     * 账户是否未过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     *  账户是否未锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    /**
     * 密码是否未过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /** 账户是否激活
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }


    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUsername(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLastPasswordResetDate(Date lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }
}
