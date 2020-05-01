package com.example.auth.demo.mapper.user;

import com.example.auth.demo.entity.user.Role;
import com.example.auth.demo.entity.user.UserDetail;
import com.example.auth.demo.entity.user.UserLogin;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserMapper {
    /**
     * 根据用户名查找用户
     * @param loginid
     * @return
     */
    UserDetail findByLoginId(@Param("loginid") String loginid);


    UserDetail findById(@Param("id") long id);


    /**
     * 根据手机号码查找用户
     * @param phonenum
     * @return
     */
    Integer findByPhonenum(@Param("phonenum")String phonenum);


    /**
     * 更新密码修改时间
     * @param id
     * @return
     */
    int updatePswdDate(@Param("id")long id);

    /**
     * 创建新用户
     * @param userDetail
     */
    int regist(UserDetail userDetail);


    /**
     * 添加用户详细信息
     * @param userDetail
     * @return
     */
    int insertUserDetail(UserDetail userDetail);


    /**
     * 根据角色id查找角色
     * @param roleId
     * @return
     */
    Role findRoleById(@Param("roleId") long roleId);

    /**
     * 根据用户id查找该用户角色
     * @param userId
     * @return
     */
    Role findRoleByUserId(@Param("userId") long userId);
}
