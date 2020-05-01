package com.example.auth.demo.mapper.admin;

import com.example.auth.demo.entity.common.Room;
import com.example.auth.demo.entity.user.Role;
import com.example.auth.demo.entity.user.UserDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface AdminMapper {


    /**
     * 更新审核结果
     * @param id
     * @param state
     * @return
     */
    int changeVerifyState(@Param("id") int id,@Param("userid") long userid,@Param("state") int state);

    /**
     * 更新用户认证信息
     * @param id
     * @return
     */
    int updateUserDetail(@Param("id") long id,@Param("state") int state);


    /**
     * 获取待审核图片路径
     * @param id
     * @param userid
     * @return
     */
    String getVerifyImage(@Param("id") int id,@Param("userid") long userid);

    /**
     * 添加人脸新信息
     * @param userid
     * @param feature
     * @return
     */
    int addUserFaceFeature(@Param("userid")long userid,@Param("feature")byte[] feature);


    /**
     * 增加会议室
     * @param room
     * @return
     */
    int addRoom(Room room);


    /**
     * 更新会议室图片路径
     * @param roomid
     * @param path
     * @return
     */
    int updateimgPath(@Param("roomid")int roomid ,@Param("path") String path);
}
